package obj.rendering.sceneComponents;


import obj.rendering.geometry.Transformation;
import obj.rendering.geometry.Vector3;

public class DistantLight extends LightSource {

  Vector3 direction;

  public DistantLight(Transformation transform, double intensity) {
    super(transform, intensity);
  }

  @Override
  public double illuminate(Vector3 normal, double distance) {
    return Math.max(intensity() * normal.dotProduct(getTransform().applyVector(Vector3.FORWARD.multiply(1)).normalize()), 0);
  }

  public double getMaxDist(Vector3 point) {
    return Double.POSITIVE_INFINITY;
  }
}
