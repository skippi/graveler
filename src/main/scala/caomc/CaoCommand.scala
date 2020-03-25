package caomc

import net.minecraft.command.ICommandSender
import net.minecraft.server.MinecraftServer
import net.minecraftforge.server.command.CommandTreeBase

class CaoCommand extends CommandTreeBase {
  override def getName: String = "cao"
  override def getUsage(sender: ICommandSender): String = "commands.cao.usage"
  override def checkPermission(
      server: MinecraftServer,
      sender: ICommandSender
  ): Boolean = true

  this.addSubcommand(new LivesCommand)
  this.addSubcommand(new ShareCommand)
  this.addSubcommand(new StatusCommand)
}
