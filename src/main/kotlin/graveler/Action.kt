package graveler

import net.minecraft.util.math.BlockPos

sealed class Action(val priority: Int)
data class Fall(val pos: BlockPos) : Action(1)
data class Gravity(val pos: BlockPos) : Action(0)
