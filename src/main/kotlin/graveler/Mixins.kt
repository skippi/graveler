package graveler

import graveler.util.pointedAt
import graveler.util.scheduler
import graveler.util.stressMap
import net.minecraft.block.Blocks
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorld
import net.minecraft.world.World

object Mixins {
  @JvmStatic
  fun updateNeighbors(world: IWorld, pos: BlockPos) {
    if (world.isRemote) return

    UPDATE_ORDER
      .map { pos.offset(it) }
      .map { world.world.pointedAt(it) }
      .forEach { it.tickStress() }
  }

  @JvmStatic
  fun onBlockAdded(world: World, pos: BlockPos) {
    if (world.isRemote) return

    world.pointedAt(pos).tickStress()
  }

  private fun PointedWorld.tickStress() {
    val stresses = world.stressMap?.stresses ?: return

    if (blockState.isAir(world, pos)) {
      stresses.remove(pos)
      return
    }

    if (blockState.block != Blocks.BLACK_WOOL) return

    val newStress = stress
    if (newStress >= 7) {
      world.scheduler?.schedule(Fall(pos))
    }

    if (stresses[pos] != newStress) {
      stresses[pos] = newStress
      updateNeighbors(world, pos)
    }
  }

  private val UPDATE_ORDER = arrayOf(Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH, Direction.DOWN, Direction.UP)
}
