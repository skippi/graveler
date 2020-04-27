package graveler.util

import graveler.math.Bounds
import net.minecraft.block.*
import net.minecraft.entity.item.FallingBlockEntity
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3i
import net.minecraft.world.World

fun World.fallAt(pos: BlockPos) {
  val targetBounds = Bounds(pos, Vec3i(64, 64, 64))

  if (!isAreaLoaded(targetBounds)) {
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

private fun World.isAreaLoaded(bounds: Bounds): Boolean {
  return isAreaLoaded(BlockPos(bounds.min), BlockPos(bounds.max))
}
