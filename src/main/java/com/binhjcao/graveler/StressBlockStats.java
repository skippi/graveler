package com.binhjcao.graveler;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public record StressBlockStats(int strength) {
  public static final int DEFAULT_STRENGTH = 8;

  public static StressBlockStats get(Block block) {
    return new StressBlockStats(strength(block));
  }

  private static int strength(Block block) {
    if (isTorch(block)) {
      return 1;
    }
    var blockTags = block.builtInRegistryHolder();
    if (blockTags.is(BlockTags.DIRT) || blockTags.is(BlockTags.GRASS_BLOCKS)) {
      return 4;
    }
    return DEFAULT_STRENGTH;
  }

  private static boolean isTorch(Block block) {
    return BuiltInRegistries.BLOCK.getKey(block).getPath().contains("torch");
  }
}
