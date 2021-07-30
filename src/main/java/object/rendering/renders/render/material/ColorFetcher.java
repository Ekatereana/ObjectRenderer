package object.rendering.renders.render.material;

import object.rendering.geometry.Ray;
import object.rendering.geometry.Vector3;
import object.rendering.renders.InteractionService;
import object.rendering.scene.Light;
import object.rendering.scene.Scene;
import object.rendering.scene.SceneComponent;

import java.awt.*;

public abstract class ColorFetcher {

    private static final boolean DEFAULT_APPLY_SHADING = true;
    private static boolean applyShading = DEFAULT_APPLY_SHADING;
    private static boolean quitShading = false;
    private static double bias = 0.7;

    public static Color performGeneralChecks(SceneComponent obj) {
        if (obj == null) {
            return Color.WHITE;
        }

        if (!applyShading || quitShading) {
            return obj.getMesh().color();
        }

        return null;
    }


    public static Color getNoShaderColor(Ray ray, Scene scene) {
        quitShading = true;
        Color color = getMattColor(ray, scene);
        quitShading = false;
        return color;
    }

    public static Color getMattColor(
            Ray ray,
            Scene scene
    ) {
        SceneComponent obj = InteractionService.findInteraction(ray, scene);
        Color resultColor = performGeneralChecks(obj);
        if (resultColor == null) {
            double c = 0.0;
            for (Light light : scene.getLights()) {
                Vector3 hitNormal = obj.getNormal(ray.getPoint());

                Ray nRay = new Ray(ray.getPoint().add(hitNormal.multiply(bias)), light.getTransform().rotation().multiply(1));
                boolean visible = InteractionService.findInteraction(nRay, scene, light.getMaxDist(ray.getPoint())) == null;
                if (visible) {
                    c += calculateShading(light, hitNormal, obj);
                }
            }

            if (c > 1) {
                c = 1;
            }
            return mapColor(
                    obj.getMesh().color().getRed(),
                    obj.getMesh().color().getGreen(),
                    obj.getMesh().color().getBlue(),
                    c);
        }

        return resultColor;
    }

    public static Color getReflectedColor(
            Ray ray,
            Scene scene,
            int depth
    ) {
        if (depth > scene.getSceneObjects().size()) return Color.WHITE;
        SceneComponent obj = InteractionService.findInteraction(ray, scene);
        Color resultColor = performGeneralChecks(obj);
        if (resultColor == null) {
            Vector3 hitNormal = obj.getNormal(ray.getPoint());
            double c = 0.0;
            switch (obj.getMesh().getMaterialType()) {
                case REFLECTED:
                    Vector3 reflectDir;
                    reflectDir = obj.getHalfwayVector(hitNormal, ray.getDirection());
                    Ray nRay = new Ray(hitNormal.multiply(bias).add(ray.getPoint()), reflectDir);
                    resultColor = getReflectedColor(
                            nRay,
                            scene,
                            depth + 1);

                    for (Light light : scene.getLights()) {
                        c += calculateShading(light, hitNormal, obj);
                    }

                    resultColor = mapColor(
                            resultColor.getRed(),
                            resultColor.getGreen(),
                            resultColor.getBlue(),
                            c*0.8);

                    resultColor = obj.getMesh().color() != null ?
                            blendColors(obj.getMesh().color(), resultColor)
                            : resultColor;
                    break;
                case MATT:
                    resultColor = getMattColor(ray, scene);
                    break;
            }

        }

        return resultColor;
    }

    private static Color mapColor(int red, int green, int blue, double c) {
        return new Color(
                Math.min((int) (red * (c)), 255),
                Math.min((int) (green * (c)), 255),
                Math.min((int) (blue * (c)), 255));
    }

    private static double calculateShading(Light light, Vector3 hitNormal, SceneComponent obj) {
        return (obj.albedo() /
                Math.PI * light
                .illuminate(
                        hitNormal,
                        light
                                .getTransform()
                                .position()
                                .distance(obj.getTransform().position())));
    }


    private static Color blendColors(Color c0, Color c1) {
        double totalAlpha = c0.getAlpha() + c1.getAlpha();
        double weight0 = c0.getAlpha() / totalAlpha;
        double weight1 = c1.getAlpha() / totalAlpha;

        double r = weight0 * c0.getRed() + weight1 * c1.getRed();
        double g = weight0 * c0.getGreen() + weight1 * c1.getGreen();
        double b = weight0 * c0.getBlue() + weight1 * c1.getBlue();
        double a = Math.max(c0.getAlpha(), c1.getAlpha());

        return new Color((int) r, (int) g, (int) b, (int) a);
    }


}
