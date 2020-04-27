package graveler.util

import graveler.PointedWorld
import graveler.StressMap
import graveler.StressMapStorage
import io.skippi.cotm.util.toNullable
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

fun World.pointedAt(pos: BlockPos): PointedWorld = PointedWorld(this, pos)

val World.stressMap: StressMap? get() = getCapability(StressMapStorage.CAPABILITY).toNullable
