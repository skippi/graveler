package graveler

import graveler.action.Action
import graveler.action.ActionContext
import java.util.*
import net.minecraft.world.World

class Scheduler(var processingRate: Int = 128) {
  private val queue = PriorityQueue<Action>(11, compareByDescending { it.priority })
  private var cooldown: Int = 0

  fun schedule(action: Action): Scheduler {
    queue.add(action)
    return this
  }

  fun tick(world: World): Scheduler {
    var count = 0
    val context = ActionContext(this, world)
    while (!isBusy && queue.isNotEmpty() && count < processingRate) {
      val action = queue.remove()
      action.apply(context)
      ++count
    }

    cooldown = (cooldown + 1) % 1

    return this
  }

  private val isBusy: Boolean
    get() = cooldown != 0
}
