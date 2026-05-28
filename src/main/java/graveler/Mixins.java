package graveler;

import graveler.action.UpdateNeighborStress;
import graveler.action.UpdateStress;
import graveler.util.WorldExtensions;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public final class Mixins {
  private Mixins() {}

  public static void updateNeighbors(ServerLevel level, BlockPos pos) {
    if (level.isClientSide()) {
      return;
    }

    Scheduler scheduler = WorldExtensions.getScheduler(level);
    if (scheduler != null) {
      scheduler.schedule(new UpdateNeighborStress(pos));
    }
  }

  public static void onBlockAdded(Level level, BlockPos pos) {
    if (level.isClientSide()) {
      return;
    }

    Scheduler scheduler = WorldExtensions.getScheduler(level);
    if (scheduler != null) {
      scheduler.schedule(new UpdateStress(pos));
    }
  }
}
