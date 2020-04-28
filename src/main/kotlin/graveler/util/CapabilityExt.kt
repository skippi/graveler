package graveler.util

import graveler.*
import io.skippi.cotm.util.toNullable
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.chunk.Chunk

fun World.pointedAt(pos: BlockPos): PointedWorld = PointedWorld(this, pos)

val Chunk.stressMap: StressMap? get() = getCapability(Capabilities.STRESS_MAP).toNullable

val World.scheduler: Scheduler? get() = getCapability(Capabilities.SCHEDULER).toNullable
