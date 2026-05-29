package com.binhjcao.graveler.client;

import com.binhjcao.graveler.GravelerAttachments;
import com.binhjcao.graveler.StressBlockStats;
import com.binhjcao.graveler.StressMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.debug.DebugValueAccess;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.phys.AABB;

public final class StressDebugRenderer implements DebugRenderer.SimpleDebugRenderer {
  private static final int RADIUS = 12;
  private static final int FILL_ALPHA = 45;
  private static final int COLLAPSE_STRESS = StressBlockStats.DEFAULT_STRENGTH;
  private static final BlockPos.MutableBlockPos MUTABLE_POS = new BlockPos.MutableBlockPos();
  private static final AABB BLOCK_BOUNDS = new AABB(0, 0, 0, 1, 1, 1);

  private static int[] xs = new int[0];
  private static int[] ys = new int[0];
  private static int[] zs = new int[0];
  private static int[] stresses = new int[0];
  private static int highlightCount;

  public static void refresh(Minecraft minecraft) {
    if (minecraft.player == null) {
      highlightCount = 0;
      return;
    }

    MinecraftServer server = minecraft.getSingleplayerServer();
    if (server == null) {
      highlightCount = 0;
      return;
    }

    ClientLevel clientLevel = minecraft.level;
    if (clientLevel == null) {
      highlightCount = 0;
      return;
    }

    ServerLevel serverLevel = server.getLevel(clientLevel.dimension());
    if (serverLevel == null) {
      highlightCount = 0;
      return;
    }

    BlockPos center = minecraft.player.blockPosition();
    int minX = center.getX() - RADIUS;
    int minY = center.getY() - RADIUS;
    int minZ = center.getZ() - RADIUS;
    int maxX = center.getX() + RADIUS;
    int maxY = center.getY() + RADIUS;
    int maxZ = center.getZ() + RADIUS;

    IntArrayList xList = new IntArrayList();
    IntArrayList yList = new IntArrayList();
    IntArrayList zList = new IntArrayList();
    IntArrayList stressList = new IntArrayList();

    for (int chunkX = minX >> 4; chunkX <= maxX >> 4; chunkX++) {
      for (int chunkZ = minZ >> 4; chunkZ <= maxZ >> 4; chunkZ++) {
        if (!serverLevel.hasChunk(chunkX, chunkZ)) {
          continue;
        }

        ChunkAccess chunk = serverLevel.getChunk(chunkX, chunkZ);
        StressMap stressMap = chunk.getAttached(GravelerAttachments.STRESS_MAP);
        if (stressMap == null || !stressMap.hasNonZeroData()) {
          continue;
        }

        stressMap.bindTo(chunk);
        ChunkPos chunkPos = chunk.getPos();
        stressMap.forEachNonZeroInBox(
            chunkPos,
            minX,
            minY,
            minZ,
            maxX,
            maxY,
            maxZ,
            (worldX, worldY, worldZ, stress) -> {
              xList.add(worldX);
              yList.add(worldY);
              zList.add(worldZ);
              stressList.add(stress);
            });
      }
    }

    highlightCount = xList.size();
    ensureCapacity(highlightCount);
    for (int i = 0; i < highlightCount; i++) {
      xs[i] = xList.getInt(i);
      ys[i] = yList.getInt(i);
      zs[i] = zList.getInt(i);
      stresses[i] = stressList.getInt(i);
    }
  }

  public static void clear() {
    highlightCount = 0;
  }

  @Override
  public void emitGizmos(
      double camX,
      double camY,
      double camZ,
      DebugValueAccess debugValueAccess,
      Frustum frustum,
      float partialTick) {
    for (int i = 0; i < highlightCount; i++) {
      int worldX = xs[i];
      int worldY = ys[i];
      int worldZ = zs[i];
      if (frustum != null && !frustum.isVisible(BLOCK_BOUNDS.move(worldX, worldY, worldZ))) {
        continue;
      }

      MUTABLE_POS.set(worldX, worldY, worldZ);
      Gizmos.cuboid(MUTABLE_POS, GizmoStyle.fill(colorForStress(stresses[i])));
    }
  }

  private static void ensureCapacity(int size) {
    if (xs.length >= size) {
      return;
    }

    int capacity = Math.max(size, xs.length * 2 + 64);
    xs = new int[capacity];
    ys = new int[capacity];
    zs = new int[capacity];
    stresses = new int[capacity];
  }

  private static int colorForStress(int stress) {
    int headroom = COLLAPSE_STRESS - stress;
    float ratio;
    if (headroom <= 1) {
      ratio = 1.0f;
    } else {
      float load = stress / (float) (COLLAPSE_STRESS - 1);
      ratio = (float) Math.pow(Mth.clamp(load, 0.0f, 1.0f), 0.55f);
    }

    int red;
    int green;
    if (ratio <= 0.5f) {
      float t = ratio * 2.0f;
      red = (int) (255 * t);
      green = (int) (255 - (255 - 165) * t);
    } else {
      float t = (ratio - 0.5f) * 2.0f;
      red = 255;
      green = (int) (165 * (1.0f - t));
    }

    return ARGB.color(FILL_ALPHA, red, green, 0);
  }
}
