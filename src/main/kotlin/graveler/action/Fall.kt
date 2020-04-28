package graveler.action

import graveler.util.fallAt
import net.minecraft.util.math.BlockPos

data class Fall(val pos: BlockPos) : Action {
  override val weight: Double = 1 / 32.0

  override fun apply(context: ActionContext) {
    context.world.fallAt(pos)
  }
}
