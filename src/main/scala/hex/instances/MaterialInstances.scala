package hex.instances

import hex.Material

import net.minecraft.block.material.{Material => ForgeMaterial}

trait MaterialInstances {
  implicit val ForgeWorldIsWorld = new Material[ForgeMaterial] {
    def isLiquid(a: ForgeMaterial): Boolean = a.isLiquid
  }
}
