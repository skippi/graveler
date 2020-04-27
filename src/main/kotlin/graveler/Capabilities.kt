package graveler

import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.CapabilityInject

object Capabilities {
  @CapabilityInject(Scheduler::class)
  lateinit var SCHEDULER: Capability<Scheduler>

  @CapabilityInject(StressMap::class)
  lateinit var STRESS_MAP: Capability<StressMap>
}
