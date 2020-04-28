package graveler

import graveler.action.Action
import graveler.action.ActionContext
import net.minecraft.world.World

@OptIn(ExperimentalStdlibApi::class)
class Scheduler(private var processingRate: Double = 1.0) {
  private val queue = ArrayDeque<Action>()
  private var cooldown: Int = 0

  fun schedule(action: Action): Scheduler {
    queue.addLast(action)
    return this
  }

  fun tick(world: World): Scheduler {
    var count = 0.0
    val context = ActionContext(this, world)
    while (!isBusy && queue.isNotEmpty() && count < processingRate) {
      val action = queue.removeFirst()
      action.apply(context)
      count += action.weight
    }

    cooldown = (cooldown + 1) % 1

    println(queue.size)

    return this
  }

  private val isBusy: Boolean
    get() = cooldown != 0
}
