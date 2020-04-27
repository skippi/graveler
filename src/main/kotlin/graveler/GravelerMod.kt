package graveler

import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.capabilities.CapabilityManager
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext

@Mod(GravelerMod.ModId)
class GravelerMod {
  private fun init(event: FMLCommonSetupEvent) {
    CapabilityManager.INSTANCE.register(
      Scheduler::class.java, SchedulerStorage()) { Scheduler.withDefaults() }

    CapabilityManager.INSTANCE.register(StressMap::class.java, StressMapStorage()) { StressMap(mutableMapOf()) }
  }

  companion object {
    const val ModId = "graveler"
  }

  init {
    val modEventBus = FMLJavaModLoadingContext.get().modEventBus
    modEventBus.addListener { event: FMLCommonSetupEvent -> init(event) }
    MinecraftForge.EVENT_BUS.let {
      it.register(this)
      it.register(PhysicsHandler())
    }
  }
}
