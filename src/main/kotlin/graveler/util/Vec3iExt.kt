package graveler.util

import net.minecraft.util.math.Vec3i

operator fun Vec3i.unaryMinus(): Vec3i {
  return Vec3i(-x, -y, -z)
}

operator fun Vec3i.plus(b: Vec3i): Vec3i {
  return Vec3i(x + b.x, y + b.y, z + b.z)
}

operator fun Vec3i.minus(b: Vec3i): Vec3i {
  return this + (-b)
}

operator fun Vec3i.div(scalar: Double): Vec3i {
  return Vec3i(x / scalar, y / scalar, z / scalar)
}
