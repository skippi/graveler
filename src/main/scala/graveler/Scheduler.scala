package graveler

import graveler.PhysicsUtil._
import graveler.collection.UniquePriorityQueue
import java.lang.Math._
import java.util.HashSet
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.World

class Scheduler(private val processingRate: Int) {
  private val allowedChunks = new HashSet[ChunkPos]()
  private val queue =
    new UniquePriorityQueue[Action](11, _.priority compare _.priority)
  private var cooldown: Int = 0

  private def canActAt(pos: BlockPos): Boolean = {
    allowedChunks.contains(new ChunkPos(pos))
  }

  private def canPerformAction: Boolean = {
    !isBusy && hasPendingActions
  }

  private def isBusy: Boolean = {
    cooldown != 0
  }

  private def hasPendingActions: Boolean = {
    !queue.isEmpty
  }

  def schedule(action: Action): Scheduler = {
    queue.add(action)
    this
  }

  def setPhysicsAt(pos: BlockPos, state: Boolean): Scheduler = {
    setPhysicsAt(new ChunkPos(pos), state)
  }

  def setPhysicsAt(pos: ChunkPos, state: Boolean): Scheduler = {
    if (state)
      allowedChunks.add(pos)
    else
      allowedChunks.remove(pos)

    this
  }

  def tick(world: World): Scheduler = {
    if (canPerformAction) {
      (1 to min(queue.size, processingRate))
        .map { _ =>
          queue.remove
        }
        .foreach {
          case Fall(pos)                     => fallAt(world, pos)
          case Gravity(pos) if canActAt(pos) => triggerGravityAt(world, pos)
          case _                             =>
        }
    }

    cooldown = (cooldown + 1) % 1

    this
  }
}

object Scheduler {
  final val DefaultProcessingRate = 32

  def apply: Scheduler = {
    new Scheduler(DefaultProcessingRate)
  }
}
