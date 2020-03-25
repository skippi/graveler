package caomc

import caomc.CapabilityExtensions._
import caomc.physics._
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.event.AttachCapabilitiesEvent
import net.minecraftforge.event.world.BlockEvent
import net.minecraftforge.event.world.ChunkEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@Mod.EventBusSubscriber(modid = CaoMC.ModId)
object PhysicsHandler {
  final val PhysicsRes = new ResourceLocation(CaoMC.ModId, "physics")

  @SubscribeEvent
  def onAttachWorldCapability(event: AttachCapabilitiesEvent[World]): Unit = {
    event.addCapability(PhysicsRes, new SchedulerProvider)
  }

  @SubscribeEvent
  def onWorldTick(event: TickEvent.WorldTickEvent): Unit = {
    if (event.phase != TickEvent.Phase.END) return

    val world = event.world

    world.getSchedulerOption foreach { _.tick(world) }
  }

  @SubscribeEvent
  def onChunkUnload(event: ChunkEvent.Unload): Unit = {
    event.getWorld.getSchedulerOption
      .foreach(_.setPhysicsAt(event.getChunk.getPos, false))
  }

  @SubscribeEvent
  def onBlockPlace(event: BlockEvent.EntityPlaceEvent): Unit = {
    event.getEntity match {
      case _: EntityPlayer =>
        event.getWorld.getSchedulerOption
          .foreach(_.setPhysicsAt(event.getPos, true))
      case _ =>
    }
  }

  @SubscribeEvent
  def onHarvestDrops(event: BlockEvent.HarvestDropsEvent): Unit = {
    event.getHarvester match {
      case _: EntityPlayer =>
        event.getWorld.getSchedulerOption
          .foreach(_.setPhysicsAt(event.getPos, true))
      case _ =>
    }
  }

  @SubscribeEvent
  def onBlockBreak(event: BlockEvent.BreakEvent): Unit = {
    event.getWorld.getSchedulerOption
      .foreach(_.setPhysicsAt(event.getPos, true))
  }

  @SubscribeEvent
  def onNeighborNotify(event: BlockEvent.NeighborNotifyEvent): Unit = {
    val world = event.getWorld
    val pos = event.getPos

    world.getSchedulerOption foreach { _.schedule(Gravity(pos)) }
  }
}
