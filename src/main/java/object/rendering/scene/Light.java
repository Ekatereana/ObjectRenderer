package object.rendering.scene;

import lombok.Getter;
import object.rendering.geometry.Vector3;

public abstract class Light extends SceneComponent {

  private double intensity;
  private Vector3 direction;

  public Light(Transform transform, double intensity, Vector3 direction) {
    super(transform);
    this.intensity = intensity;
    this.direction = direction;
  }

  public double intensity() {
    return intensity;
  }

  public Vector3 direction(){
    return direction;
  }

  public abstract double illuminate(Vector3 normal, double distance);

  public abstract double getMaxDist(Vector3 point);
}
