package graveler

import graveler.action.UpdateStress
import graveler.util.pointedAt
import graveler.util.scheduler
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
      .forEach { world.world.scheduler?.schedule(UpdateStress(it.pos)) }
  }

  @JvmStatic
  fun onBlockAdded(world: World, pos: BlockPos) {
    if (world.isRemote) return

    world.scheduler?.schedule(UpdateStress(pos))
  }

  private val UPDATE_ORDER = arrayOf(Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH, Direction.DOWN, Direction.UP)
}
