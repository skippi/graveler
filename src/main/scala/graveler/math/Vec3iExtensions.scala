package graveler.math

import net.minecraft.util.math.Vec3i
import scala.math._

object Vec3iExtensions {
  implicit class ExtendedVec3i(private val vec: Vec3i) {
    def +(that: Vec3i): Vec3i = {
      new Vec3i(
        vec.getX + that.getX,
        vec.getY + that.getY,
        vec.getZ + that.getZ
      )
    }

    def /(scalar: Double): Vec3i = {
      new Vec3i(vec.getX / scalar, vec.getY / scalar, vec.getZ / scalar)
    }

    def -(that: Vec3i): Vec3i = this + (-that)

    def dot(that: Vec3i): Int = {
      val x = vec.getX * that.getX
      val y = vec.getY * that.getY
      val z = vec.getZ * that.getZ

      x + y + z
    }

    def norm: Double = sqrt((vec dot vec).toDouble)

    def unary_-(): Vec3i = new Vec3i(-vec.getX, -vec.getY, -vec.getZ)
  }
}
