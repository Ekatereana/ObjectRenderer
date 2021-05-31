package object.rendering;

import object.rendering.geometry.Vector3;

public interface RenderConfig {
    int SCREEN_WIDTH = 640;
    int SCREEN_HEIGHT = 640;

    Vector3 objTransform1 = new Vector3(-1, 0, 0);
    Vector3 objTransform11 = new Vector3(0, 1, 0);
    Vector3 objTransform2 = new Vector3(10, 10, 10);
    Vector3 objTransform21 = new Vector3(10, 10, 10);

    Vector3 lightDir = new Vector3(20, -5, 7);
    Vector3 secondLightDir = new Vector3(10000, 10000, 10000);
    Vector3 cameraDir = new Vector3(10, -10, 0);

}
