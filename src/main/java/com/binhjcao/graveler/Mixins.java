package com.binhjcao.graveler;

import com.binhjcao.graveler.action.UpdateNeighborStress;
import com.binhjcao.graveler.action.UpdateStress;
import com.binhjcao.graveler.util.WorldExtensions;
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
    if (level.isClientSide() || !(level instanceof ServerLevel serverLevel)) {
      return;
    }

    Scheduler scheduler = WorldExtensions.getScheduler(serverLevel);
    if (scheduler != null) {
      scheduler.schedule(new UpdateStress(pos));
    }
  }
}
