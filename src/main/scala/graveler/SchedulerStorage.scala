package graveler

import net.minecraft.nbt.NBTBase
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.Capability.IStorage

class SchedulerStorage extends IStorage[Scheduler] {
  override def writeNBT(
      capability: Capability[Scheduler],
      instance: Scheduler,
      side: EnumFacing
  ): NBTBase = null

  @Override
  def readNBT(
      capability: Capability[Scheduler],
      instance: Scheduler,
      side: EnumFacing,
      nbt: NBTBase
  ): Unit = {
    // Do nothing
  }
}
