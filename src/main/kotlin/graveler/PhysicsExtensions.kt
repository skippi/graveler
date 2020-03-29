package graveler

import graveler.CapabilityUtil.*
import graveler.math.Bounds
import graveler.math.Vec3Util.*
import java.util.ArrayDeque
import java.util.HashSet
import net.minecraft.block.*
import net.minecraft.block.BlockState
import net.minecraft.entity.item.FallingBlockEntity
import net.minecraft.util.Direction
import net.minecraft.util.math.*
import net.minecraft.world.World
import org.apache.logging.log4j.LogManager

private val LOGGER = LogManager.getLogger()

fun World.fallAt(pos: BlockPos) {
  val targetBounds = Bounds(pos, Vec3i(64, 64, 64))

  if (!isAreaLoaded(targetBounds)) {
    forceInstantFallAt(pos)
    return
  }

  if (isRemote) {
    return
  }

  forceFallAt(pos)
}

private fun World.forceFallAt(pos: BlockPos) {
  addEntity(
    FallingBlockEntity(
      this,
      pos.x + 0.5,
      pos.y.toDouble(),
      pos.z + 0.5,
      getBlockState(pos)))
}

private fun World.forceInstantFallAt(pos: BlockPos) {
  val state = getBlockState(pos)

  setBlockState(pos, Blocks.AIR.defaultState)

  var newPos = pos.down()
  while (getBlockState(newPos).allowsFallThrough && newPos.y > 0) {
    newPos = newPos.down()
  }

  if (newPos.y > 0) {
    // Forge: Fix loss of state information during world gen.
    setBlockState(newPos.up(), state)
  }
}

private fun World.hasSupportingBlockAt(pos: BlockPos): Boolean {
  val state = getBlockState(pos)
  val downState = getBlockState(pos.down())

  return state.allowsSupporting && !downState.allowsFallThrough
}

private fun World.isAreaLoaded(bounds: Bounds): Boolean {
  return isAreaLoaded(BlockPos(bounds.min), BlockPos(bounds.max))
}

private fun World.isStableAt(pos: BlockPos): Boolean {
  if (pos.y < 0) {
    return true
  }

  val state = getBlockState(pos)
  if (!state.allowsFalling) {
    return true
  }

  val downState = getBlockState(pos.down())
  if (!downState.allowsFallThrough) {
    return true
  }

  val visited = HashSet<BlockPos>()
  val posesToVisit = ArrayDeque<BlockPos>()
  posesToVisit.add(pos)

  val originAdhesion = state.adhesion
  var count = 0

  while (!posesToVisit.isEmpty() && count < 128) {
    val currentPos = posesToVisit.remove()
    if (visited.contains(currentPos)) {
      continue
    }

    ++count
    visited.add(currentPos)

    val currentPosState = getBlockState(currentPos)
    if (!currentPosState.allowsSupporting) {
      continue
    }

    val combinedAdhesion = (0.7f * originAdhesion + 0.3f * currentPosState.adhesion)
    val distToPos = norm(subtract(currentPos, pos))
    if (distToPos > combinedAdhesion) {
      continue
    }

    if (hasSupportingBlockAt(currentPos)) {
      return true
    }

    for (i in 0 until 4) {
      val dir = Direction.byHorizontalIndex(i)
      posesToVisit.add(currentPos.offset(dir))
    }
  }

  return false
}

fun World.triggerGravityAt(origin: BlockPos) {
  val visited = HashSet<BlockPos>()
  val posesToVisit = ArrayDeque<BlockPos>()
  posesToVisit.add(origin)

  for (i in 0 until 6) {
    posesToVisit.add(origin.offset(Direction.byIndex(i)))
  }

  val scheduler = getSchedulerOption(this).orElseThrow(::NullPointerException)
  var count = 0

  while (!posesToVisit.isEmpty() && count < 512) {
    val pos = posesToVisit.remove()
    if (visited.contains(pos)) {
      continue
    }

    count += 1
    visited.add(pos)

    if (!getBlockState(pos).allowsFalling) {
      continue
    }

    if (!isStableAt(pos)) {
      scheduler.schedule(Fall(pos))

      posesToVisit.add(pos.offset(Direction.DOWN))

      for (i in 0 until 4) {
        val dir = Direction.byHorizontalIndex(i)
        posesToVisit.add(pos.offset(dir))
      }

      posesToVisit.add(pos.offset(Direction.UP))
    } else {
      for (i in 0 until 4) {
        val dir = Direction.byHorizontalIndex(i)
        posesToVisit.add(pos.offset(dir))
      }
    }
  }
}

private val BlockState.adhesion: Float
  get() {
    val hardness = Math.max(Math.min(getBlockHardness(null, null), 10f), 0.6f)
    return 2 * hardness
  }

private val BlockState.allowsFalling: Boolean
  get() {
    return (!isPassable &&
        !isLiquid &&
        block != Blocks.BEDROCK &&
        !(block is FallingBlock) &&
        !(block is LeavesBlock))
  }

private val BlockState.allowsSupporting: Boolean
  get() {
    return (!isPassable && !isLiquid && !(block is LeavesBlock))
  }

private val BlockState.allowsFallThrough: Boolean
  get() {
    return ((isPassable || isLiquid) && !(block is CauldronBlock))
  }

private val BlockState.isLiquid: Boolean
  get() {
    return material.isLiquid()
  }

val BlockState.isPassable: Boolean
  get() {
    return !material.blocksMovement() // TODO: Fix this broken hack
    // try {
    //   return getBlock().isPassable(null, null)
    // } catch (NullPointerException e) {

    // }
  }
