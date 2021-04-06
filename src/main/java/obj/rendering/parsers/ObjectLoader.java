package obj.rendering.parsers;

import obj.rendering.abstracts.Polygon;
import obj.rendering.abstracts.Vertex;
import obj.rendering.geometry.Vector3;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ObjectLoader {

    private final String VERTEX_PREFIX = "v";
    private final String FACE_PREFIX = "f";
    private final String NORMAL_PREFIX = "vn";
    private final String TEXTURE_PREFIX = "vt";
    private final String SPACE_PREFIX = "vp";
    private List<Vector3> vertices;
    private List<Vector3> normals;
    private List<Polygon> polygons;
    private Map<String, Consumer<String>> lineParser;

    public ObjectLoader() {
        lineParser = new HashMap();
        lineParser.put(VERTEX_PREFIX, this::processVertex);
        lineParser.put(FACE_PREFIX, this::processFace);
        lineParser.put(NORMAL_PREFIX, this::processNormal);
//        not implemented yet
        lineParser.put(TEXTURE_PREFIX, (line) -> {});
        lineParser.put(SPACE_PREFIX, (line) -> {});

        vertices = new ArrayList<>();
        polygons = new ArrayList<>();
        normals = new ArrayList<>();
    }

    public void load(String fileName) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
        lines.stream().filter(line -> !(line.isEmpty() || line.startsWith("#"))).forEach(
                l -> {
                    lineParser.get(l.split(" ")[0]).accept(l);
                }
        );
        System.out.println(lines);
        System.out.println("Parsed");

    }

    private void processVertex(String line) {
        String[] values = prettySplit(line);
        double[] result = new double[3];
        for (int i = 1; i < values.length; i++) {
            result[i - 1] = Double.parseDouble(values[i]);
        }
        vertices.add(new Vector3(result[0], result[1], result[2]));
    }

    private void processNormal(String line) {
        String[] values = prettySplit(line);
        double[] result = new double[3];
        for (int i = 1; i < values.length; i++) {
            result[i - 1] = Double.parseDouble(values[i]);
        }
        normals.add(new Vector3(result[0], result[1], result[2]));
    }


    private void processFace(String line) {
        line = line.substring(FACE_PREFIX.length()).trim();
        polygons.add(parseCodeLine(line));
    }

    private Polygon parseCodeLine(String list) {
        if (list == null) {
            return null;
        }
        if (list.equals("")) {
            return null;
        }
        String[] vertexStrings = list.split(" ");
        List<Vertex> parsed = new ArrayList<>();

        for (int i = 0; i < vertexStrings.length; i++) {
            String[] temp = vertexStrings[i].split("/");
            Vector3 geometry = vertices.get(Integer.parseInt(temp[0]) - 1);
            int index = -1;
            if (temp.length >= 3) {
                index = Integer.parseInt(temp[2]);
            }
            Vector3 normal = index == -1 ? Vector3.ZERO : normals.get(index - 1);
            parsed.add(new Vertex(geometry, normal));
        }
        return new Polygon(parsed);
    }

    private String[] prettySplit(String line) {
        return line.replaceAll("\\s+", " ").split(" ");
    }
}
