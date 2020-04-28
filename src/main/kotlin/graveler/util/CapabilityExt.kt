package graveler.util

import graveler.Capabilities
import graveler.Scheduler
import graveler.StressMap
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorld
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.chunk.IChunk

fun IWorld.pointedAt(pos: BlockPos): PointedWorld = PointedWorld(this, pos)

val IChunk.stressMap: StressMap?
  get() = (this as? Chunk)?.getCapability(Capabilities.STRESS_MAP)?.toNullable

val IWorld.scheduler: Scheduler?
  get() = world.getCapability(Capabilities.SCHEDULER).toNullable
