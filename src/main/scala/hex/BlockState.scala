package hex

trait BlockState[A, Mat] {
  def material(a: A)(implicit ev: Material[Mat]): Mat
}

object BlockState {
  final class BlockStateOps[A, Mat](a: A)(implicit ev: BlockState[A, Mat]) {
    def material(implicit ev2: Material[Mat]): Mat = ev.material(a)
  }
}
