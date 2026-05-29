package com.binhjcao.graveler;

import com.binhjcao.graveler.action.Action;
import com.binhjcao.graveler.action.ActionContext;
import java.util.ArrayDeque;
import java.util.Deque;
import net.minecraft.server.level.ServerLevel;

public class Scheduler {
  private final Deque<Action> queue = new ArrayDeque<>();
  private final double processingRate;

  public Scheduler() {
    this(1.0);
  }

  public Scheduler(double processingRate) {
    this.processingRate = processingRate;
  }

  public Scheduler schedule(Action action) {
    queue.addLast(action);
    return this;
  }

  public Scheduler tick(ServerLevel level) {
    double count = 0.0;
    ActionContext context = new ActionContext(this, level);

    while (!queue.isEmpty() && count < processingRate) {
      Action action = queue.removeFirst();
      action.apply(context);
      count += action.weight();
    }

    return this;
  }
}
