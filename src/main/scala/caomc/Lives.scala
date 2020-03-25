package caomc

import scala.math._

class Lives {
  private var _count: Int = 10

  var curseTicks = 0

  def assign(other: Lives): Lives = {
    count = other.count
    curseTicks = other.curseTicks

    this
  }

  def count = _count

  def count_=(value: Int): Unit = {
    _count = max(0, value)
  } ensuring (_count >= 0)

  def deduct: Lives = {
    count -= 1

    if (!isAlive)
      curseTicks = -1
    else if (count <= 5) {
      val curseDurationMinutes = 3 + (5 - count)
      curseTicks = TickMath.minutesToTicks(curseDurationMinutes)
    }

    this
  }

  def isAlive: Boolean = count != 0

  def isCursed: Boolean = curseTicks != 0

  def isPermanentlyCursed: Boolean = curseTicks < 0

  def isTemporarilyCursed: Boolean = curseTicks > 0

  def damageReduction: Double = {
    if (count <= 5)
      0.5 + 0.1 * (5 - count)
    else 0.0
  }
}
