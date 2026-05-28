package graveler.action;

import graveler.Scheduler;
import net.minecraft.server.level.ServerLevel;

public record ActionContext(Scheduler scheduler, ServerLevel world) {}
