package object.rendering.renders.render.material;

import lombok.Getter;
import object.rendering.geometry.Ray;
import object.rendering.renders.InteractionService;
import object.rendering.scene.Scene;

import java.awt.*;
import java.util.function.BiFunction;

public enum MaterialType {
    MATT((ray, scene) -> {
        return ColorFetcher.getMattColor(ray, scene);
    }),
    REFLECTED((ray, scene) -> {
        return ColorFetcher.getReflectedColor(ray, scene, 0);
    });

    @Getter
    private BiFunction<Ray, Scene, Color> getColorLambda;

    MaterialType(BiFunction<Ray, Scene, Color> getColorLambda) {
        this.getColorLambda = getColorLambda;
    }
}
