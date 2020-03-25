package graveler

import graveler.CapabilityExtensions._
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ResourceLocation
import net.minecraftforge.event.AttachCapabilitiesEvent
import net.minecraftforge.event.entity.living.LivingAttackEvent
import net.minecraftforge.event.entity.living.LivingHurtEvent
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

@Mod.EventBusSubscriber(modid = Graveler.ModId)
final object LivesHandler {
  final val LivesRes = new ResourceLocation(Graveler.ModId, "lives")

  @SubscribeEvent
  def onAttachEntityCapability(event: AttachCapabilitiesEvent[Entity]): Unit = {
    event.getObject match {
      case _: EntityPlayer => event.addCapability(LivesRes, new LivesProvider)
      case _               =>
    }
  }

  @SubscribeEvent
  def onPlayerLoggedInEvent(event: PlayerLoggedInEvent): Unit = {
    event.player.getLivesOption
      .foreach(lives => notifyLivesStatus(event.player, lives))
  }

  def notifyLivesStatus(player: EntityPlayer, lives: Lives): Unit = {
    player.sendMessage(CaoLocale.createLivesStatusText(lives))
  }

  @SubscribeEvent
  def onPlayerCloneEvent(event: PlayerEvent.Clone): Unit = {
    val oldPlayer = event.getOriginal
    val newPlayer = event.getEntityPlayer

    (oldPlayer.getLivesOption zip newPlayer.getLivesOption) foreach {
      case (oldLives, newLives) =>
        newLives.assign(oldLives)
        newLives.deduct
        newPlayer.sendMessage(CaoLocale.createLivesRespawnText(newLives))
    }
  }

  @SubscribeEvent
  def onLivingHurt(event: LivingHurtEvent): Unit = {
    val sourceEntity = event.getSource.getTrueSource
    sourceEntity.getLivesOption
      .filter(_.isTemporarilyCursed)
      .foreach(lives => {
        val damageRatio = (1 - lives.damageReduction)
        event.setAmount(event.getAmount * damageRatio.toFloat)
      })
  }

  @SubscribeEvent
  def onLivingAttack(event: LivingAttackEvent): Unit = {
    val sourceEntity = event.getSource.getTrueSource
    sourceEntity.getLivesOption
      .filter(_.isPermanentlyCursed)
      .foreach(_ => event.setCanceled(true))
  }

  @SubscribeEvent
  def onPlayerTickEvent(event: TickEvent.PlayerTickEvent): Unit = {
    if (event.phase != TickEvent.Phase.END) return

    event.player.getLivesOption
      .filter(_.isTemporarilyCursed)
      .foreach(_.curseTicks -= 1)
  }
}
