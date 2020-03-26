package graveler

import graveler.CapabilityExtensions._
import graveler.math.Bounds
import graveler.math.Vec3iExtensions._
import net.minecraft.block._
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.item.EntityFallingBlock
import net.minecraft.init.Blocks
import net.minecraft.util.EnumFacing
import net.minecraft.util.math._
import net.minecraft.world.World
import scala.collection.mutable._
import scala.math._
import scala.util.control.Breaks._

object PhysicsExtensions {
  implicit class ExtendedWorld(private val world: World) {
    require(world != null)

    def fallAt(pos: BlockPos): World = {
      val targetBounds = Bounds(pos, new Vec3i(64, 64, 64))

      if (!world.isAreaLoaded(targetBounds)) {
        return forceInstantFallAt(pos)
      }

      if (world.isRemote) return world

      forceFallAt(pos)
    }

    private def forceFallAt(pos: BlockPos): World = {
      world.spawnEntity(
        new EntityFallingBlock(
          world,
          pos.getX + 0.5d,
          pos.getY.toDouble,
          pos.getZ + 0.5d,
          world.getBlockState(pos)
        )
      )

      world
    }

    private def forceInstantFallAt(pos: BlockPos): World = {
      val state = world.getBlockState(pos)
      if (state == null) return world

      world.setBlockToAir(pos)

      var newPos = pos.down
      while (world.getBlockState(newPos).allowsFallThrough && newPos.getY > 0) {
        newPos = newPos.down
      }

      if (newPos.getY > 0) {
        // Forge: Fix loss of state information during world gen.
        world.setBlockState(newPos.up, state)
      }

      world
    }

    def hasSupportingBlockAt(pos: BlockPos): Boolean = {
      val state = world.getBlockState(pos)
      val downState = world.getBlockState(pos.down)

      state.allowsSupporting && !downState.allowsFallThrough
    }

    def isAreaLoaded(bounds: Bounds): Boolean = {
      world.isAreaLoaded(new BlockPos(bounds.min), new BlockPos(bounds.max))
    }

    def isStableAt(pos: BlockPos): Boolean = {
      if (pos.getY < 0) return true

      val state = world.getBlockState(pos)
      if (!state.allowsFalling) return true

      val downState = world.getBlockState(pos.down)
      if (!downState.allowsFallThrough) return true

      val visited = new HashSet[BlockPos]
      val posesToVisit = new Queue[BlockPos]
      posesToVisit.enqueue(pos)

      val originAdhesion = state.adhesion
      var count = 0

      while (posesToVisit.nonEmpty && count < 128) {
        breakable {
          val currentPos = posesToVisit.dequeue
          if (visited.contains(currentPos)) {
            break
          }

          count += 1
          visited.add(currentPos)

          val currentPosState = world.getBlockState(currentPos)
          if (!currentPosState.allowsSupporting) {
            break
          }

          val combinedAdhesion =
            blend(originAdhesion, currentPosState.adhesion, 0.7f)
          if ((currentPos - pos).norm > combinedAdhesion) {
            break
          }

          if (world.hasSupportingBlockAt(currentPos)) {
            return true
          }

          posesToVisit.enqueueAll(EnumFacing.HORIZONTALS.map(currentPos.offset))
        }
      }

      false
    }

    private def blend(a: Float, b: Float, ratio: Float): Float = {
      require(0 <= ratio && ratio <= 1)

      ratio * a + (1 - ratio) * b
    }

    def triggerGravityAt(origin: BlockPos): World = {
      val visited = new HashSet[BlockPos]

      val posesToVisit = new Queue[BlockPos]
      posesToVisit.enqueue(origin)
      posesToVisit.enqueueAll(EnumFacing.VALUES.map(origin.offset))

      var count = 0
      val scheduler = world.getSchedulerOption.get

      while (posesToVisit.nonEmpty && count < 256) {
        breakable {
          val pos = posesToVisit.dequeue
          if (visited.contains(pos)) {
            break
          }

          count += 1
          visited += pos

          if (!world.getBlockState(pos).allowsFalling) {
            break
          }

          if (!world.isStableAt(pos)) {
            scheduler.schedule(Fall(pos))

            posesToVisit.enqueue(pos.offset(EnumFacing.DOWN))
            posesToVisit.enqueueAll(EnumFacing.HORIZONTALS.map(pos.offset))
            posesToVisit.enqueue(pos.offset(EnumFacing.UP))
          } else {
            posesToVisit.enqueueAll(EnumFacing.HORIZONTALS.map(pos.offset))
          }
        }
      }

      world
    }
  }

  implicit class ExtendedIBlockState(private val state: IBlockState) {
    require(state != null)
    require(state.getBlock != null)
    require(state.getMaterial != null)

    def adhesion: Float = {
      val hardness = max(min(state.getBlockHardness(null, null), 10f), 0.6f)
      2 * hardness
    }

    def allowsFalling: Boolean = {
      val block = state.getBlock

      !(isAir
        || isLiquid
        || isPortal
        || isPassable
        || block == Blocks.BEDROCK
        || block.isInstanceOf[BlockFalling]
        || block.isInstanceOf[BlockSign]
        || block.isInstanceOf[BlockTorch]
        || block.isInstanceOf[BlockLeaves])
    }

    def allowsSupporting: Boolean = {
      val block = state.getBlock

      !(isAir
        || isBush
        || isLiquid
        || isPassable
        || block.isInstanceOf[BlockFalling]
        || block.isInstanceOf[BlockLeaves])
    }

    def allowsFallThrough: Boolean = {
      val block = state.getBlock

      ((block == Blocks.FIRE
      || isAir
      || isBush
      || isLiquid
      || isPassable)
      && !block.isInstanceOf[BlockCauldron])
    }

    def isAir: Boolean = state.getMaterial == Material.AIR

    def isBush: Boolean = state.getBlock.isInstanceOf[BlockBush]

    def isGrowable: Boolean = state.getBlock.isInstanceOf[IGrowable]

    def isLiquid: Boolean = state.getBlock.isInstanceOf[BlockLiquid]

    def isPassable: Boolean = try {
      state.getBlock.isPassable(null, null)
    } catch {
      case _: NullPointerException =>
        !state.getMaterial.blocksMovement // TODO: Fix this broken hack
    }

    def isPortal: Boolean = state.getBlock.isInstanceOf[BlockPortal]
  }
}
