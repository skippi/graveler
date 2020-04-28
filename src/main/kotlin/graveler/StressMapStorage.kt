package graveler

import net.minecraft.nbt.*
import net.minecraft.util.Direction
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.Capability.IStorage

class StressMapStorage : IStorage<StressMap> {
  override fun writeNBT(
    capability: Capability<StressMap>,
    instance: StressMap,
    side: Direction?
  ): INBT {
    val list = ListNBT()
    instance.stresses.forEach { (pos, stress) ->
      val pair = CompoundNBT()
      pair.put("pos", NBTUtil.writeBlockPos(pos))
      pair.put("stress", IntNBT.valueOf(stress))
      list.add(pair)
    }

    return list
  }

  override fun readNBT(
    capability: Capability<StressMap>,
    instance: StressMap,
    side: Direction?,
    nbt: INBT
  ) {
    val list = nbt as? ListNBT ?: return
    list.forEach {
      val pair = it as? CompoundNBT ?: return@forEach
      val pos = NBTUtil.readBlockPos(pair.getCompound("pos"))
      val stress = pair.getInt("stress")

      instance.stresses[pos] = stress
    }
  }

  companion object {
    val RESOURCE = ResourceLocation(GravelerMod.ModId, "stress")
  }
}
