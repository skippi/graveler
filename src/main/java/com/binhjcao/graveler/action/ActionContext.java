package com.binhjcao.graveler.action;

import com.binhjcao.graveler.Scheduler;
import net.minecraft.server.level.ServerLevel;

public record ActionContext(Scheduler scheduler, ServerLevel world) {}
