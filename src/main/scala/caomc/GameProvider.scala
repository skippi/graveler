package caomc

import caomc.forge.ScalaAnnotations._
import net.minecraft.nbt.NBTBase
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.ICapabilitySerializable

class GameProvider extends ICapabilitySerializable[NBTBase] {
  private val Instance = GameProvider.GameCap.getDefaultInstance

  override def hasCapability(
      capability: Capability[_],
      side: EnumFacing
  ): Boolean = {
    capability == GameProvider.GameCap
  }

  override def getCapability[T](
      capability: Capability[T],
      side: EnumFacing
  ): T = {
    if (capability == GameProvider.GameCap)
      GameProvider.GameCap.cast[T](this.Instance)
    else null.asInstanceOf[T]
  }

  override def serializeNBT: NBTBase = {
    GameProvider.GameCap.getStorage
      .writeNBT(GameProvider.GameCap, this.Instance, null)
  }

  override def deserializeNBT(nbt: NBTBase): Unit = {
    GameProvider.GameCap.getStorage
      .readNBT(GameProvider.GameCap, this.Instance, null, nbt)
  }
}

object GameProvider {
  @capabilityInject(classOf[Game])
  final var GameCap: Capability[Game] = null
}
