package hex

trait World[A, B] {
  def getBlockState(a: A, pos: Vec3)(implicit ev: BlockState[B, _]): B
  def setBlockState(a: A, pos: Vec3, state: B)(
      implicit ev: BlockState[B, _]
  ): Unit
}
