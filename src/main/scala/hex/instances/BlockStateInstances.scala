package hex.instances

import hex._
import hex.forge.Vec3Converters._
import net.minecraft.block.material.{Material => ForgeMaterial}
import net.minecraft.block.state.{IBlockState => ForgeBlockState}
import net.minecraft.world.{World => ForgeWorld}

trait BlockStateInstances {
  implicit val ForgeBlockStateIsBlock
      : BlockState[ForgeBlockState, ForgeMaterial, ForgeWorld] =
    new BlockState[ForgeBlockState, ForgeMaterial, ForgeWorld] {
      def hardness(a: ForgeBlockState, world: ForgeWorld, pos: Vec3)(
          implicit ev: World[ForgeWorld, ForgeBlockState]
      ): Float = {
        a.getBlockHardness(world, pos.toBlockPos)
      }

      def material(a: ForgeBlockState)(
          implicit ev: Material[ForgeMaterial]
      ): ForgeMaterial = {
        a.getMaterial
      }
    }
}
