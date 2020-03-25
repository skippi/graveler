package graveler.math

import Vec3iExtensions._
import net.minecraft.util.math.Vec3i

case class Bounds(val center: Vec3i, val size: Vec3i) {
  require(size.getX >= 0)
  require(size.getY >= 0)
  require(size.getZ >= 0)

  private def extent: Vec3i = size / 2

  def max: Vec3i = center + extent

  def min: Vec3i = center - extent

  def points: Seq[Vec3i] = {
    for {
      i <- min.getX to max.getX
      j <- min.getY to max.getY
      k <- min.getZ to max.getZ
    } yield new Vec3i(i, j, k)
  }
}

object Bounds {
  def apply(center: Vec3i, size: Vec3i) = new Bounds(center, size)
}
