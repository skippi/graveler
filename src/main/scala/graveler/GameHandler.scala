package graveler

import graveler.CapabilityExtensions._
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.event.AttachCapabilitiesEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

@Mod.EventBusSubscriber(modid = Graveler.ModId)
object GameHandler {
  final val GameRes = new ResourceLocation(Graveler.ModId, "game")

  @SubscribeEvent
  def attachGame(event: AttachCapabilitiesEvent[World]): Unit = {
    event.addCapability(GameRes, new GameProvider)
  }

  @SubscribeEvent
  def onWorldTick(event: TickEvent.WorldTickEvent): Unit = {
    if (event.phase != TickEvent.Phase.END) return

    event.world.getGameOption foreach { _.tick(event.world) }
  }

  @SubscribeEvent
  def onPlayerLoggedIn(event: PlayerLoggedInEvent): Unit = {
    val player = event.player
    val world = player.getEntityWorld

    world.getGameOption foreach { _.participants += player.getName }
  }
}
