package caomc.math

import Vec3iExtensions._
import net.minecraft.util.math.Vec3i

case class BoundedArea(a: Vec3i, b: Vec3i) {
  def translatedBy(displacement: Vec3i): BoundedArea = {
    BoundedArea(a + displacement, b + displacement)
  }

  def centeredAt(pos: Vec3i): BoundedArea = translatedBy(pos - center)

  def center: Vec3i = (a + b) / 2

  def points: Seq[Vec3i] =
    for {
      i <- a.getX to b.getX
      j <- a.getY to b.getY
      k <- a.getZ to b.getZ
    } yield new Vec3i(i, j, k)
}

object BoundedArea {
  def cube(center: Vec3i, radius: Double): BoundedArea = {
    val sideLength = 2 * radius
    val displacement = new Vec3i(sideLength, sideLength, sideLength)

    BoundedArea(-displacement, displacement)
      .centeredAt(center)
  }
}
