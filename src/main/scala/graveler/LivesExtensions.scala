package graveler

import graveler.CapabilityExtensions._
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World
import scala.jdk.CollectionConverters._

object LivesExtensions {
  implicit class ExtendedWorld(private val world: World) {
    def alivePlayers: Seq[EntityPlayer] = {
      world.playerEntities.asScala.filter { _.hasLives }.toSeq
    }
  }

  implicit class ExtendedEntity(private val entity: Entity) {
    def hasLives: Boolean = entity.getLivesOption forall { _.isAlive }
  }
}
