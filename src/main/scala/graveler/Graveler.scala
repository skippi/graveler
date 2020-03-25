package graveler

import java.util.concurrent.Callable
import net.minecraftforge.common.capabilities.CapabilityManager
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import org.apache.logging.log4j.Logger

@Mod(
  modid = Graveler.ModId,
  name = Graveler.Name,
  version = Graveler.Version,
  modLanguage = "scala",
  modLanguageAdapter = "graveler.forge.ScalaLanguageAdapter"
)
object Graveler {
  final val ModId = "graveler"
  final val Name = "Graveler"
  final val Version = "1.1"

  final var Logger: Logger = null

  @EventHandler
  def preInit(event: FMLPreInitializationEvent): Unit = {
    this.Logger = event.getModLog
  }

  @EventHandler
  def init(event: FMLInitializationEvent): Unit = {
    CapabilityManager.INSTANCE.register[Scheduler](
      classOf[Scheduler],
      new SchedulerStorage,
      (() => Scheduler()): Callable[Scheduler]
    )
  }
}
