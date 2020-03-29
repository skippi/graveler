package graveler;

import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;

public class CapabilityUtil {
  public static LazyOptional<Scheduler> getSchedulerOption(final IWorld world) {
    return getSchedulerOption(world.getWorld());
  }

  public static LazyOptional<Scheduler> getSchedulerOption(final World world) {
    return world.getCapability(SchedulerProvider.PHYSICS_CAP, null);
  }
}
