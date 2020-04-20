package graveler

import graveler.SchedulerProvider.Companion.scheduler
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.world.World
import net.minecraftforge.event.AttachCapabilitiesEvent
import net.minecraftforge.event.TickEvent
import net.minecraftforge.event.world.BlockEvent
import net.minecraftforge.event.world.ChunkEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

class PhysicsHandler {
  @SubscribeEvent
  fun onAttachWorldCapability(event: AttachCapabilitiesEvent<World>) {
    event.addCapability(SchedulerStorage.Resource, SchedulerProvider())
  }

  @SubscribeEvent
  fun onWorldTick(event: TickEvent.WorldTickEvent) {
    if (event.phase != TickEvent.Phase.END) {
      return
    }

    val world = event.world
    world.scheduler?.tick(world)
  }

  @SubscribeEvent
  fun onChunkUnload(event: ChunkEvent.Unload) {
    val world = event.world
    world.scheduler?.setPhysicsAt(event.chunk.pos, false)
  }

  @SubscribeEvent
  fun onBlockPlace(event: BlockEvent.EntityPlaceEvent) {
    if (event.entity !is PlayerEntity) {
      return
    }

    val world = event.world
    world.scheduler?.setPhysicsAt(event.pos, true)
  }

  @SubscribeEvent
  fun onHarvestDrops(event: BlockEvent.HarvestDropsEvent) {
    if (event.harvester !is PlayerEntity) {
      return
    }

    val world = event.world
    world.scheduler?.setPhysicsAt(event.pos, true)
  }

  @SubscribeEvent
  fun onBlockBreak(event: BlockEvent.BreakEvent) {
    val world = event.world
    world.scheduler?.setPhysicsAt(event.pos, true)
  }

  @SubscribeEvent
  fun onNeighborNotify(event: BlockEvent.NeighborNotifyEvent) {
    val world = event.world
    world.scheduler?.schedule(Gravity(event.pos))
  }
}
