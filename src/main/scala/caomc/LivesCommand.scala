package caomc

import caomc.CapabilityExtensions._
import net.minecraft.command.CommandBase
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.server.MinecraftServer

class LivesCommand extends CommandBase {
  @throws(classOf[CommandException])
  override def execute(
      server: MinecraftServer,
      sender: ICommandSender,
      args: Array[String]
  ): Unit = sender.getCommandSenderEntity match {
    case player: EntityPlayer =>
      player.getLivesOption foreach { lives =>
        sender.sendMessage(CaoLocale.createLivesStatusText(lives))
      }
    case _ =>
  }

  override def getName: String = "lives"
  override def getUsage(sender: ICommandSender): String =
    "commands.cao.lives.usage"
  override def checkPermission(
      server: MinecraftServer,
      sender: ICommandSender
  ): Boolean = true
}
