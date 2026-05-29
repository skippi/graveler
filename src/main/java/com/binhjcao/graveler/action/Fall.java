package com.binhjcao.graveler.action;

import com.binhjcao.graveler.util.PointedWorld;
import com.binhjcao.graveler.util.WorldExtensions;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.Direction;

public record Fall(BlockPos pos) implements Action {
  @Override
  public double weight() {
    return 1 / 128.0;
  }

  @Override
  public void apply(ActionContext context) {
    fallAt(context.world(), pos);
  }

  private static void fallAt(ServerLevel level, BlockPos pos) {
    if (!level.hasChunksAt(
        pos.getX() - 32,
        pos.getY() - 32,
        pos.getZ() - 32,
        pos.getX() + 32,
        pos.getY() + 32,
        pos.getZ() + 32)) {
      forceInstantFallAt(level, pos);
      return;
    }

    if (level.isClientSide()) {
      return;
    }

    forceFallAt(level, pos);
  }

  private static void forceFallAt(Level level, BlockPos pos) {
    FallingBlockEntity.fall(level, pos, level.getBlockState(pos));
  }

  private static void forceInstantFallAt(Level level, BlockPos pos) {
    var state = level.getBlockState(pos);
    level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());

    PointedWorld below = WorldExtensions.pointedAt(level, pos).move(Direction.DOWN);
    while (below.allowsFallThrough() && below.pos().getY() > 0) {
      below = below.move(Direction.DOWN);
    }

    if (below.pos().getY() > 0) {
      level.setBlockAndUpdate(below.pos().above(), state);
    }
  }
}
