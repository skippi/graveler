package graveler

import net.minecraft.util.math.BlockPos

data class StressMap(
  var preloaded: Boolean = false,
  val stresses: MutableMap<BlockPos, Int> = mutableMapOf()
) {
  operator fun get(pos: BlockPos): Int? = stresses[pos]
  operator fun set(pos: BlockPos, stress: Int) = stresses.set(pos, stress)
}
