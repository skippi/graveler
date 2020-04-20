package graveler

import graveler.util.ForgeExtensions.toLazyOptional
import net.minecraft.util.Direction
import net.minecraft.world.IWorld
import net.minecraft.world.World
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.CapabilityInject
import net.minecraftforge.common.capabilities.ICapabilityProvider
import net.minecraftforge.common.util.LazyOptional

class SchedulerProvider : ICapabilityProvider {
    private val instance: Scheduler? = Capability.defaultInstance

    override fun <T> getCapability(
      cap: Capability<T>,
      side: Direction?
    ): LazyOptional<T> {
        return Capability.orEmpty(cap, instance.toLazyOptional)
    }

    companion object {
        @CapabilityInject(Scheduler::class)
        lateinit var Capability: Capability<Scheduler?>

        val IWorld.scheduler: Scheduler? get() = world.world.scheduler

        val World.scheduler: Scheduler? get() = getCapability(Capability, null).orElse(null)
    }
}
