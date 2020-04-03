package graveler

import net.minecraft.world.IWorld
import net.minecraft.world.World

val IWorld.scheduler: Scheduler?
  get() = world.world.scheduler

val World.scheduler: Scheduler?
  get() = getCapability(SchedulerProvider.CAPABILITY, null).orElse(null)
