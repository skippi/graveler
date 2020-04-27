package graveler.action

import graveler.Scheduler
import net.minecraft.world.World

data class ActionContext(
  val scheduler: Scheduler,
  val world: World
)
