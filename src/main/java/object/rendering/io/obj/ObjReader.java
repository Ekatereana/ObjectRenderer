package object.rendering.io.obj;

import object.rendering.object.Triangle;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface ObjReader {
  void load(InputStream is) throws IOException;
  List<Triangle> getPolygons();
}
