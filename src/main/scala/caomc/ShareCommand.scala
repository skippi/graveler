package caomc

import caomc.CapabilityExtensions._
import net.minecraft.command.CommandBase
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.server.MinecraftServer
import scala.math._

class ShareCommand extends CommandBase {
  @throws(classOf[CommandException])
  override def execute(
      server: MinecraftServer,
      sender: ICommandSender,
      args: Array[String]
  ): Unit = {
    sender.getCommandSenderEntity match {
      case source: EntityPlayer =>
        if (args.size < 2) {
          throw new CommandException("Usage: /cao share @user_name @amount")
        }

        val targetName = args(0)
        val target =
          server.getPlayerList.getPlayers.stream
            .filter(p => p.getName == targetName)
            .findFirst
            .orElse(null)
        if (target == null) {
          throw new CommandException("Invalid @username")
        }

        val sourceLives = source.getLivesOption.get
        val targetLives = target.getLivesOption.get

        try {
          val requestedAmount = args(1).toInt
          val amount = min(requestedAmount, sourceLives.count)

          targetLives.count += amount
          sourceLives.count -= amount

          target.sendMessage(
            CaoLocale.createModText(
              s"${source.getName} shared ${amount} lives with you."
            )
          )
          sender.sendMessage(
            CaoLocale.createModText(
              s"You shared ${amount} lives with ${target.getName}."
            )
          )
        } catch {
          case _: NumberFormatException =>
            throw new CommandException("Invalid integer @amount")
        }
      case _ =>
        throw new CommandException(
          "You cannot use this command on a non-player"
        )
    }
  }

  override def getName: String = "share"
  override def getUsage(sender: ICommandSender): String =
    "commands.cao.share.usage"
  override def checkPermission(
      server: MinecraftServer,
      sender: ICommandSender
  ): Boolean = true
}
