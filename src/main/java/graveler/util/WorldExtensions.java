package graveler.util;

import graveler.GravelerAttachments;
import graveler.StressMap;
import graveler.Scheduler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.chunk.ChunkAccess;

public final class WorldExtensions {
  private WorldExtensions() {}

  public static PointedWorld pointedAt(LevelAccessor level, BlockPos pos) {
    return new PointedWorld(level, pos);
  }

  public static Scheduler getScheduler(Level level) {
    if (level instanceof ServerLevel serverLevel) {
      return serverLevel.getAttachedOrCreate(GravelerAttachments.SCHEDULER);
    }

    return null;
  }

  public static StressMap getStressMap(ChunkAccess chunk) {
    StressMap stressMap = chunk.getAttachedOrCreate(GravelerAttachments.STRESS_MAP);
    stressMap.bindTo(chunk);
    return stressMap;
  }
}
