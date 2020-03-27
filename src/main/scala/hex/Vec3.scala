package hex

import scala.math._

case class Vec3(
    val x: Double,
    val y: Double,
    val z: Double
) {
  def +(that: Vec3): Vec3 = {
    Vec3(x + that.x, y + that.y, z + that.z)
  }

  def /(scalar: Double): Vec3 = {
    Vec3(x / scalar, y / scalar, z / scalar)
  }

  def -(that: Vec3): Vec3 = {
    this + (-that)
  }

  def dot(that: Vec3): Double = {
    x * that.x + y * that.y + z * that.z
  }

  def norm: Double = {
    sqrt(this dot this)
  }

  def unary_-(): Vec3 = {
    Vec3(-x, -y, -z)
  }
}
