package graveler

import graveler.util.stressMap
import net.minecraft.block.*
import net.minecraft.block.material.Material
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

data class PointedWorld(val world: World, val pos: BlockPos) {
  val stress: Int get() {
    if (!isStressAware) return 0

    val stressMap = world.stressMap ?: return 0
    val below = move(Direction.DOWN)
    val belowStress = when {
      below.isStressAware -> stressMap[below.pos] ?: 0
      below.isPermanentlyStable -> 0
      else -> MAX_STRESS
    }

    val horizontalStress = Direction.Plane.HORIZONTAL
      .map { move(it) }
      .map {
        when {
          it.isStressAware -> stressMap[it.pos] ?: 0
          it.isPermanentlyStable -> 0
          else -> MAX_STRESS
        }
      }
      .map { it + 1 }

    return (horizontalStress + belowStress).min()!!
  }

  val isStressAware: Boolean
    get() = !blockState.isAir(world, pos) && !isPermanentlyStable

  val isPermanentlyStable: Boolean
    get() = block == Blocks.BEDROCK || material.isLiquid

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
