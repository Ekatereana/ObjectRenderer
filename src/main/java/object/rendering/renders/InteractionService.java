package object.rendering.renders;

import object.rendering.geometry.Ray;
import object.rendering.scene.Scene;
import object.rendering.scene.SceneComponent;

public abstract class InteractionService {
    public static SceneComponent findInteraction(Ray primaryRay, Scene scene, double minDistance) {
        SceneComponent intersection = null;
        for (SceneComponent obj : scene.getSceneObjects()) {
            if (obj.intersect(primaryRay)) {
                double distance = primaryRay.getScale();
                if (distance <= minDistance) {
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

    public static SceneComponent findInteraction(Ray ray, Scene scene) {
       return  findInteraction(ray, scene, Double.POSITIVE_INFINITY);
    }
}
