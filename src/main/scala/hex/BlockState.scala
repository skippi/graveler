package hex

trait BlockState[A, M, W] {
  def hardness(a: A, world: W, pos: Vec3)(
      implicit ev: World[W, A]
  ): Float

  def material(a: A)(implicit ev: Material[M]): M
}

object BlockState {
  final class Ops[A, M, W](a: A)(implicit ev: BlockState[A, M, W]) {
    def hardness(world: W, pos: Vec3)(
        implicit ev1: World[W, A]
    ): Float = {
      ev.hardness(a, world, pos)
    }

    def material(implicit ev2: Material[M]): M = ev.material(a)
  }
}
