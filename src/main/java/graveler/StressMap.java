package graveler;

import com.mojang.serialization.Codec;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.BlockPos;

public class StressMap {
  public static final Codec<StressMap> CODEC =
      Codec.unboundedMap(BlockPos.CODEC, Codec.INT)
          .xmap(
              map -> {
                StressMap stressMap = new StressMap();
                stressMap.stresses.putAll(map);
                return stressMap;
              },
              stressMap -> Map.copyOf(stressMap.stresses));

  private final Map<BlockPos, Integer> stresses = new HashMap<>();

  public Integer get(BlockPos pos) {
    return stresses.get(pos);
  }

  public void set(BlockPos pos, int stress) {
    stresses.put(pos, stress);
  }

  public void remove(BlockPos pos) {
    stresses.remove(pos);
  }

  public boolean removeIfPresent(BlockPos pos) {
    return stresses.remove(pos) != null;
  }

  Map<BlockPos, Integer> stresses() {
    return stresses;
  }
}
