package graveler.collection;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;

public class UniquePriorityQueue<E> {
  private HashSet<E> tracker;
  private PriorityQueue<E> queue;

  public UniquePriorityQueue(int initialCapacity, Comparator<? super E> comparator) {
    tracker = new HashSet<E>();
    queue = new PriorityQueue<E>(initialCapacity, comparator);
  }

  public E remove() {
    final E element = queue.remove();
    tracker.remove(element);

    return element;
  }

  public boolean add(final E elem) {
    if (tracker.contains(elem)) {
      return false;
    }

    tracker.add(elem);
    queue.add(elem);

    return true;
  }

  public boolean isEmpty() {
    return queue.isEmpty();
  }

  public int size() {
    return queue.size();
  }
}
