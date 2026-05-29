package graveler;

import java.util.Map;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public record StressBlockStats(int strength) {
  public static final int DEFAULT_STRENGTH = 8;

  public static StressBlockStats get(Block block) {
    return new StressBlockStats(DEFAULT_STRENGTH);
  }
}
