package object.rendering.renders;


import object.rendering.geometry.Ray;
import object.rendering.geometry.Vector3;
import object.rendering.graphics.Raster;
import lombok.Getter;
import object.rendering.renders.render.material.MaterialType;
import object.rendering.scene.*;

import java.awt.*;

public class BasicRaytracingRender implements AbstractSceneRender {


  @Getter
  private Raster raster;

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
    return MaterialType.MATT.getGetColorLambda().apply(primaryRay, scene);
  }




}
