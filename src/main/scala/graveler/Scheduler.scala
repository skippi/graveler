package graveler

import PhysicsExtensions._
import graveler.collection.UniquePriorityQueue
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.World
import scala.collection.mutable.HashSet

class Scheduler(private val processingRate: Int) {
  private val allowedChunks = HashSet[ChunkPos]()
  private var cooldown: Int = 0
  private val queue = new UniquePriorityQueue[Action]()(
    Ordering.by[Action, Int](_.priority).reverse
  )

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
    queue.nonEmpty
  }

  def schedule(action: Action): Scheduler = {
    queue.enqueue(action)
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
      queue.take(processingRate) foreach {
        case Fall(pos)                     => world.fallAt(pos)
        case Gravity(pos) if canActAt(pos) => world.triggerGravityAt(pos)
        case _                             =>
      }
    }

    cooldown = (cooldown + 1) % 1

    this
  }
}

object Scheduler {
  final val DefaultProcessingRate = 32

  def apply(): Scheduler = {
    new Scheduler(DefaultProcessingRate)
  }
}
