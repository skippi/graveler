package graveler;

import static graveler.PhysicsUtil.*;

import graveler.collection.UniquePriorityQueue;
import java.util.HashSet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Scheduler {
  private static Logger LOGGER = LogManager.getLogger();

  private int processingRate;

  private HashSet<ChunkPos> allowedChunks = new HashSet<ChunkPos>();
  private UniquePriorityQueue<Action> queue =
      new UniquePriorityQueue<Action>(
          11, (Action a, Action b) -> Integer.compare(b.priority, a.priority));
  private int cooldown = 0;

  public Scheduler(int processingRate) {
    this.processingRate = processingRate;
  }

  public static Scheduler withDefaults() {
    return new Scheduler(32);
  }

  private boolean canActAt(final BlockPos pos) {
    return allowedChunks.contains(new ChunkPos(pos));
  }

  private boolean canPerformAction() {
    return !isBusy() && hasPendingActions();
  }

  private boolean isBusy() {
    return cooldown != 0;
  }

  private boolean hasPendingActions() {
    return !queue.isEmpty();
  }

  public void schedule(final Action action) {
    queue.add(action);
  }

  public void setPhysicsAt(final BlockPos pos, final boolean state) {
    setPhysicsAt(new ChunkPos(pos), state);
  }

  public void setPhysicsAt(final ChunkPos pos, final boolean state) {
    if (state) {
      allowedChunks.add(pos);
    } else {
      allowedChunks.remove(pos);
    }
  }

  public void tick(final World world) {
    if (canPerformAction()) {
      for (int i = 0; i < processingRate && hasPendingActions(); ++i) {
        final Action action = queue.remove();

        LOGGER.info("applying action {}", action);

        if (action instanceof Action.Fall) {
          final Action.Fall fall = (Action.Fall) action;

          fallAt(world, fall.pos);
        } else if (action instanceof Action.Gravity) {
          final Action.Gravity gravity = (Action.Gravity) action;

          if (canActAt(gravity.pos)) {
            triggerGravityAt(world, gravity.pos);
          }
        }
      }
    }

    cooldown = (cooldown + 1) % 1;
  }
}
