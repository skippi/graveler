package graveler

import graveler.util.stressMap
import net.minecraft.block.*
import net.minecraft.block.material.Material
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

data class PointedWorld(val world: World, val pos: BlockPos) {
  val stress: Int get() {
    if (block != Blocks.BLACK_WOOL) return 0

    val stressMap = world.stressMap ?: return 0
    val below = move(Direction.DOWN)
    val belowStress = when {
      below.block == Blocks.BLACK_WOOL -> stressMap[below.pos] ?: 0
      below.blockState.isSolidSide(world, below.pos, Direction.UP) -> 0
      else -> MAX_STRESS
    }

    val horizontalStress = Direction.Plane.HORIZONTAL
      .map { move(it) }
      .filter { it.block == Blocks.BLACK_WOOL }
      .map { stressMap[it.pos] ?: 0 }
      .map { it + 1 }

    return (horizontalStress + belowStress).min()!!
  }

  fun move(direction: Direction): PointedWorld = copy(pos = pos.offset(direction))

  val blockState: BlockState
    get() = world.getBlockState(pos)

  private val block: Block
    get() = blockState.block

  private val material: Material
    get() = blockState.material

  val allowsFallThrough: Boolean
    get() = (!material.blocksMovement() || material.isLiquid) &&
      block !is CauldronBlock

  companion object {
    private const val MAX_STRESS = 50
  }
}
