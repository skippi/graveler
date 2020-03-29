package graveler;

import static graveler.CapabilityUtil.*;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GravelerMod.MODID)
public class PhysicsHandler {
  public static final ResourceLocation PHYSICS_RES =
      new ResourceLocation(GravelerMod.MODID, "physics");

  @SubscribeEvent
  public static void onAttachWorldCapability(AttachCapabilitiesEvent<World> event) {
    event.addCapability(PHYSICS_RES, new SchedulerProvider());
  }

  @SubscribeEvent
  public static void onWorldTick(TickEvent.WorldTickEvent event) {
    if (event.phase != TickEvent.Phase.END) {
      return;
    }

    final World world = event.world;

    getSchedulerOption(world).ifPresent(s -> s.tick(world));
  }

  @SubscribeEvent
  public static void onChunkUnload(ChunkEvent.Unload event) {
    getSchedulerOption(event.getWorld())
        .ifPresent(s -> s.setPhysicsAt(event.getChunk().getPos(), false));
  }

  @SubscribeEvent
  public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
    if (!(event.getEntity() instanceof PlayerEntity)) {
      return;
    }

    getSchedulerOption(event.getWorld()).ifPresent(s -> s.setPhysicsAt(event.getPos(), true));
  }

  @SubscribeEvent
  public static void onHarvestDrops(BlockEvent.HarvestDropsEvent event) {
    if (!(event.getHarvester() instanceof PlayerEntity)) {
      return;
    }

    getSchedulerOption(event.getWorld()).ifPresent(s -> s.setPhysicsAt(event.getPos(), true));
  }

  @SubscribeEvent
  public static void onBlockBreak(BlockEvent.BreakEvent event) {
    getSchedulerOption(event.getWorld()).ifPresent(s -> s.setPhysicsAt(event.getPos(), true));
  }

  @SubscribeEvent
  public static void onNeighborNotify(BlockEvent.NeighborNotifyEvent event) {
    getSchedulerOption(event.getWorld()).ifPresent(s -> s.schedule(new Gravity(event.getPos())));
  }
}
