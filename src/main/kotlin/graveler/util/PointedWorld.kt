package graveler.util

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.material.Material
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.chunk.Chunk

data class PointedWorld(val world: World, val pos: BlockPos) {
  val chunk: Chunk?
    get() = world.getChunkAt(pos)

  fun move(direction: Direction): PointedWorld = copy(pos = pos.offset(direction))

  val blockState: BlockState
    get() = world.getBlockState(pos)

  val block: Block
    get() = blockState.block

  val material: Material
    get() = blockState.material
}
