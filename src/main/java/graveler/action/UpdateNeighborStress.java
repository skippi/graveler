package graveler.action;

import graveler.util.WorldExtensions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public record UpdateNeighborStress(BlockPos pos) implements Action {
  private static final Direction[] UPDATE_ORDER = {
    Direction.WEST,
    Direction.EAST,
    Direction.NORTH,
    Direction.SOUTH,
    Direction.DOWN,
    Direction.UP
  };

  @Override
  public double weight() {
    return 0.0;
  }

  @Override
  public void apply(ActionContext context) {
    for (Direction direction : UPDATE_ORDER) {
      BlockPos neighbor = pos.relative(direction);
      context.scheduler().schedule(new UpdateStress(neighbor));
    }
  }
}
