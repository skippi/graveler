package graveler;

import static graveler.CapabilityUtil.*;
import static graveler.math.Vec3Util.*;

import graveler.math.Bounds;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import net.minecraft.block.*;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PhysicsUtil {
  private static final Logger LOGGER = LogManager.getLogger();

  public static void fallAt(final World world, final BlockPos pos) {
    final Bounds targetBounds = new Bounds(pos, new Vec3i(64, 64, 64));

    if (!isAreaLoaded(world, targetBounds)) {
      forceInstantFallAt(world, pos);
      return;
    }

    if (world.isRemote) {
      return;
    }

    forceFallAt(world, pos);
  }

  private static void forceFallAt(final World world, final BlockPos pos) {
    world.addEntity(
        new FallingBlockEntity(
            world,
            pos.getX() + 0.5d,
            (double) pos.getY(),
            pos.getZ() + 0.5d,
            world.getBlockState(pos)));
  }

  private static void forceInstantFallAt(final World world, final BlockPos pos) {
    final BlockState state = world.getBlockState(pos);
    if (state == null) {
      return;
    }

    world.setBlockState(pos, Blocks.AIR.getDefaultState());

    BlockPos newPos = pos.down();
    while (allowsFallThrough(world.getBlockState(newPos)) && newPos.getY() > 0) {
      newPos = newPos.down();
    }

    if (newPos.getY() > 0) {
      // Forge: Fix loss of state information during world gen.
      world.setBlockState(newPos.up(), state);
    }
  }

  private static boolean hasSupportingBlockAt(final World world, final BlockPos pos) {
    final BlockState state = world.getBlockState(pos);
    final BlockState downState = world.getBlockState(pos.down());

    return allowsSupporting(state) && !allowsFallThrough(downState);
  }

  private static boolean isAreaLoaded(final World world, final Bounds bounds) {
    return world.isAreaLoaded(new BlockPos(bounds.getMin()), new BlockPos(bounds.getMax()));
  }

  private static boolean isStableAt(final World world, final BlockPos pos) {
    if (pos.getY() < 0) {
      return true;
    }

    final BlockState state = world.getBlockState(pos);
    if (!allowsFalling(state)) {
      return true;
    }

    final BlockState downState = world.getBlockState(pos.down());
    if (!allowsFallThrough(downState)) {
      return true;
    }

    final HashSet<BlockPos> visited = new HashSet<BlockPos>();
    final Queue<BlockPos> posesToVisit = new ArrayDeque<BlockPos>();
    posesToVisit.add(pos);

    final float originAdhesion = adhesion(state);
    int count = 0;

    while (!posesToVisit.isEmpty() && count < 128) {
      final BlockPos currentPos = posesToVisit.remove();
      if (visited.contains(currentPos)) {
        continue;
      }

      ++count;
      visited.add(currentPos);

      final BlockState currentPosState = world.getBlockState(currentPos);
      if (!allowsSupporting(currentPosState)) {
        continue;
      }

      final float combinedAdhesion = (0.7f * originAdhesion + 0.3f * adhesion(currentPosState));
      final double distToPos = norm(subtract(currentPos, pos));
      if (distToPos > combinedAdhesion) {
        continue;
      }

      if (hasSupportingBlockAt(world, currentPos)) {
        return true;
      }

      for (int i = 0; i < 4; ++i) {
        final Direction dir = Direction.byHorizontalIndex(i);
        posesToVisit.add(currentPos.offset(dir));
      }
    }

    return false;
  }

  public static void triggerGravityAt(final World world, final BlockPos origin) {
    final HashSet<BlockPos> visited = new HashSet<BlockPos>();

    final Queue<BlockPos> posesToVisit = new ArrayDeque<BlockPos>();
    posesToVisit.add(origin);

    for (int i = 0; i < 6; ++i) {
      posesToVisit.add(origin.offset(Direction.byIndex(i)));
    }

    final Scheduler scheduler = getSchedulerOption(world).orElseThrow(NullPointerException::new);
    int count = 0;

    while (!posesToVisit.isEmpty() && count < 512) {
      final BlockPos pos = posesToVisit.remove();

      if (visited.contains(pos)) {
        continue;
      }

      count += 1;
      visited.add(pos);

      if (!allowsFalling(world.getBlockState(pos))) {
        continue;
      }

      if (!isStableAt(world, pos)) {
        scheduler.schedule(new Fall(pos));

        posesToVisit.add(pos.offset(Direction.DOWN));

        for (int i = 0; i < 4; ++i) {
          final Direction dir = Direction.byHorizontalIndex(i);
          posesToVisit.add(pos.offset(dir));
        }

        posesToVisit.add(pos.offset(Direction.UP));
      } else {
        for (int i = 0; i < 4; ++i) {
          final Direction dir = Direction.byHorizontalIndex(i);
          posesToVisit.add(pos.offset(dir));
        }
      }
    }
  }

  public static float adhesion(final BlockState state) {
    float hardness = Math.max(Math.min(state.getBlockHardness(null, null), 10f), 0.6f);
    return 2 * hardness;
  }

  public static boolean allowsFalling(final BlockState state) {
    final Block block = state.getBlock();

    return (!isPassable(state)
        && !isLiquid(state)
        && block != Blocks.BEDROCK
        && !(block instanceof FallingBlock)
        && !(block instanceof LeavesBlock));
  }

  public static boolean allowsSupporting(final BlockState state) {
    final Block block = state.getBlock();

    return (!isPassable(state) && !isLiquid(state) && !(block instanceof LeavesBlock));
  }

  public static boolean allowsFallThrough(final BlockState state) {
    final Block block = state.getBlock();

    return ((isPassable(state) || isLiquid(state)) && !(block instanceof CauldronBlock));
  }

  private static boolean isLiquid(final BlockState state) {
    return state.getMaterial().isLiquid();
  }

  private static boolean isPassable(final BlockState state) {
    return !state.getMaterial().blocksMovement(); // TODO: Fix this broken hack
    // try {
    //   return state.getBlock().isPassable(null, null);
    // } catch (NullPointerException e) {

    // }
  }
}
