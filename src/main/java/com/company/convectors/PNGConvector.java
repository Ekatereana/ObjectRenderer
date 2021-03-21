package com.company.convectors;

import com.company.abstruct.ImageConvector;
import com.company.enums.ChunkType;
import com.company.enums.ImageType;
import com.company.pojo.Chunk;
import com.company.pojo.ColorSpace;
import com.company.pojo.ImageInstance;
import com.company.pojo.ImageMappingException;
import com.company.pojo.headers.PNGHeader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class PNGConvector extends ImageConvector {
    private final int HEADER_SIZE = 8;
    private final int DEF_COL_LENGTH = 4;
    private final int COLOR_TYPE = 9;
    private final int DEFLATE_BYTE = 10;
    private final int FILTER_BYTE = 11;
    private Map<Integer, String> switchColorType;

    public PNGConvector() {
        switchColorType = new HashMap<>();
        switchColorType.put(3, "read");
    }


    @Override
    public ImageInstance read(ImageInstance inst) {

        try {
            InputStream is = inst.getIs();
            readHeader(is, inst.getSourcePath());
            HashMap<ChunkType, Chunk> chunks = new HashMap<>();
            List<Chunk> idats = new ArrayList<>();
//            read all chunks
            Chunk chunk;
            while (is.available() > 0) {
                chunk = readChunk(is, inst.getSourcePath());
                if (chunk.getType().equals(ChunkType.IDAT)) {
                    idats.add(chunk);
                } else {
                    chunks.put(chunk.getType(), chunk);
                }
            }
//            read header
            inst = readImageInstanceHeader(chunks.get(ChunkType.IHDR).getContent(), inst);
//            merge IDAT chunks
            byte[] allContent = mergeIDATs(idats);
            ColorSpace rgb = readPixels(allContent, inst, chunks);
            inst.setRgb(rgb);
            System.out.println("");

        } catch (IOException | ImageMappingException | DataFormatException e) {
            e.printStackTrace();
        }
        return inst;
    }

    private ColorSpace readPixels(byte[] allContent, ImageInstance inst, HashMap<ChunkType, Chunk> chunks) throws ImageMappingException, IOException, DataFormatException {
        PNGHeader header = (PNGHeader) inst.getHeader();
       InflaterInputStream decoder = new InflaterInputStream(new ByteArrayInputStream(allContent));
        int height = header.getHeight();
        int width = header.getWidth();
        System.out.println(header.getColorType());
        int[][] r = new int[height][width];
        int[][] g = new int[height][width];
        int[][] b = new int[height][width];
        Chunk chunk;
        byte[] palette;
        switch (header.getColorType()) {
            case 3:
                chunk = chunks.get(ChunkType.PLTE);
                if (chunk == null)
                    throw new ImageMappingException("Incorrect format of color palette", inst.getSourcePath());
                else {
                    int k;
                    palette = chunk.getContent();
                    for (int i = 0; i < height; i++) {
                        for (int j = 0; j < width; j++) {
                            k = decoder.read();
                            r[i][j] = palette[k];
                            g[i][j] = palette[k];
                            b[i][j] = palette[k];
                        }
                    }
                    return new ColorSpace(r, g, b);
                }

        }

        return null;
    }


    private ImageInstance readImageInstanceHeader(byte[] content, ImageInstance instance) {
        if (content[DEFLATE_BYTE] == 1 || content[FILTER_BYTE] == 1) throw new Error("Unknown encoding type");
        System.out.println();
        int width = ByteBuffer.wrap(Arrays.copyOfRange(content, 0, DEF_COL_LENGTH))
                .order(ByteOrder.BIG_ENDIAN)
                .getInt();
        int length = ByteBuffer.wrap(Arrays.copyOfRange(content, DEF_COL_LENGTH, 2 * DEF_COL_LENGTH))
                .order(ByteOrder.BIG_ENDIAN)
                .getInt();
        instance.setHeader(new PNGHeader(width, length, content[COLOR_TYPE]));
        return instance;
    }

    private Chunk readChunk(InputStream is, String sourcePath) throws IOException {
        int length = fromByteArrToInt(is.readNBytes(DEF_COL_LENGTH));
        byte[] name = is.readNBytes(DEF_COL_LENGTH);
        System.out.println();
        byte[] content = is.readNBytes(length);
        byte[] crc = is.readNBytes(DEF_COL_LENGTH);
        Chunk chunk = new Chunk(length, ChunkType.findByName(name), crc, content);
        return chunk;
    }

    private int fromByteArrToInt(byte[] bytes) {
        return ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).getInt();
    }

    private byte[] mergeIDATs(List<Chunk> chunks) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        chunks.forEach(c -> {
            try {
                out.write(c.getContent());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return out.toByteArray();
    }

    @Override
    public String readHeader(InputStream is, String fileName) throws IOException, ImageMappingException {
        List<String> header = ImageType.PNG.getExpectedHeader();
        int k;
        for (int i = 0; i < HEADER_SIZE; i++) {
            k = is.read();
            if (!Integer.toHexString(k).equals(header.get(i))) {
                throw new ImageMappingException("unexpected symbols in png file header", fileName);
            }

        }
        return "";
    }

    @Override
    public String write(ImageInstance inst) throws IOException {
        return super.write(inst);
    }
}
