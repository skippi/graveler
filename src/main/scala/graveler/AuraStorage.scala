package graveler

import net.minecraft.nbt.NBTBase
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.Capability.IStorage

class AuraStorage extends IStorage[Aura] {
  override def writeNBT(
      capability: Capability[Aura],
      instance: Aura,
      side: EnumFacing
  ): NBTBase = {
    val data = new NBTTagCompound
    data.setString("effect", instance.effect.name)
    data.setInteger("rollCount", instance.rollCount)
    data
  }

  @Override
  def readNBT(
      capability: Capability[Aura],
      instance: Aura,
      side: EnumFacing,
      nbt: NBTBase
  ): Unit = nbt match {
    case data: NBTTagCompound =>
      instance.effect = Aura.Effect.parse(data.getString("effect"))
      instance.rollCount = data.getInteger("rollCount")
    case _ =>
  }
}
