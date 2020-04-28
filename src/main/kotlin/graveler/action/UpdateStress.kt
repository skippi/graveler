package graveler.action

import graveler.util.PointedWorld
import graveler.util.pointedAt
import graveler.util.stressMap
import net.minecraft.block.Blocks
import net.minecraft.util.Direction
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

  private fun PointedWorld.getNewStress(): Int {
    if (!isStressAware) return 0

    val below = move(Direction.DOWN)
    val horizontalStress = Direction.Plane.HORIZONTAL
      .map { move(it).stress + 1 }

    return (horizontalStress + below.stress).min()!!
  }

  private fun PointedWorld.clearStress() {
    chunk?.stressMap?.stresses?.remove(pos)
  }

  private var PointedWorld.stress: Int
    get() = when {
      isStressAware -> chunk?.stressMap?.get(pos) ?: 0
      isPermanentlyStable -> 0
      else -> MAX_STRESS
    }
    set(value) {
      when {
        isStressAware -> chunk?.stressMap?.set(pos, value)
      }
    }

  private val PointedWorld.isStressAware: Boolean
    get() = !blockState.isAir(world, pos) && !isPermanentlyStable

  private val PointedWorld.isPermanentlyStable: Boolean
    get() = block == Blocks.BEDROCK || material.isLiquid

  companion object {
    private const val MAX_STRESS = 50
  }
}
