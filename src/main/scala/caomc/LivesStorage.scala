package caomc

import net.minecraft.nbt.NBTBase
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.Capability.IStorage

class LivesStorage extends IStorage[Lives] {
  override def writeNBT(
      capability: Capability[Lives],
      instance: Lives,
      side: EnumFacing
  ): NBTBase = {
    val data = new NBTTagCompound
    data.setInteger("lives", instance.count)
    data.setInteger("curseRemainingTicks", instance.curseTicks)
    data
  }

  @Override
  def readNBT(
      capability: Capability[Lives],
      instance: Lives,
      side: EnumFacing,
      nbt: NBTBase
  ): Unit = nbt match {
    case data: NBTTagCompound =>
      instance.count = data.getInteger("lives")
      instance.curseTicks = data.getInteger("curseRemainingTicks")
    case _ =>
  }
}
