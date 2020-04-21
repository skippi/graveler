package graveler

import net.minecraft.nbt.NBTBase
import net.minecraft.util.EnumFacing
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.Capability.IStorage

class SchedulerStorage : IStorage<Scheduler> {
  override fun writeNBT(
    capability: Capability<Scheduler>,
    instance: Scheduler,
    side: EnumFacing
  ): NBTBase? = null

  override fun readNBT(
    capability: Capability<Scheduler>,
    instance: Scheduler,
    side: EnumFacing,
    nbt: NBTBase
  ) {
    // Do nothing
  }

  companion object {
    val Resource = ResourceLocation(GravelerMod.ModId, "physics")
  }
}
