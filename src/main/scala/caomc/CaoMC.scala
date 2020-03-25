package caomc

import caomc.physics._
import java.util.concurrent.Callable
import net.minecraftforge.common.capabilities.CapabilityManager
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.event.FMLServerStartingEvent
import org.apache.logging.log4j.Logger

@Mod(
  modid = CaoMC.ModId,
  name = CaoMC.Name,
  version = CaoMC.Version,
  modLanguage = "scala",
  modLanguageAdapter = "caomc.forge.ScalaLanguageAdapter"
)
object CaoMC {
  final val ModId = "caomc"
  final val Name = "CaoMC"
  final val Version = "1.1"

  final var Logger: Logger = null

  @EventHandler
  def preInit(event: FMLPreInitializationEvent): Unit = {
    this.Logger = event.getModLog
  }

  @EventHandler
  def init(event: FMLInitializationEvent): Unit = {
    CapabilityManager.INSTANCE.register[Game](
      classOf[Game],
      new GameStorage,
      new Callable[Game] { def call = new Game }
    )

    CapabilityManager.INSTANCE.register[Lives](
      classOf[Lives],
      new LivesStorage,
      new Callable[Lives] { def call = new Lives }
    )

    CapabilityManager.INSTANCE.register[Scheduler](
      classOf[Scheduler],
      new SchedulerStorage,
      (() => Scheduler()): Callable[Scheduler]
    )

    CapabilityManager.INSTANCE.register[Aura](
      classOf[Aura],
      new AuraStorage,
      new Callable[Aura] { def call = new Aura }
    )
  }

  @EventHandler
  def serverStarting(event: FMLServerStartingEvent): Unit = {
    event.registerServerCommand(new CaoCommand)
  }
}
