package graveler

import graveler.util.stressMap
import net.minecraft.block.*
import net.minecraft.block.material.Material
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

data class PointedWorld(val world: World, val pos: BlockPos) {
  val stress: Int get() {
    if (!allowsFalling) return 0

    val stressMap = world.stressMap ?: return 0

    val below = move(Direction.DOWN)
    val belowStress = stressMap[below.pos] ?: if (below.allowsFallThrough) MAX_STRESS else 0

    val horizontalStress = Direction.Plane.HORIZONTAL
      .map { move(it) }
      .filter { it.allowsSupporting }
      .map { (stressMap[it.pos] ?: 0) + 1 }

    return (horizontalStress + belowStress).min()!!
  }

  fun move(direction: Direction): PointedWorld = copy(pos = pos.offset(direction))

  private val blockState: BlockState
    get() = world.getBlockState(pos)

  private val block: Block
    get() = blockState.block

  private val material: Material
    get() = blockState.material

  private val allowsFalling: Boolean
    get() = material.blocksMovement() &&
      !material.isLiquid &&
      block != Blocks.BEDROCK &&
      block !is FallingBlock &&
      block !is LeavesBlock

  private val allowsSupporting: Boolean
    get() = material.blocksMovement() &&
      !material.isLiquid &&
      block !is LeavesBlock

  val allowsFallThrough: Boolean
    get() = (!material.blocksMovement() || material.isLiquid) &&
      block !is CauldronBlock

  companion object {
    private const val MAX_STRESS = 50
  }
}
