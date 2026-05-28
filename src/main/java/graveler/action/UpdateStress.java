package graveler.action;

import graveler.util.PointedWorld;
import graveler.util.WorldExtensions;
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
    if (newStress >= 7) {
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

    PointedWorld below = point.move(Direction.DOWN);
    int horizontalStress =
        Direction.Plane.HORIZONTAL
            .stream()
            .mapToInt(direction -> point.move(direction).stress() + 1)
            .min()
            .orElse(Integer.MAX_VALUE);

    return Math.min(horizontalStress, below.stress());
  }
}
