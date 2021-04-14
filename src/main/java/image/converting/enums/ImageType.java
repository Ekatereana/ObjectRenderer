package image.converting.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum ImageType {
    BMP("bmp", Arrays.asList(new String[]{"BM"})),
    PNG("png", Arrays.asList(new String[]{"89", "50", "4e", "47", "d", "a", "1a", "a"})),
    PPM("ppm", Arrays.asList(new String[]{"P3", "P6"})),
    UNKNOWN("unknown", Arrays.asList(new String[]{}));

    private String token;
    private List<String> expectedHeader;
    private static final Map<String, ImageType> types = new HashMap<>();

    static {
        Arrays.stream(ImageType.values()).forEach(v ->
                types.put(v.token, v));
    }

    ImageType(String token, List<String> expectedHeader) {
        this.token = token;
        this.expectedHeader = expectedHeader;
    }

    public static ImageType getType(String token){
        return types.get(token) != null ? types.get(token) : UNKNOWN ;
    }

    public List<String> getExpectedHeader() {
        return expectedHeader;
    }

    public String getToken() {
        return token;
    }
}
