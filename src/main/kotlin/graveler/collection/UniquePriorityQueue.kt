package graveler.collection

import java.util.*

class UniquePriorityQueue<E>(initialCapacity: Int, comparator: Comparator<in E>) {
  val tracker = HashSet<E>()
  val queue = PriorityQueue(initialCapacity, comparator)

  fun remove(): E {
    val elem = queue.remove()
    tracker.remove(elem)

    return elem
  }

  fun add(elem: E): Boolean {
    if (tracker.contains(elem)) {
      return false
    }

    tracker.add(elem)
    queue.add(elem)

    return true
  }

  val isEmpty: Boolean
    get() = queue.isEmpty()

  val size: Int
    get() = queue.size
}
