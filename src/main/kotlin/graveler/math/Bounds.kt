package graveler.math

import graveler.math.Vec3Util.*
import net.minecraft.util.math.Vec3i

data class Bounds(
  val center: Vec3i,
  val size: Vec3i
) {
  val extent: Vec3i
    get() = divide(size, 2.0)

  val max: Vec3i
    get() = add(center, extent)

  val min: Vec3i
    get() = subtract(center, extent)
}
