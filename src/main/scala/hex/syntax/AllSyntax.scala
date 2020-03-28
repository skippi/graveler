package hex.syntax

import hex._

trait AllSyntax {
  implicit def blockStateOps[A](a: A)(
      implicit ev: BlockState[A, _, _]
  ): BlockState.Ops[A, _, _] = {
    new BlockState.Ops(a)
  }

  implicit def materialOps[A: Material](a: A): Material.Ops[A] = {
    new Material.Ops(a)
  }

  implicit def worldOps[A](a: A)(implicit ev: World[A, _]): World.Ops[A, _] = {
    new World.Ops(a)
  }
}
