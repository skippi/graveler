@file:Suppress("unused")

package graveler

import graveler.action.UpdateNeighborStress
import graveler.action.UpdateStress
import graveler.util.scheduler
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorld
import net.minecraft.world.World

object Mixins {
  @JvmStatic
  fun updateNeighbors(world: IWorld, pos: BlockPos) {
    if (world.isRemote) return

    world.world.scheduler?.schedule(UpdateNeighborStress(pos))
  }

  @JvmStatic
  fun onBlockAdded(world: World, pos: BlockPos) {
    if (world.isRemote) return

    world.scheduler?.schedule(UpdateStress(pos))
  }
}
