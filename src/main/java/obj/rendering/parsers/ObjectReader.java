package obj.rendering.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface ObjectReader<T> {

    void read(InputStream is) throws IOException;

    List<T> getPolygons();
}
