package object.rendering.scene;

import object.rendering.geometry.Vector3;
import object.rendering.graphics.Raster;

public class Camera extends SceneComponent {
  private static final double DEFAULT_SIZE = 1;

  private double size;
  private Raster raster;

  public Camera(Raster raster, Transform transform, double size) {
    super(transform);
    this.raster = raster;
    this.size = size;
  }

  public Camera(Raster raster, Transform transform) {
    this(raster, transform, DEFAULT_SIZE);
  }

  public double size() {
    return size;
  }

  public double fieldOfView() {
    return 2 * Math.atan(size);
  }

  public double aspectRatio() {
    return (double) raster.getWidth() / raster.getHeight();
  }

  public void lookAt(Vector3 from, Vector3 to) {
    Vector3 forward = from.subtract(to).normalize();
    Vector3 right = Vector3.UP.crossProduct(forward);
    Vector3 up = forward.crossProduct(right);
    transform.setMatrix(right, up, forward, from);
  }

  public Raster raster() {
    return raster;
  }

}
