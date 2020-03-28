package hex

trait Material[A] {
  def isLiquid(a: A): Boolean
}

object Material {
  final class Ops[A](a: A)(implicit ev: Material[A]) {
    def isLiquid: Boolean = ev.isLiquid(a)
  }
}