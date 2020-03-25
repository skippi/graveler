package caomc

import caomc.physics._
import net.minecraft.entity.Entity
import net.minecraft.world.World

object CapabilityExtensions {
  implicit class CapabilityExtendedWorld(private val world: World) {
    def getSchedulerOption: Option[Scheduler] = {
      Option(world)
        .map { _.getCapability(SchedulerProvider.PhysicsCap, null) }
    }

    def getGameOption: Option[Game] = {
      Option(world) map { _.getCapability(GameProvider.GameCap, null) }
    }
  }

  implicit class CapabilityExtendedEntity(private val entity: Entity) {
    def getLivesOption: Option[Lives] = {
      Option(entity) map { _.getCapability(LivesProvider.LivesCap, null) }
    }

    def getAuraOption: Option[Aura] = {
      Option(entity) map { _.getCapability(AuraProvider.AuraCap, null) }
    }
  }
}
