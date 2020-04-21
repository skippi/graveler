package graveler

import net.minecraft.util.EnumFacing
import net.minecraft.world.World
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.CapabilityInject
import net.minecraftforge.common.capabilities.ICapabilityProvider

class SchedulerProvider : ICapabilityProvider {
  private val instance: Scheduler? = Capability.defaultInstance

  override fun <T> getCapability(
    cap: Capability<T>,
    side: EnumFacing?
  ): T? {
    return if (cap == Capability) Capability.cast<T>(instance) else null
  }

  override fun hasCapability(cap: Capability<*>, side: EnumFacing?): Boolean {
    return cap == Capability
  }

  companion object {
    @CapabilityInject(Scheduler::class)
    lateinit var Capability: Capability<Scheduler?>

    val World.scheduler: Scheduler? get() = getCapability(Capability, null)
  }
}
