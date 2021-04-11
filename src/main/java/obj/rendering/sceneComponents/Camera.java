package obj.rendering.sceneComponents;

import obj.rendering.geometry.Transformation;
import obj.rendering.geometry.Vector3;
import obj.rendering.graphics.RasterToImageMapper;

import java.awt.image.Raster;

public class Camera extends SceneComponent {
    private static final double DEFAULT_SIZE = 1;

    private double size;
    private RasterToImageMapper raster;

    public Camera(RasterToImageMapper raster, Transformation transform, double size) {
        super(transform);
        this.raster = raster;
        this.size = size;
    }

    public Camera(RasterToImageMapper raster, Transformation transform) {
        this(raster, transform, DEFAULT_SIZE);
    }

    public double size() {
        return size;
    }

    /**
     * Calculate camera field of view.
     * Assume that plane always at distance 1 and
     * size represents half of plane height
     * tangents of half of the field of view is equal to size.
     *
     * @return field of view in radians
     */
    public double fieldOfView() {
        return 2 * Math.atan(size);
    }

    public double aspectRatio() {
        return (double) raster.getWidth() / raster.getHeight();
    }

    /**
     * Set position of camera to from and and orientation to face to.
     *
     * @param from position of camera
     * @param to   point to look at
     */
    public void lookAt(Vector3 from, Vector3 to) {
        Vector3 forward = from.subtract(to).normalize();
        Vector3 right = Vector3.UP.crossProduct(forward);
        Vector3 up = forward.crossProduct(right);
        transform.setMatrix(right, up, forward, from);
    }

    public RasterToImageMapper raster() {
        return raster;
    }
}
