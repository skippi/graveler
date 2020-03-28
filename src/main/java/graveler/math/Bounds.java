package graveler.math;

import java.util.List;
import java.util.ArrayList;
import net.minecraft.util.math.Vec3i;

public class Bounds {
  private final Vec3i center;
  private final Vec3i size;

  public Bounds(final Vec3i center, final Vec3i size) {
    this.center = center;
    this.size = size;
  }

  public Vec3i getCenter() {
    return center;
  }

  public Vec3i getSize() {
    return size;
  }

  private Vec3i getExtent() {
    return Vec3Util.divide(size, 2);
  }

  public Vec3i getMax() {
    return Vec3Util.add(center, getExtent());
  }

  public Vec3i getMin() {
    return Vec3Util.subtract(center, getExtent());
  }

  public List<Vec3i> getPoints() {
    final List<Vec3i> result = new ArrayList<Vec3i>();

    final Vec3i min = getMin();
    final Vec3i max = getMax();

    for (int i = min.getX(); i <= max.getX(); ++i) {
      for (int j = min.getY(); j <= max.getY(); ++j) {
        for (int k = min.getZ(); k <= max.getZ(); ++j) {
          result.add(new Vec3i(i, j, k));
        }
      }
    }

    return result;
  }
}
