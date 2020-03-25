package caomc

import caomc.LivesExtensions._
import net.minecraft.world.World
import scala.collection.mutable.HashSet

class Game {
  var ticks = 0L
  var isActive = false
  var participants = new HashSet[String]

  def tick(world: World): Game = {
    if (isNewGame) {
      world.setWorldTime(0)
    }

    isActive = world.alivePlayers.nonEmpty

    if (isActive) {
      ticks += 1
    }

    world.getGameRules.setOrCreateGameRule(
      "doDaylightCycle",
      if (isActive) "true" else "false"
    )

    this
  }

  def isNewGame: Boolean = participants.isEmpty
}
