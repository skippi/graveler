package graveler.collection

import scala.collection.mutable.{HashSet, PriorityQueue}
import scala.math._

class UniquePriorityQueue[A](implicit ord: Ordering[A]) {
  private val uniqueTracker = new HashSet[A]
  private val queue = PriorityQueue[A]()(ord)

  def dequeue: A = {
    val elem = queue.dequeue
    uniqueTracker -= elem
    elem
  }

  def enqueue(elem: A): UniquePriorityQueue[A] = {
    if (!uniqueTracker.contains(elem)) {
      uniqueTracker += elem
      queue.enqueue(elem)
    }

    this
  }

  def nonEmpty: Boolean = queue.nonEmpty

  def size: Int = queue.size

  def take(n: Int): Seq[A] = {
    (1 to min(size, n)) map { _ =>
      dequeue
    }
  }
}
