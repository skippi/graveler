package graveler

import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.capabilities.CapabilityManager
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent

@Mod(
  modid = GravelerMod.ModId,
  name = GravelerMod.Name,
  version = GravelerMod.Version
)
class GravelerMod {
  @Mod.EventHandler
  fun init(event: FMLInitializationEvent) {
    CapabilityManager.INSTANCE.register(
      Scheduler::class.java, SchedulerStorage()) { Scheduler.withDefaults() }
  }

  companion object {
    const val ModId = "graveler"
    const val Name = "Graveler"
    const val Version = "0.1"
  }

  init {
    MinecraftForge.EVENT_BUS.let {
      it.register(this)
      it.register(PhysicsHandler())
    }
  }
}
