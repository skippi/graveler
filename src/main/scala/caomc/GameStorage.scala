package caomc

import caomc.NBTExtensions._
import net.minecraft.nbt.NBTBase
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.Capability.IStorage

class GameStorage extends IStorage[Game] {
  override def writeNBT(
      capability: Capability[Game],
      instance: Game,
      side: EnumFacing
  ): NBTBase = {
    val data = new NBTTagCompound
    data.setLong("aliveTicks", instance.ticks)
    data.setBoolean("active", instance.isActive)
    data.setTag("participants", instance.participants.toTagList)

    data
  }

  @Override
  def readNBT(
      capability: Capability[Game],
      instance: Game,
      side: EnumFacing,
      nbt: NBTBase
  ): Unit = {
    nbt match {
      case data: NBTTagCompound => {
        instance.ticks = data.getLong("aliveTicks")
        instance.isActive = data.getBoolean("active")
        instance.participants ++= data.getTagList("participants", 8).toStringSet
      }
      case _ =>
    }

    ()
  }
}
