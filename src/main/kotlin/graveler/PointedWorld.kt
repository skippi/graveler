package graveler

import graveler.util.stressMap
import net.minecraft.block.*
import net.minecraft.block.material.Material
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.chunk.Chunk

data class PointedWorld(val world: World, val pos: BlockPos) {
  fun getNewStress(): Int {
    if (!isStressAware) return 0

    val below = move(Direction.DOWN)
    val horizontalStress = Direction.Plane.HORIZONTAL
      .map { move(it).stress + 1 }

    return (horizontalStress + below.stress).min()!!
  }

  fun clearStress() {
    chunk?.stressMap?.stresses?.remove(pos)
  }

  var stress: Int
    get() = when {
      isStressAware -> chunk?.stressMap?.get(pos) ?: 0
      isPermanentlyStable -> 0
      else -> MAX_STRESS
    }
    set(value) {
      when {
        isStressAware -> chunk?.stressMap?.set(pos, value)
      }
    }

  private val chunk: Chunk?
    get() = world.getChunkAt(pos)

  val isStressAware: Boolean
    get() = !blockState.isAir(world, pos) && !isPermanentlyStable

  private val isPermanentlyStable: Boolean
    get() = block == Blocks.BEDROCK || material.isLiquid

  fun move(direction: Direction): PointedWorld = copy(pos = pos.offset(direction))

  private val blockState: BlockState
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
