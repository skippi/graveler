package graveler

import java.util.UUID
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.entity.ai.attributes.IAttributeInstance
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.event.entity.living.LivingEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@Mod.EventBusSubscriber(modid = Graveler.ModId)
object WaterSpeedHandler {
  final val MobLandSpeedBoost = (
    new AttributeModifier(
      UUID.fromString("078e4fc9-8fd4-425e-b2a7-7b958a1b7dbe"),
      "Land speed boost",
      0.3,
      2
    )
  ).setSaved(false)

  final val MobWaterSpeedBoost =
    (new AttributeModifier(
      UUID.fromString("eba2f38b-4a27-42e7-9a2f-0ae1fdbbd4a8"),
      "Water speed boost",
      3,
      2
    )).setSaved(false)
  final val MobWaterSwimBoost =
    (new AttributeModifier(
      UUID.fromString("45d116dd-f203-4cbb-9065-4de7440ae653"),
      "Water swim boost",
      2,
      2
    )).setSaved(false)

  @SubscribeEvent
  def onLivingUpdate(event: LivingEvent.LivingUpdateEvent): Unit = {
    event.getEntityLiving match {
      case _: EntityPlayer =>
      case entity =>
        val swimSpeedAttribute =
          entity.getEntityAttribute(EntityLivingBase.SWIM_SPEED)
        val movementSpeedAttribute =
          entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED)

        if (entity.isInWater) {
          applyModifier(movementSpeedAttribute, MobWaterSpeedBoost)
          applyModifier(swimSpeedAttribute, MobWaterSwimBoost)
          removeModifier(movementSpeedAttribute, MobLandSpeedBoost)
        } else {
          removeModifier(movementSpeedAttribute, MobWaterSpeedBoost)
          removeModifier(swimSpeedAttribute, MobWaterSwimBoost)
          applyModifier(movementSpeedAttribute, MobLandSpeedBoost)
        }
    }
  }

  private def applyModifier(
      attribute: IAttributeInstance,
      modifier: AttributeModifier
  ): Unit = {
    if (!attribute.hasModifier(modifier)) {
      attribute.applyModifier(modifier)
    }
  }

  private def removeModifier(
      attribute: IAttributeInstance,
      modifier: AttributeModifier
  ): Unit = {
    if (attribute.hasModifier(modifier)) {
      attribute.removeModifier(modifier)
    }
  }
}
