package image.converting.convectors;

import command.line.parser.instances.ImageInstance;
import image.converting.abstruct.ImageConvector;
import image.converting.enums.ChunkType;
import image.converting.enums.ImageType;
import image.converting.pojo.Chunk;
import image.converting.pojo.ColorSpace;
import image.converting.pojo.ImageMappingException;
import image.converting.pojo.headers.PNGHeader;
import org.apache.commons.compress.utils.BitInputStream;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import java.util.*;
import java.util.zip.DataFormatException;
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
            System.out.println("I'm trying hard, but don't work properly. " +
                    "So I created cool abstractions. Let's check!");
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
        if (header.getColorType() != 3) {
            throw new ImageMappingException("Unsupported color type. Only  PLTE indexes allowed", inst.getSourcePath());
        }

        chunk = chunks.get(ChunkType.PLTE);
        if (chunk == null)
            throw new ImageMappingException("Incorrect format of color palette", inst.getSourcePath());
        else {
            int k;
            palette = chunk.getContent();
            BitInputStream reader = new BitInputStream(decoder, ByteOrder.BIG_ENDIAN);
            System.out.println(header.isOrder());
            System.out.println(header.getFilter());
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    k = BigInteger.valueOf(reader.readBits(header.getBitDepth())).intValue();
                    r[i][j] = palette[k] & 0xff;
                    g[i][j] = palette[k + 1] & 0xff;
                    b[i][j] = palette[k + 2] & 0xff;
                }
            }
            return new ColorSpace(r, g, b);

        }
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
        instance.setHeader(new PNGHeader(width, length,
                content[COLOR_TYPE], content[2 * DEF_COL_LENGTH], content[DEFLATE_BYTE + 1], content[DEFLATE_BYTE + 2] == 1));
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
    public String write(ImageInstance inst) throws IOException, ImageMappingException {
        BufferedImage image = new BufferedImage(inst.getHeader().getWidth(), inst.getHeader().getHeight(),
                BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();
        int [][] r = inst.getRgb().getR();
        int [][] g = inst.getRgb().getG();
        int [][] b = inst.getRgb().getR();

        for (int i = 0; i < r.length; i++) {
            for (int j = 0; j < r[i].length; j++) {
              try{
                  graphics.setColor(new Color(
                          r[i][j] > 0 ? r[i][j] : 255,
                          g[i][j] > 0 ? g[i][j] : 255,
                          b[i][j] > 0 ? b[i][j] : 255));
              }
              catch (IllegalArgumentException e){
                  System.out.println(r[i][j]);
                  System.out.println(b[i][j]);
                  System.out.println(g[i][j]);
              }
            }
        }
        try {
            ImageIO.write(image, "PNG", new File(inst.getOutputPath()));
        } catch (IOException error) {
            error.printStackTrace();
        }
        return inst.getOutputPath();
    }
}
