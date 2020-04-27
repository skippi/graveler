package graveler

import graveler.collection.UniquePriorityQueue
import graveler.util.fallAt
import java.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.World

class Scheduler(private val processingRate: Int) {
  companion object {
    private const val DefaultProcessingRate = 64

    fun withDefaults(): Scheduler {
      return Scheduler(DefaultProcessingRate)
    }
  }

  private val allowedChunks = HashSet<ChunkPos>()
  private val queue = UniquePriorityQueue<Action>(11, compareByDescending({ it.priority }))
  private var cooldown: Int = 0

  private fun canActAt(pos: BlockPos): Boolean {
    return allowedChunks.contains(ChunkPos(pos))
  }

  val canPerformAction: Boolean
    get() = !isBusy && hasPendingActions

  val isBusy: Boolean
    get() = cooldown != 0

  val hasPendingActions: Boolean
    get() = !queue.isEmpty

  fun schedule(action: Action): Scheduler {
    queue.add(action)
    return this
  }

  fun tick(world: World): Scheduler {
    if (canPerformAction) {
      (1..queue.size.coerceAtMost(processingRate))
        .map { queue.remove() }
        .forEach {
          when (it) {
            is Fall -> world.fallAt(it.pos)
            else -> {}
          }
        }
    }

    cooldown = (cooldown + 1) % 1

    return this
  }
}
