package caomc

import caomc.CapabilityExtensions._
import net.minecraft.entity.Entity
import net.minecraft.entity.monster.EntityMob
import net.minecraft.util.ResourceLocation
import net.minecraftforge.event.AttachCapabilitiesEvent
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.event.entity.living.LivingEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@Mod.EventBusSubscriber(modid = CaoMC.ModId)
object AuraHandler {
  final val AuraRes = new ResourceLocation(CaoMC.ModId, "aura")

  @SubscribeEvent
  def attachAura(event: AttachCapabilitiesEvent[Entity]): Unit = {
    event.addCapability(AuraRes, new AuraProvider)
  }

  @SubscribeEvent
  def onEntityJoinWorldEvent(event: EntityJoinWorldEvent): Unit =
    event.getEntity match {
      case mob: EntityMob =>
        mob.getAuraOption
          .filter { _.rollCount == 0 }
          .foreach { _.roll }
      case _ =>
    }

  @SubscribeEvent
  def onLivingUpdate(event: LivingEvent.LivingUpdateEvent): Unit = {
    val entity = event.getEntityLiving

    entity.getAuraOption foreach { _.tick(entity) }
  }
}
