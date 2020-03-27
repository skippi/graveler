package hex.instances

import hex._
import net.minecraft.block.state.{IBlockState => ForgeBlockState}
import net.minecraft.world.{World => ForgeWorld}
import net.minecraft.util.math.BlockPos

trait WorldInstances {
  implicit val ForgeWorldIsWorld = new World[ForgeWorld, ForgeBlockState] {
    def getBlockState(a: ForgeWorld, pos: Vec3)(
        implicit ev: BlockState[ForgeBlockState, _]
    ): ForgeBlockState = {
      a.getBlockState(new BlockPos(pos.x, pos.y, pos.z))
    }

    def setBlockState(a: ForgeWorld, pos: Vec3, state: ForgeBlockState)(
        implicit ev: BlockState[ForgeBlockState, _]
    ): Unit = {
      a.setBlockState(new BlockPos(pos.x, pos.y, pos.z), state)
      ()
    }
  }
}
