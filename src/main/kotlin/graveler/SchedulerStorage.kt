package graveler

import net.minecraft.nbt.INBT
import net.minecraft.util.Direction
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.Capability.IStorage

class SchedulerStorage : IStorage<Scheduler> {
  override fun writeNBT(
    capability: Capability<Scheduler>,
    instance: Scheduler,
    side: Direction
  ): INBT? = null

  override fun readNBT(
    capability: Capability<Scheduler>,
    instance: Scheduler,
    side: Direction,
    nbt: INBT
  ) {
    // Do nothing
  }
}
