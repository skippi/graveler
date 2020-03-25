package graveler.physics

import graveler.forge.ScalaAnnotations._
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.ICapabilityProvider

class SchedulerProvider extends ICapabilityProvider {
  import SchedulerProvider._

  private val Instance = PhysicsCap.getDefaultInstance

  override def hasCapability(
      capability: Capability[_],
      side: EnumFacing
  ): Boolean = {
    capability == PhysicsCap
  }

  override def getCapability[T](
      capability: Capability[T],
      side: EnumFacing
  ): T = {
    if (capability == PhysicsCap)
      PhysicsCap.cast[T](Instance)
    else null.asInstanceOf[T]
  }
}

object SchedulerProvider {
  @capabilityInject(classOf[Scheduler])
  final var PhysicsCap: Capability[Scheduler] = null
}
