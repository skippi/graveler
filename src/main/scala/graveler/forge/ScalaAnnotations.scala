package graveler.forge

import net.minecraftforge.common.capabilities.CapabilityInject
import scala.annotation.meta.setter

object ScalaAnnotations {
  type capabilityInject = CapabilityInject @setter
}
