package graveler

import graveler.forge.ScalaAnnotations._
import net.minecraft.nbt.NBTBase
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.ICapabilitySerializable

class AuraProvider extends ICapabilitySerializable[NBTBase] {
  private val Instance = AuraProvider.AuraCap.getDefaultInstance

  override def hasCapability(
      capability: Capability[_],
      side: EnumFacing
  ): Boolean = {
    capability == AuraProvider.AuraCap
  }

  override def getCapability[T](
      capability: Capability[T],
      side: EnumFacing
  ): T = {
    if (capability == AuraProvider.AuraCap)
      AuraProvider.AuraCap.cast[T](Instance)
    else null.asInstanceOf[T]
  }

  override def serializeNBT: NBTBase = {
    AuraProvider.AuraCap.getStorage
      .writeNBT(AuraProvider.AuraCap, Instance, null)
  }

  override def deserializeNBT(nbt: NBTBase): Unit = {
    AuraProvider.AuraCap.getStorage
      .readNBT(AuraProvider.AuraCap, this.Instance, null, nbt)
  }
}

object AuraProvider {
  @capabilityInject(classOf[Aura])
  final var AuraCap: Capability[Aura] = null
}
