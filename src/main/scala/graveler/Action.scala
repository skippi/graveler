package graveler

import net.minecraft.util.math.BlockPos

sealed abstract class Action(val priority: Int)
case class Fall(val pos: BlockPos) extends Action(1)
case class Gravity(val pos: BlockPos) extends Action(0)
