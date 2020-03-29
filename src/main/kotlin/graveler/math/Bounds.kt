package graveler.math

import net.minecraft.util.math.Vec3i

data class Bounds(
  val center: Vec3i,
  val size: Vec3i
) {
  val extent: Vec3i
    get() = size / 2.0

  val max: Vec3i
    get() = center + extent

  val min: Vec3i
    get() = center - extent
}
