package graveler;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.BlockPos;

public class StressMap {
  private record Entry(BlockPos pos, int stress) {
    static final Codec<Entry> CODEC =
        RecordCodecBuilder.create(
            instance ->
                instance
                    .group(
                        BlockPos.CODEC.fieldOf("pos").forGetter(Entry::pos),
                        Codec.INT.fieldOf("stress").forGetter(Entry::stress))
                    .apply(instance, Entry::new));
  }

  public static final Codec<StressMap> CODEC =
      Entry.CODEC
          .listOf()
          .xmap(
              entries -> {
                StressMap stressMap = new StressMap();
                for (Entry entry : entries) {
                  stressMap.stresses.put(entry.pos(), entry.stress());
                }
                return stressMap;
              },
              stressMap ->
                  stressMap.stresses.entrySet().stream()
                      .map(entry -> new Entry(entry.getKey(), entry.getValue()))
                      .toList());

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
