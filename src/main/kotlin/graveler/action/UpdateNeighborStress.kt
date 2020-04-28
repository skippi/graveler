package graveler.action

import graveler.util.pointedAt
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos

data class UpdateNeighborStress(val pos: BlockPos) : Action {
  override val weight: Double = 0.0

  override fun apply(context: ActionContext) {
    val world = context.world

    UPDATE_ORDER
      .map { pos.offset(it) }
      .map { world.pointedAt(it) }
      .forEach { context.scheduler.schedule(UpdateStress(it.pos)) }
  }

  companion object {
    private val UPDATE_ORDER = arrayOf(Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH, Direction.DOWN, Direction.UP)
  }
}
