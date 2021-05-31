package object.rendering.renders.render.material;

import object.rendering.geometry.Ray;
import object.rendering.geometry.Vector3;
import object.rendering.renders.InteractionService;
import object.rendering.scene.Light;
import object.rendering.scene.Scene;
import object.rendering.scene.SceneComponent;

import java.awt.*;
import java.util.Comparator;

public abstract class ColorFetcher {

    private static final boolean DEFAULT_APPLY_SHADING = true;
    private static boolean applyShading = DEFAULT_APPLY_SHADING;
    private static double bias = 0.7;

    public static Color performGeneralChecks(SceneComponent obj) {
        if (obj == null) {
            return Color.WHITE;
        }

        if (!applyShading) {
            return obj.getMesh().color();
        }

        return null;
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
                    c += (obj.albedo() / Math.PI * light.illuminate(hitNormal, light.getTransform().position().distance(obj.getTransform().position())));
                }
            }

            if (c > 1) {
                c = 1;
            }

            return new Color(
                    (int) (obj.getMesh().color().getRed() * c),
                    (int) (obj.getMesh().color().getGreen() * c),
                    (int) (obj.getMesh().color().getBlue() * c));


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
        System.out.println(depth + " | " + obj);

        Color resultColor = performGeneralChecks(obj);
        if (resultColor == null) {
            double c = 0.0;
            Vector3 hitNormal = obj.getNormal(ray.getPoint());
            switch (obj.getMesh().getMaterialType()) {
                case REFLECTED:
                    Vector3 reflectDir;
                    Light superLight = scene.getLights().stream().max((o1, o2) -> o1.intensity() > o2.intensity() ? 1 : 0).get();
                    reflectDir = obj.getHalfwayVector(hitNormal, superLight.direction());
                    Ray nRay = new Ray(hitNormal, reflectDir.multiply(bias));
                    resultColor = getReflectedColor(nRay, scene, depth + 1);
                    for (Light light : scene.getLights()) {
                        c += (obj.albedo() /
                                Math.PI * light
                                .illuminate(
                                        hitNormal,
                                        light
                                                .getTransform()
                                                .position()
                                                .distance(obj.getTransform().position())));

                    }

                    if (c > 1) {
                        c = 1;
                    }
                    resultColor = new Color(
                            (int) (resultColor.getRed() * c),
                            (int) (resultColor.getGreen() * c),
                            (int) (resultColor.getBlue() * c));
                    break;
                case MATT:
                    resultColor = obj.getMesh().color();
                    break;
            }

        }

        return resultColor;
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


    private static Color performReflection(Ray ray, SceneComponent obj, int depth) {
        Color resultColor = null;
        switch (depth) {
            case 0:
                break;
            case 1:
                break;
            default:
                break;
        }
        return resultColor;

    }
}
