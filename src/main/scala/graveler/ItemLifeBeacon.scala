package graveler

import graveler.CapabilityExtensions._
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.EnumAction
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.world.World

class ItemLifeBeacon extends Item {
  private val restoreCount = 5

  override def onItemRightClick(
      world: World,
      player: EntityPlayer,
      hand: EnumHand
  ): ActionResult[ItemStack] = {
    val itemStack = player.getHeldItem(hand)
    player.setActiveHand(hand)
    new ActionResult[ItemStack](EnumActionResult.SUCCESS, itemStack)
  }

  override def onItemUseFinish(
      stack: ItemStack,
      world: World,
      livingEntity: EntityLivingBase
  ): ItemStack = {
    livingEntity.getLivesOption foreach { lives =>
      lives.count += restoreCount
      lives.curseTicks = 0
    }

    stack.shrink(1)
    stack
  }

  override def getItemUseAction(stack: ItemStack): EnumAction = EnumAction.EAT
  override def getMaxItemUseDuration(stack: ItemStack): Int = 32

  setRegistryName("life_beacon")
  setUnlocalizedName(getRegistryName.toString)
  setCreativeTab(CreativeTabs.FOOD)
}
