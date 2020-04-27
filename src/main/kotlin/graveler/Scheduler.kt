package graveler

import graveler.action.Action
import graveler.action.ActionContext
import kotlin.collections.ArrayDeque
import net.minecraft.world.World

@OptIn(ExperimentalStdlibApi::class)
class Scheduler(var processingRate: Int = 128) {

  private val queue = ArrayDeque<Action>()
  private var cooldown: Int = 0

  fun schedule(action: Action): Scheduler {
    queue.addLast(action)
    return this
  }

  fun tick(world: World): Scheduler {
    var count = 0
    val context = ActionContext(this, world)
    while (!isBusy && queue.isNotEmpty() && count < processingRate) {
      val action = queue.removeFirst()
      action.apply(context)
      count += action.weight
    }

    cooldown = (cooldown + 1) % 1

    return this
  }

  private val isBusy: Boolean
    get() = cooldown != 0
}
