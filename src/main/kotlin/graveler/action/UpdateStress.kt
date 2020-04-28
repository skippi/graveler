package graveler.action

import graveler.util.pointedAt
import net.minecraft.util.math.BlockPos

data class UpdateStress(val pos: BlockPos) : Action {
  override val weight: Double = 0.0

  override fun apply(context: ActionContext) {
    val world = context.world
    val point = world.pointedAt(pos)

    if (!point.isStressAware) {
      point.clearStress()
      return
    }

    if (!world.isAreaLoaded(pos, 1)) {
      return
    }

    val newStress = point.getNewStress()
    if (newStress >= 7) {
      context.scheduler.schedule(Fall(pos))
    }

    if (point.stress != newStress) {
      point.stress = newStress
      context.scheduler.schedule(UpdateNeighborStress(pos))
    }
  }
}
