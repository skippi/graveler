package graveler

import net.minecraft.world.World

object CapabilityExtensions {
  implicit class CapabilityExtendedWorld(private val world: World) {
    def getSchedulerOption: Option[Scheduler] = {
      Option(world)
        .map { _.getCapability(SchedulerProvider.PhysicsCap, null) }
    }
  }
}
