package graveler

import net.minecraft.util.math.BlockPos

data class StressMap(val stresses: MutableMap<BlockPos, Int>) {
  operator fun get(pos: BlockPos): Int? = stresses[pos]
  operator fun set(pos: BlockPos, stress: Int) = stresses.set(pos, stress)
}
