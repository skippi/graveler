package graveler

import graveler.CapabilityExtensions._
import net.minecraft.command.CommandBase
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import net.minecraft.server.MinecraftServer
import scala.jdk.CollectionConverters._

class StatusCommand extends CommandBase {
  @throws(classOf[CommandException])
  override def execute(
      server: MinecraftServer,
      sender: ICommandSender,
      args: Array[String]
  ): Unit = {
    val maxTicks: Long = server.worlds.toSeq
      .flatMap(_.getGameOption)
      .map(_.ticks)
      .maxOption
      .getOrElse(0L)

    val aliveCount = server.getPlayerList.getPlayers.asScala
      .flatMap(_.getLivesOption)
      .filter(_.isAlive)
      .size

    sender.sendMessage(CaoLocale.createGameStatusText(aliveCount, maxTicks))
  }

  override def getName: String = "status"
  override def getUsage(sender: ICommandSender): String =
    "commands.cao.status.usage"
  override def checkPermission(
      server: MinecraftServer,
      sender: ICommandSender
  ) = true
}
