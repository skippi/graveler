package graveler

import graveler.SchedulerProvider.Companion.scheduler
import graveler.util.InstanceProvider
import graveler.util.pointedAt
import graveler.util.stressMap
import net.minecraft.world.World
import net.minecraftforge.event.AttachCapabilitiesEvent
import net.minecraftforge.event.TickEvent
import net.minecraftforge.event.world.BlockEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

class PhysicsHandler {
  @SubscribeEvent
  fun onAttachWorldCapability(event: AttachCapabilitiesEvent<World>) {
    event.addCapability(SchedulerStorage.Resource, SchedulerProvider())
    event.addCapability(StressMapStorage.RESOURCE, InstanceProvider(StressMapStorage.CAPABILITY))
  }

  @SubscribeEvent
  fun onWorldTick(event: TickEvent.WorldTickEvent) {
    tickStress(event)

    if (event.phase != TickEvent.Phase.END) return

    val world = event.world
    world.scheduler?.tick(world)
  }

  @SubscribeEvent
  fun onBlockPlace(event: BlockEvent.EntityPlaceEvent) {
    val world = event.world.world
    val pointed = world.pointedAt(event.pos)

    world.stressMap?.set(pointed.pos, pointed.stress)
  }

  @SubscribeEvent
  fun onBlockBreak(event: BlockEvent.BreakEvent) {
    val world = event.world.world
    world.stressMap?.stresses?.remove(event.pos)
  }

  private fun tickStress(event: TickEvent.WorldTickEvent) {
    if (event.phase != TickEvent.Phase.END) return

    val stresses = event.world.stressMap?.stresses ?: return
    val poses = stresses.keys

    poses
      .map { event.world.pointedAt(it) }
      .forEach {
        val newStress = it.stress
        if (newStress >= 7) {
          event.world.scheduler?.schedule(Fall(it.pos))
        }

        stresses[it.pos] = newStress
      }
  }
}
