package object.rendering.scene;


import object.rendering.geometry.Ray;
import object.rendering.geometry.Vector3;
import object.rendering.graphics.Raster;
import lombok.Getter;

import java.awt.*;

public class BasicRaytracingRender implements SceneRender {

  private static final boolean DEFAULT_APPLY_SHADING = true;

  @Getter
  private Raster raster;
  private boolean applyShading = DEFAULT_APPLY_SHADING;

  public BasicRaytracingRender(Scene scene) {
    this(scene.getCamera().orElseThrow());
  }

  public BasicRaytracingRender(Camera camera) {
    this(camera.raster());
  }

  public BasicRaytracingRender(Raster raster) {
    this.raster = raster;
  }

  @Override
  public void render(Scene scene) {
    Camera camera = scene.getCamera().orElseThrow();
    Transform transform = camera.getTransform();
    Vector3 pos = camera.getTransform().position();
    int width = raster.getWidth();
    int height = raster.getHeight();
    double aspectRatio = camera.aspectRatio();
    double fieldOfView = Math.tan(camera.fieldOfView() / 2);
    Ray primaryRay = new Ray(pos, Vector3.ZERO);
    primaryRay.setOrigin(transform.applyPoint(transform.position()));
    for (int y = 0; y < height; y++) {
      double normalizedY = 1 - 2 * (y + 0.5) / height;
      double cameraY =  normalizedY * fieldOfView;
      for (int x = 0; x < width; x++) {
        double normalizedX = 2 * (x + 0.5) / width - 1;
        double cameraX = normalizedX * aspectRatio * fieldOfView;
        Vector3 direction = new Vector3(cameraX, cameraY, -1).normalize();
        primaryRay.setDirection(transform.applyVector(direction).normalize());
        Color color = getColor(primaryRay, scene);
        raster.setPixel(x, y,
                (byte) color.getRed(),
                (byte) color.getGreen(),
                (byte) color.getBlue());
      }
    }
  }

  Color getColor(Ray primaryRay, Scene scene) {
    SceneComponent obj = findInteraction(primaryRay, scene);

    if (obj == null) {
      return scene.getBackgroundColor();
    }

    if (!applyShading) {
      return obj.getMesh().color();
    }

    double c = 0;

    for (Light light : scene.getLights()) {
      Vector3 hitNormal = obj.getNormal(primaryRay.getPoint());
      double bias = 0.001;
      Ray nRay = new Ray(primaryRay.getPoint().add(hitNormal.multiply(bias)), light.getTransform().rotation().multiply(-1));
      boolean visible = findInteraction(nRay, scene, light.getMaxDist(primaryRay.getPoint())) == null;
      if (visible) {
        c += obj.albedo() / Math.PI * light.illuminate(hitNormal, light.getTransform().position().distance(obj.getTransform().position()));
      }
    }

    if (c > 1) {
      c = 1;
    }

    return new Color((int) (obj.getMesh().color().getRed() * c), (int) (obj.getMesh().color().getGreen() * c), (int) (obj.getMesh().color().getBlue() * c));
  }

  SceneComponent findInteraction(Ray primaryRay, Scene scene) {
    return findInteraction(primaryRay, scene, Double.POSITIVE_INFINITY);
  }

  SceneComponent findInteraction(Ray primaryRay, Scene scene, double minDistance) {
    SceneComponent intersection = null;
    for (SceneComponent obj : scene.getSceneObjects()) {
      if (obj.intersect(primaryRay)) {
        double distance = primaryRay.getScale();
        if (distance < minDistance) {
          minDistance = distance;
          intersection = obj;
        }
      }
    }
    if (intersection != null) {
      primaryRay.setScale(minDistance);
    }
    return intersection;
  }


}
