package graveler

import graveler.util.InstanceProvider
import graveler.util.scheduler
import net.minecraft.world.World
import net.minecraft.world.chunk.Chunk
import net.minecraftforge.event.AttachCapabilitiesEvent
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

class PhysicsHandler {
  @SubscribeEvent
  fun onAttachWorldCapability(event: AttachCapabilitiesEvent<World>) {
    event.addCapability(SchedulerStorage.Resource, InstanceProvider(Capabilities.SCHEDULER))
  }

  @SubscribeEvent
  fun onAttachChunkCapability(event: AttachCapabilitiesEvent<Chunk>) {
    event.addCapability(StressMapStorage.RESOURCE, InstanceProvider(Capabilities.STRESS_MAP))
  }

  @SubscribeEvent
  fun onWorldTick(event: TickEvent.WorldTickEvent) {
    if (event.phase != TickEvent.Phase.END) return

    val world = event.world
    world.scheduler?.tick(world)
  }
}
