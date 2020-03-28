package hex.instances

import hex._
import hex.forge.Vec3Converters._
import net.minecraft.block.state.{IBlockState => ForgeBlockState}
import net.minecraft.world.{World => ForgeWorld}

trait WorldInstances {
  implicit val ForgeWorldIsWorld = new World[ForgeWorld, ForgeBlockState] {
    def getBlockState(a: ForgeWorld, pos: Vec3)(
        implicit ev: BlockState[ForgeBlockState, _, _]
    ): ForgeBlockState = {
      a.getBlockState(pos.toBlockPos)
    }

    def setBlockState(a: ForgeWorld, pos: Vec3, state: ForgeBlockState)(
        implicit ev: BlockState[ForgeBlockState, _, _]
    ): Unit = {
      a.setBlockState(pos.toBlockPos, state)
      ()
    }

    def isRemote(a: ForgeWorld): Boolean = a.isRemote
  }
}
