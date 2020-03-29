package graveler;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class SchedulerStorage implements IStorage<Scheduler> {
  @Override
  public INBT writeNBT(Capability<Scheduler> capability, Scheduler instance, Direction side) {
    return null;
  }

  @Override
  public void readNBT(
      Capability<Scheduler> capability, Scheduler instance, Direction side, INBT nbt) {
    // Do nothing
  }
}
