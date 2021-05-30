package object.rendering.scene;


import object.rendering.geometry.Vector3;

public class DistantLight extends Light {

  Vector3 direction;

  public DistantLight(Transform transform, double intensity, Vector3 direction) {
    super(transform, intensity);
    this.direction = direction;
  }

  @Override
  public double illuminate(Vector3 normal, double distance) {
    return Math.max(intensity() * normal.dotProduct(getTransform().applyVector(direction).normalize()), 0);
  }

  public double getMaxDist(Vector3 point) {
    return Double.POSITIVE_INFINITY;
  }
}
