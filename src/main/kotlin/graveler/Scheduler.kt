package graveler

import graveler.PhysicsUtil.*
import graveler.collection.UniquePriorityQueue
import java.lang.Math.*
import java.util.HashSet
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.World

class Scheduler(private val processingRate: Int) {
  companion object {
    final val DefaultProcessingRate = 32

    val withDefaults: Scheduler
      get() = Scheduler(DefaultProcessingRate)
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
    get() = !queue.isEmpty()

  fun schedule(action: Action): Scheduler {
    queue.add(action)
    return this
  }

  fun setPhysicsAt(pos: BlockPos, state: Boolean): Scheduler {
    return setPhysicsAt(ChunkPos(pos), state)
  }

  fun setPhysicsAt(pos: ChunkPos, state: Boolean): Scheduler {
    if (state)
      allowedChunks.add(pos)
    else
      allowedChunks.remove(pos)

    return this
  }

  fun tick(world: World): Scheduler {
    if (canPerformAction) {
      (1..min(queue.size(), processingRate))
        .map { _ -> queue.remove() }
        .forEach {
          when (it) {
            is Fall -> fallAt(world, it.pos)
            is Gravity -> if (canActAt(it.pos)) {
              triggerGravityAt(world, it.pos)
            }
          }
        }
    }

    cooldown = (cooldown + 1) % 1

    return this
  }
}
