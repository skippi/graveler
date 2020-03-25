package caomc

import caomc.math.BoundedArea
import caomc.physics.PhysicsExtensions._
import net.minecraft.entity.Entity
import net.minecraft.util.math.BlockPos
import scala.util.Random

class Aura {
  import Aura._

  var effect: Effect = None()
  var rollCount: Int = 0

  def radius: Int = effect match {
    case Arid() => 1
    case _      => 0
  }

  def roll: Aura = {
    this.effect = Effect.random
    rollCount += 1

    this
  }

  def tick(entity: Entity): Aura = {
    val world = entity.getEntityWorld
    if (world.isRemote) {
      return this
    }

    val targetArea = BoundedArea.cube(entity.getPosition, 1)
    val blocksInArea = targetArea.points map { pos =>
      new BlockPos(pos)
    }

    effect match {
      case Arid() =>
        blocksInArea
          .filter { world.getBlockState(_).isLiquid }
          .foreach { world.setBlockToAir(_) }
      case _ =>
    }

    this
  }
}

object Aura {
  // Something weird happens with case objects on match expressions, which is
  // why case classes are used here
  sealed trait Effect {
    def name: String = this.getClass.getSimpleName.toLowerCase
  }
  case class None() extends Effect
  case class Arid() extends Effect

  object Effect {
    final val Rng = new Random(0)

    def parse(str: String): Effect = str match {
      case "arid" => Arid()
      case _      => None()
    }

    def random: Effect = {
      val roll = Rng.nextDouble

      if (roll < 0.05)
        Arid()
      else None()
    }
  }
}
