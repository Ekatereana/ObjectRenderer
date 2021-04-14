package object.rendering.io.obj;


import object.rendering.geometry.Triangulation;
import object.rendering.geometry.Vector3;
import object.rendering.object.Triangle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class ParseObjFile implements ObjReader {
    /**
     * String indicators of parameter in .obj file.
     */
    private final static String OBJ_VERTEX_NORMAL = "vn";
    private final static String OBJ_VERTEX = "v";
    private final static String OBJ_FACE = "f";
    private List<Vector3> verticesNormals = new ArrayList<>();
    private List<Vector3> verticesGeometry = new ArrayList<>();
    private List<Polygon> polygons = new ArrayList<>();
    private Triangulation triangulation = new Triangulation();

    public void load(InputStream is) throws IOException {
        InputStreamReader fileReader;
        BufferedReader bufferedReader;
        fileReader = new InputStreamReader(is, StandardCharsets.UTF_8);
        bufferedReader = new BufferedReader(fileReader);
        String line;

        // read .obj file
        while ((line = bufferedReader.readLine()) != null) {
            line = line.trim();

            if (line.isEmpty()) {
                continue;
            }

            // cases to process
            if (line.startsWith(OBJ_VERTEX_NORMAL)) {
                processVertexNormal(line);
                // add vertex with some coordinates
            } else if (line.startsWith(OBJ_VERTEX)) {
                processVertex(line);
            } else if (line.startsWith(OBJ_FACE)) {
                processFace(line);
            }
        }
        bufferedReader.close();
    }

    public List<Triangle> getPolygons() {
        return triangulation.splitPolygons(polygons);
    }
    /**
     * Create vertex( just coordinates of each one) from data of file.
     * @param line
     */
    private void processVertex(String line) {
        String[] values = line.replaceAll("\\s+", " ").split(" ");
        double[] result = new double[3];
        for (int i = 1; i < values.length; i++) {
            result[i - 1] = Double.parseDouble(values[i]);
        }
        verticesGeometry.add(new Vector3(result[0], result[1], result[2]));
    }
    /**
     * Create normal-data of vertex
     * @param line
     */
    private void processVertexNormal(String line) {
        String[] values = line.replaceAll("\\s+", " ").split(" ");
        double[] result = new double[3];
        for (int i = 1; i < values.length; i++) {
            result[i - 1] = Double.parseDouble(values[i]);
        }
        verticesNormals.add(new Vector3(result[0], result[1], result[2]));
    }


    private void processFace(String line) {
        line = line.substring(OBJ_FACE.length()).trim();
        polygons.add(parseCodeLine(line));
    }

    private  Polygon parseCodeLine(String list) {
        if (list == null) {
            return null;
        }
        if (list.equals("")) {
            return null;
        }
        String[] vertexStrings = list.split(" ");
        List<Vertex> vertices = new ArrayList<>();

        for (int i = 0; i < vertexStrings.length; i++) {
            String[] temp = vertexStrings[i].split("/");
            Vector3 geometry = verticesGeometry.get(Integer.parseInt(temp[0]) - 1);
            int index = -1;
            if (temp.length >= 3) {
                index = Integer.parseInt(temp[2]);
            }
            Vector3 normal = index == -1 ? Vector3.ZERO : verticesNormals.get(index - 1);
            vertices.add(new Vertex(geometry, normal));
        }
        return new Polygon(vertices);
    }
}
