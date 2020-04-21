package graveler.util

import graveler.Fall
import graveler.SchedulerProvider.Companion.scheduler
import graveler.math.Bounds
import graveler.math.minus
import graveler.math.norm
import java.util.*
import net.minecraft.block.BlockCauldron
import net.minecraft.block.BlockFalling
import net.minecraft.block.BlockLeaves
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.item.EntityFallingBlock
import net.minecraft.init.Blocks
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3i
import net.minecraft.world.World

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
  spawnEntity(
    EntityFallingBlock(
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
    val distToPos = (currentPos - pos).norm
    if (distToPos > combinedAdhesion) {
      continue
    }

    if (hasSupportingBlockAt(currentPos)) {
      return true
    }

    for (i in 0 until 4) {
      val dir = EnumFacing.getHorizontal(i)
      posesToVisit.add(currentPos.offset(dir))
    }
  }

  return false
}

fun World.triggerGravityAt(origin: BlockPos) {
  val scheduler = this.scheduler ?: return
  val visited = HashSet<BlockPos>()
  val posesToVisit = ArrayDeque<BlockPos>()
  posesToVisit.add(origin)

  for (i in 0 until 6) {
    posesToVisit.add(origin.offset(EnumFacing.VALUES[i]))
  }

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

      posesToVisit.add(pos.offset(EnumFacing.DOWN))

      for (i in 0 until 4) {
        val dir = EnumFacing.getHorizontal(i)
        posesToVisit.add(pos.offset(dir))
      }

      posesToVisit.add(pos.offset(EnumFacing.UP))
    } else {
      for (i in 0 until 4) {
        val dir = EnumFacing.getHorizontal(i)
        posesToVisit.add(pos.offset(dir))
      }
    }
  }
}

private val IBlockState.adhesion: Float
  get() {
    val hardness = getBlockHardness(null, null)
    val coercedHardness = hardness
      .coerceAtLeast(0.6f)
      .coerceAtMost(10f)

    return 2 * coercedHardness
  }

private val IBlockState.allowsFalling: Boolean
  get() = !isPassable && !isLiquid &&
    block != Blocks.BEDROCK &&
    block !is BlockFalling &&
    block !is BlockLeaves

private val IBlockState.allowsSupporting: Boolean
  get() = !isPassable && !isLiquid && block !is BlockLeaves

private val IBlockState.allowsFallThrough: Boolean
  get() = (isPassable || isLiquid) && block !is BlockCauldron

private val IBlockState.isLiquid: Boolean get() = material.isLiquid

val IBlockState.isPassable: Boolean
  get() {
    return !material.blocksMovement() // TODO: Fix this broken hack
    // try {
    //   return getBlock().isPassable(null, null)
    // } catch (NullPointerException e) {

    // }
  }
