package graveler.action

import graveler.util.pointedAt
import graveler.util.scheduler
import graveler.util.stressMap
import net.minecraft.util.math.BlockPos

data class UpdateStress(val pos: BlockPos) : Action {
  override val weight: Int = 1

  override fun apply(context: ActionContext) {
    val world = context.world
    val point = world.pointedAt(pos)

    val stresses = world.stressMap?.stresses ?: return
    if (!point.isStressAware) {
      stresses.remove(pos)
      return
    }

    if (!world.isAreaLoaded(pos, 1)) {
      return
    }

    val newStress = point.stress
    if (newStress >= 7) {
      context.scheduler.schedule(Fall(pos))
    }

    if (stresses[pos] != newStress) {
      stresses[pos] = newStress
      context.scheduler.schedule(UpdateNeighborStress(pos))
    }
  }
}
