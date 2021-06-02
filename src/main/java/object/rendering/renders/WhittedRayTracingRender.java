package object.rendering.renders;

import com.github.computergraphicscourse.sceneformat.SceneFormat;
import object.rendering.geometry.Ray;
import object.rendering.geometry.Vector3;
import object.rendering.graphics.Raster;
import object.rendering.renders.render.material.ColorFetcher;
import object.rendering.renders.render.material.MaterialType;
import object.rendering.scene.Camera;
import object.rendering.scene.Light;
import object.rendering.scene.Scene;
import object.rendering.scene.SceneComponent;

import java.awt.*;

public class WhittedRayTracingRender extends BasicRaytracingRender {

    public WhittedRayTracingRender(Scene scene) {
        super(scene);
    }

    public WhittedRayTracingRender(Camera camera) {
        super(camera);
    }

    public WhittedRayTracingRender(Raster raster) {
        super(raster);
    }

    @Override
    Color getColor(Ray primaryRay, Scene scene) {
        SceneComponent obj = InteractionService.findInteraction(primaryRay, scene);
        Color result = ColorFetcher.performGeneralChecks(obj);
        return result == null ?
                obj.getMesh().getMaterialType().getGetColorLambda().apply(primaryRay, scene)
                : result;
    }
}
