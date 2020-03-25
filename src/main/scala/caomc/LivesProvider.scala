package caomc

import caomc.forge.ScalaAnnotations._
import net.minecraft.nbt.NBTBase
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.ICapabilitySerializable

class LivesProvider extends ICapabilitySerializable[NBTBase] {
  private val Instance = LivesProvider.LivesCap.getDefaultInstance

  override def hasCapability(
      capability: Capability[_],
      side: EnumFacing
  ): Boolean = {
    capability == LivesProvider.LivesCap
  }

  override def getCapability[T](
      capability: Capability[T],
      side: EnumFacing
  ): T = {
    if (capability == LivesProvider.LivesCap)
      LivesProvider.LivesCap.cast[T](Instance)
    else null.asInstanceOf[T]
  }

  override def serializeNBT: NBTBase = {
    LivesProvider.LivesCap.getStorage
      .writeNBT(LivesProvider.LivesCap, Instance, null)
  }

  override def deserializeNBT(nbt: NBTBase): Unit = {
    LivesProvider.LivesCap.getStorage
      .readNBT(LivesProvider.LivesCap, this.Instance, null, nbt)
  }
}

object LivesProvider {
  @capabilityInject(classOf[Lives])
  final var LivesCap: Capability[Lives] = null
}
