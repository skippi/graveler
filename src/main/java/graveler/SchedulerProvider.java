package graveler;

import javax.annotation.*;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

public class SchedulerProvider implements ICapabilityProvider {
  @CapabilityInject(Scheduler.class)
  public static final Capability<Scheduler> CAPABILITY = null;

  private LazyOptional<Scheduler> instance = LazyOptional.of(CAPABILITY::getDefaultInstance);

  @Override
  public @Nonnull <T> LazyOptional<T> getCapability(
      @Nonnull final Capability<T> cap, final @Nullable Direction side) {
    return CAPABILITY.orEmpty(cap, instance);
  }
}
