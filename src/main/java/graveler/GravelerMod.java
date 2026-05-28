package graveler;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class GravelerMod implements ModInitializer {
  public static final String MOD_ID = "graveler";

  @Override
  public void onInitialize() {
    GravelerAttachments.register();

    ServerTickEvents.END_LEVEL_TICK.register(level -> {
      Scheduler scheduler = level.getAttachedOrCreate(GravelerAttachments.SCHEDULER);
      scheduler.tick(level);
    });
  }
}
