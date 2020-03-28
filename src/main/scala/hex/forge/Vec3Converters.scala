package hex.forge

import hex._
import net.minecraft.util.math.BlockPos

object Vec3Converters {
  implicit class ExtendedVec3(vec: Vec3) {
    def toBlockPos: BlockPos = {
      new BlockPos(vec.x, vec.y, vec.z)
    }
  }
}
