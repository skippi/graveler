package graveler.util;

import graveler.StressMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;

public record PointedWorld(LevelAccessor level, BlockPos pos) {
  private static final int MAX_STRESS = 50;

  public ChunkAccess chunk() {
    return level.getChunk(pos);
  }

  public PointedWorld move(Direction direction) {
    return new PointedWorld(level, pos.relative(direction));
  }

  public BlockState blockState() {
    return level.getBlockState(pos);
  }

  public Block block() {
    return blockState().getBlock();
  }

  public boolean isStressAware() {
    return !blockState().isAir() && !isPermanentlyStable();
  }

  public boolean isPermanentlyStable() {
    return block() == Blocks.BEDROCK || blockState().liquid();
  }

  public boolean allowsFallThrough() {
    return (!blockState().blocksMotion() || blockState().liquid())
        && !(block() instanceof AbstractCauldronBlock);
  }

  public int stress() {
    if (isStressAware()) {
      ChunkAccess chunk = chunk();
      if (chunk == null) {
        return 0;
      }

      StressMap stressMap = WorldExtensions.getStressMap(chunk);
      Integer value = stressMap.get(pos);
      return value == null ? 0 : value;
    }

    if (isPermanentlyStable()) {
      return 0;
    }

    return MAX_STRESS;
  }

  public void setStress(int value) {
    if (!isStressAware()) {
      return;
    }

    ChunkAccess chunk = chunk();
    if (chunk == null) {
      return;
    }

    StressMap stressMap = WorldExtensions.getStressMap(chunk);
    stressMap.set(pos, value);
    chunk.markUnsaved();
  }

  public void clearStress() {
    ChunkAccess chunk = chunk();
    if (chunk == null) {
      return;
    }

    StressMap stressMap = WorldExtensions.getStressMap(chunk);
    if (stressMap.removeIfPresent(pos)) {
      chunk.markUnsaved();
    }
  }
}
