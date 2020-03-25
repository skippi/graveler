package caomc

import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.item.Item
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder

@ObjectHolder(CaoMC.ModId)
@Mod.EventBusSubscriber(modid = CaoMC.ModId)
object CaoItems {
  final val LifeBeacon = new ItemLifeBeacon

  @SubscribeEvent
  def registerItems(event: RegistryEvent.Register[Item]): Unit = {
    event.getRegistry.register(LifeBeacon)
  }

  @SubscribeEvent
  def registerRenders(event: ModelRegistryEvent): Unit = {
    ModelLoader.setCustomModelResourceLocation(
      LifeBeacon,
      0,
      new ModelResourceLocation(LifeBeacon.getRegistryName, "inventory")
    )
  }
}
