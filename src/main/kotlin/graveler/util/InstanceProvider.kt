package graveler.util

import net.minecraft.nbt.INBT
import net.minecraft.util.Direction
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.ICapabilitySerializable
import net.minecraftforge.common.util.LazyOptional

class InstanceProvider<T>(
  private val capability: Capability<T>
) : ICapabilitySerializable<INBT> {
  private val instance: T? = capability.defaultInstance

  override fun <T> getCapability(
    cap: Capability<T>,
    side: Direction?
  ): LazyOptional<T> {
    return capability.orEmpty(cap, instance.toLazyOptional)
  }

  override fun serializeNBT(): INBT {
    return capability.storage.writeNBT(capability, instance, null)!!
  }

  override fun deserializeNBT(nbt: INBT) {
    val instance = instance ?: return
    capability.storage.readNBT(capability, instance, null, nbt)
  }
}
