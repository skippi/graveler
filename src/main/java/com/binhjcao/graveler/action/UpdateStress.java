package com.binhjcao.graveler.action;

import com.binhjcao.graveler.StressBlockStats;
import com.binhjcao.graveler.util.PointedWorld;
import com.binhjcao.graveler.util.WorldExtensions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public record UpdateStress(BlockPos pos) implements Action {
  @Override
  public double weight() {
    return 0.0;
  }

  @Override
  public void apply(ActionContext context) {
    PointedWorld point = WorldExtensions.pointedAt(context.world(), pos);

    if (!point.isStressAware()) {
      point.clearStress();
      return;
    }

    if (!context.world().hasChunkAt(pos)) {
      return;
    }

    int newStress = getNewStress(point);
    if (newStress >= StressBlockStats.get(point.block()).strength()) {
      context.scheduler().schedule(new Fall(pos));
    }

    if (point.stress() != newStress) {
      point.setStress(newStress);
      context.scheduler().schedule(new UpdateNeighborStress(pos));
    }
  }

  private static int getNewStress(PointedWorld point) {
    if (!point.isStressAware()) {
      return 0;
    }
    int stress = point.move(Direction.DOWN).stress();
    for (Direction direction : Direction.Plane.HORIZONTAL) {
      var neighbor = point.move(direction);
      stress = Math.min(stress, neighbor.stress());
    }
    stress = Math.max(0, stress + cantileverPenalty(point));
    stress = Math.max(0, stress - archBonus(point));
    return stress;
  }

  private static int cantileverPenalty(PointedWorld point) {
    if (!point.move(Direction.DOWN).blockState().isSolid()) {
      return 1;
    }
    return 0;
  }

  private static int archBonus(PointedWorld point) {
    var result = 0;
    var below = point.move(Direction.DOWN);
    var eastDown = below.move(Direction.EAST);
    var westDown = below.move(Direction.WEST);
    if (eastDown.blockState().isSolid() && westDown.blockState().isSolid()) {
      result += 1;
    }
    var northDown = below.move(Direction.NORTH);
    var southDown = below.move(Direction.SOUTH);
    if (northDown.blockState().isSolid() && southDown.blockState().isSolid()) {
      result += 1;
    }
    return result;
  }
}
