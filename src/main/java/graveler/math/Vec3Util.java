package graveler.math;

import net.minecraft.util.math.Vec3i;

public class Vec3Util {
  public static Vec3i add(final Vec3i left, final Vec3i right) {
    return new Vec3i(
      left.getX() + right.getX(),
      left.getY() + right.getY(),
      left.getZ() + right.getZ()
    );
  }

  public static Vec3i divide(final Vec3i vec, final double scalar) {
    return new Vec3i(vec.getX() / scalar, vec.getY() / scalar, vec.getZ() / scalar);
  }

  public static Vec3i subtract(final Vec3i left, final Vec3i right) {
    return add(left, negate(right));
  }

  public static int dot(final Vec3i left, final Vec3i right) {
    final int x = left.getX() * right.getX();
    final int y = left.getY() * right.getY();
    final int z = left.getZ() * right.getZ();

    return x + y + z;
  }

  public static double norm(final Vec3i vec) {
    return Math.sqrt(dot(vec, vec));
  }

  public static Vec3i negate(final Vec3i vec) {
    return new Vec3i(-vec.getX(), -vec.getY(), -vec.getZ());
  }
}