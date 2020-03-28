package graveler;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@Mod("graveler")
public class GravelerMod {
  private static final Logger LOGGER = LogManager.getLogger();

  public GravelerMod() {
    IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    modEventBus.addListener(this::init);

    MinecraftForge.EVENT_BUS.register(this);
  }

  private void init(final FMLCommonSetupEvent event) {
    // CapabilityManager.INSTANCE.register[Scheduler](
    //   classOf[Scheduler],
    //   new SchedulerStorage,
    //   (() => Scheduler()): Callable[Scheduler]
    // )
  }
}
