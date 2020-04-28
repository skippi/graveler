package graveler.action

import graveler.util.PointedWorld
import graveler.util.pointedAt
import net.minecraft.block.Blocks
import net.minecraft.block.CauldronBlock
import net.minecraft.entity.item.FallingBlockEntity
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

data class Fall(val pos: BlockPos) : Action {
  override val weight: Double = 1 / 128.0

  override fun apply(context: ActionContext) {
    context.world.fallAt(pos)
  }

  private fun World.fallAt(pos: BlockPos) {
    if (!isAreaLoaded(pos, 32)) {
      forceInstantFallAt(pos)
      return
    }

    if (isRemote) {
      return
    }

    forceFallAt(pos)
  }

  private fun World.forceFallAt(pos: BlockPos) {
    addEntity(
      FallingBlockEntity(
        this,
        pos.x + 0.5,
        pos.y.toDouble(),
        pos.z + 0.5,
        getBlockState(pos)))
  }

  private fun World.forceInstantFallAt(pos: BlockPos) {
    val state = getBlockState(pos)

    setBlockState(pos, Blocks.AIR.defaultState)

    var below = this.pointedAt(pos).move(Direction.DOWN)
    while (below.allowsFallThrough && below.pos.y > 0) {
      below = below.move(Direction.DOWN)
    }

    if (below.pos.y > 0) {
      // Forge: Fix loss of state information during world gen.
      setBlockState(below.pos.up(), state)
    }
  }

  private val PointedWorld.allowsFallThrough: Boolean
    get() = (!material.blocksMovement() || material.isLiquid) &&
      block !is CauldronBlock
}
