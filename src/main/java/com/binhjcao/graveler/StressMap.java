package com.binhjcao.graveler;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;

public class StressMap {
  public static final int SECTION_BLOCK_COUNT = 4096;

  @FunctionalInterface
  public interface NonZeroVisitor {
    void accept(int worldX, int worldY, int worldZ, int stress);
  }

  private record SectionEntry(int section, List<Byte> data) {
    static final Codec<SectionEntry> CODEC =
        RecordCodecBuilder.create(
            instance ->
                instance
                    .group(
                        Codec.INT.fieldOf("section").forGetter(SectionEntry::section),
                        Codec.BYTE.listOf().fieldOf("data").forGetter(SectionEntry::data))
                    .apply(instance, SectionEntry::new));

    byte[] toArray() {
      byte[] bytes = new byte[SECTION_BLOCK_COUNT];
      int length = Math.min(data.size(), SECTION_BLOCK_COUNT);
      for (int i = 0; i < length; i++) {
        bytes[i] = data.get(i);
      }
      return bytes;
    }

    static SectionEntry fromArray(int section, byte[] data) {
      List<Byte> encoded = new ArrayList<>(SECTION_BLOCK_COUNT);
      for (byte value : data) {
        encoded.add(value);
      }
      return new SectionEntry(section, encoded);
    }
  }

  private record SectionStorage(List<SectionEntry> sections) {
    static final Codec<SectionStorage> CODEC =
        RecordCodecBuilder.create(
            instance ->
                instance
                    .group(
                        SectionEntry.CODEC.listOf().fieldOf("sections").forGetter(SectionStorage::sections))
                    .apply(instance, SectionStorage::new));
  }

  public static final Codec<StressMap> CODEC =
      SectionStorage.CODEC.xmap(StressMap::fromSections, StressMap::toSectionStorage);

  private byte[][] sections;
  private int minY = Integer.MIN_VALUE;

  public void bindTo(ChunkAccess chunk) {
    if (minY == Integer.MIN_VALUE) {
      minY = chunk.getMinY();
    }

    ensureSections(chunk.getSectionsCount());
  }

  public boolean hasNonZeroData() {
    if (sections == null) {
      return false;
    }

    for (byte[] section : sections) {
      if (section != null) {
        return true;
      }
    }

    return false;
  }

  public void forEachNonZeroInBox(
      ChunkPos chunkPos, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, NonZeroVisitor visitor) {
    if (sections == null) {
      return;
    }

    int chunkBaseX = chunkPos.getMinBlockX();
    int chunkBaseZ = chunkPos.getMinBlockZ();
    int minSection = Math.max(0, (minY - this.minY) >> 4);
    int maxSection = Math.min(sections.length - 1, (maxY - this.minY) >> 4);

    for (int sectionIndex = minSection; sectionIndex <= maxSection; sectionIndex++) {
      byte[] section = sections[sectionIndex];
      if (section == null) {
        continue;
      }

      int sectionBaseY = this.minY + (sectionIndex << 4);
      for (int index = 0; index < SECTION_BLOCK_COUNT; index++) {
        byte value = section[index];
        if (value == 0) {
          continue;
        }

        int worldX = chunkBaseX + (index & 15);
        int worldY = sectionBaseY + ((index >> 8) & 15);
        int worldZ = chunkBaseZ + ((index >> 4) & 15);
        if (worldX < minX || worldX > maxX || worldY < minY || worldY > maxY || worldZ < minZ || worldZ > maxZ) {
          continue;
        }

        visitor.accept(worldX, worldY, worldZ, value & 0xFF);
      }
    }
  }

  public int get(BlockPos pos) {
    if (sections == null) {
      return 0;
    }

    byte[] section = sections[sectionIndex(pos)];
    if (section == null) {
      return 0;
    }

    return section[indexInSection(pos)] & 0xFF;
  }

  public void set(BlockPos pos, int stress) {
    if (stress == 0) {
      removeIfPresent(pos);
      return;
    }

    int sectionIndex = sectionIndex(pos);
    if (sections == null) {
      throw new IllegalStateException("StressMap must be bound to a chunk before writing");
    }

    byte[] section = sections[sectionIndex];
    if (section == null) {
      section = new byte[SECTION_BLOCK_COUNT];
      sections[sectionIndex] = section;
    }

    section[indexInSection(pos)] = (byte) stress;
  }

  public boolean removeIfPresent(BlockPos pos) {
    if (sections == null) {
      return false;
    }

    int sectionIndex = sectionIndex(pos);
    byte[] section = sections[sectionIndex];
    if (section == null) {
      return false;
    }

    int index = indexInSection(pos);
    if (section[index] == 0) {
      return false;
    }

    section[index] = 0;
    if (isSectionEmpty(section)) {
      sections[sectionIndex] = null;
      if (isSectionsEmpty()) {
        sections = null;
      }
    }

    return true;
  }

  private int sectionIndex(BlockPos pos) {
    if (minY == Integer.MIN_VALUE) {
      throw new IllegalStateException("StressMap must be bound to a chunk before access");
    }

    return (pos.getY() - minY) >> 4;
  }

  private static int indexInSection(BlockPos pos) {
    int x = pos.getX() & 15;
    int y = pos.getY() & 15;
    int z = pos.getZ() & 15;
    return (y << 8) | (z << 4) | x;
  }

  private void ensureSections(int sectionCount) {
    if (sections == null) {
      sections = new byte[sectionCount][];
      return;
    }

    if (sections.length < sectionCount) {
      sections = Arrays.copyOf(sections, sectionCount);
    }
  }

  private static boolean isSectionEmpty(byte[] section) {
    for (byte value : section) {
      if (value != 0) {
        return false;
      }
    }
    return true;
  }

  private boolean isSectionsEmpty() {
    for (byte[] section : sections) {
      if (section != null) {
        return false;
      }
    }
    return true;
  }

  private static StressMap fromSections(SectionStorage storage) {
    StressMap stressMap = new StressMap();
    if (storage.sections.isEmpty()) {
      return stressMap;
    }

    int maxSection =
        storage.sections.stream().mapToInt(SectionEntry::section).max().orElse(0);
    stressMap.sections = new byte[maxSection + 1][];
    for (SectionEntry entry : storage.sections) {
      stressMap.sections[entry.section()] = entry.toArray();
    }
    return stressMap;
  }

  private SectionStorage toSectionStorage() {
    if (sections == null) {
      return new SectionStorage(List.of());
    }

    List<SectionEntry> encoded = new ArrayList<>();
    for (int sectionIndex = 0; sectionIndex < sections.length; sectionIndex++) {
      byte[] section = sections[sectionIndex];
      if (section != null && !isSectionEmpty(section)) {
        encoded.add(SectionEntry.fromArray(sectionIndex, section));
      }
    }
    return new SectionStorage(encoded);
  }
}
