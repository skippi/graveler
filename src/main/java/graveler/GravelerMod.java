package graveler;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(GravelerMod.MODID)
public class GravelerMod {
  private static final Logger LOGGER = LogManager.getLogger();

  public static final String MODID = "graveler";

  public GravelerMod() {
    IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    modEventBus.addListener(this::init);

    MinecraftForge.EVENT_BUS.register(this);

    Test.Logger().info("OMG?");
  }

  private void init(final FMLCommonSetupEvent event) {
    CapabilityManager.INSTANCE.<Scheduler>register(
        Scheduler.class, new SchedulerStorage(), Scheduler::apply);
  }
}
