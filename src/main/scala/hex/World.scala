package hex

trait World[A, B] {
  def getBlockState(a: A, pos: Vec3)(implicit ev: BlockState[B, _]): B

  def setBlockState(a: A, pos: Vec3, state: B)(
      implicit ev: BlockState[B, _]
  ): Unit

  def isRemote(a: A): Boolean
}

object World {
  final class Ops[A, B](a: A)(implicit ev: World[A, B]) {
    def getBlockState(pos: Vec3)(
        implicit ev1: BlockState[B, _]
    ): B = {
      ev.getBlockState(a, pos)
    }

    def setBlockState(pos: Vec3, state: B)(
        implicit ev1: BlockState[B, _]
    ): Unit = {
      ev.setBlockState(a, pos, state)
    }

    def isRemote: Boolean = {
      ev.isRemote(a)
    }
  }
}
