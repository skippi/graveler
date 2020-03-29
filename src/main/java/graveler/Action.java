package graveler;

import net.minecraft.util.math.BlockPos;

public class Action {
  public final int priority;

  public Action(int priority) {
    this.priority = priority;
  }

  static class Fall extends Action {
    public final BlockPos pos;
    
    public Fall(final BlockPos pos) {
      super(1);

      this.pos = pos;
    }
  }

  static class Gravity extends Action {
    public final BlockPos pos;
    
    public Gravity(final BlockPos pos) {
      super(0);

      this.pos = pos;
    }
  }
}
