package caomc

object TickMath {
  def ticksToSeconds(ticks: Int): Int = ticks / 20
  def ticksToSeconds(ticks: Long): Long = ticks / 20
  def secondsToTicks(seconds: Int): Int = seconds * 20
  def secondsToTicks(seconds: Long): Long = seconds * 20
  def minutesToTicks(minutes: Int): Int = minutes * 1200
}
