package graveler

import graveler.action.Action
import graveler.action.ActionContext
import net.minecraft.world.World

@OptIn(ExperimentalStdlibApi::class)
class Scheduler(private var processingRate: Double = 1.0) {
  private val queue = ArrayDeque<Action>()

  fun schedule(action: Action): Scheduler {
    queue.addLast(action)
    return this
  }

  fun tick(world: World): Scheduler {
    var count = 0.0
    val context = ActionContext(this, world)
    while (queue.isNotEmpty() && count < processingRate) {
      val action = queue.removeFirst()
      action.apply(context)
      count += action.weight
    }

    return this
  }
}
