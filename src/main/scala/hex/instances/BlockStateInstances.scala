package hex.instances

import hex._
import net.minecraft.block.material.{Material => ForgeMaterial}
import net.minecraft.block.state.{IBlockState => ForgeBlockState}

trait BlockStateInstances {
  implicit val ForgeBlockStateIsBlock =
    new BlockState[ForgeBlockState, ForgeMaterial] {
      def material(
          a: ForgeBlockState
      )(implicit ev: Material[ForgeMaterial]): ForgeMaterial = {
        a.getMaterial
      }
    }
}
