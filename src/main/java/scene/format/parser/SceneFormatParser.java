package scene.format.parser;


import com.github.computergraphicscourse.sceneformat.SceneFormat.*;
import com.github.computergraphicscourse.sceneformat.SceneFormatIO;
import com.github.computergraphicscourse.sceneformat.SceneIO;
import object.rendering.scene.SceneComponent;

import java.io.IOException;


public class SceneFormatParser {

    private static final SceneIO sceneIO = new SceneFormatIO();
    public static void main(String[] args) throws IOException {
       Scene scene = Scene.newBuilder()
                .setVersion(1)
                .addSceneObjects(
                        SceneObject.newBuilder()
                                .setId(0)
                                .setTransform(Transform.newBuilder().setPosition(Vector3.newBuilder().setX(1).setY(1).setZ(1)))
                                .setMaterial(Material.newBuilder().setSolid(SolidMaterial.newBuilder()))
                                .setMeshedObject(MeshedObject.newBuilder())
                )
                .addCameras(
                        Camera.newBuilder()
                                .setId(0)
                                .setTransform(Transform.newBuilder().setPosition(Vector3.newBuilder().setX(1.01).setY(2.76).setZ(3.0)))
                                .setPerspective(PerspectiveCamera.newBuilder().setFov(60))
                )
                .build();

        sceneIO.save(scene, "example_binary.cowscene");
        sceneIO.saveAsJson(scene, "example_json.cowscene");

        Scene readResultBinary = sceneIO.read("example_binary.cowscene");
        System.out.printf("Camera X is %f when reading binary%n", readResultBinary.getCameras(0).getTransform().getPosition().getX());

        Scene readResultJson = sceneIO.read("example_json.cowscene");
        System.out.printf("Camera X is %f when reading json%n", readResultJson.getCameras(0).getTransform().getPosition().getX());
    }
}
