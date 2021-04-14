package image.converting.enums;


import lombok.Getter;

import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


public enum ChunkType{
    IHDR(true, new byte[]{73, 72, 68, 82}),
    PLTE(true, new byte[]{80, 76, 84, 69}),
    IDAT(true, new byte[]{73, 68, 65, 84}),
    IEND(true, new byte[]{73, 69, 78, 68}),
    OTHER(false, new byte[]{});
    @Getter
    private boolean isCritical;
    @Getter
    private String name;
    private static final Map<String, ChunkType> types = new HashMap<>();

    static {
        Arrays.stream(ChunkType.values()).forEach(v ->
                types.put(v.name, v));
    }


    ChunkType(boolean isCritical, byte[] name) {
        this.isCritical = isCritical;
        this.name = Base64.getEncoder().encodeToString(name);
    }


    public static ChunkType findByName(byte[] name) {
        ChunkType result = types.get(Base64.getEncoder().encodeToString(name));
        return result != null ? result : OTHER;
    }
}
