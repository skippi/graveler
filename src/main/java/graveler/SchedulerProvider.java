package graveler;

import javax.annotation.*;

import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

public class SchedulerProvider implements ICapabilityProvider {
  @CapabilityInject(Scheduler.class)
  public static final Capability<Scheduler> PHYSICS_CAP = null;

  private LazyOptional<Scheduler> instance = LazyOptional.of(PHYSICS_CAP::getDefaultInstance);

  @Override
  public @Nonnull <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> cap, final @Nullable Direction side) {
    return PHYSICS_CAP.orEmpty(cap, instance);
  }
}
