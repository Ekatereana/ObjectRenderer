package image.converting.convectors;

import image.converting.abstruct.ImageConvector;
import image.converting.enums.ImageType;
import image.converting.pojo.ColorSpace;
import image.converting.pojo.ImageInstance;
import image.converting.pojo.ImageMappingException;
import image.converting.pojo.headers.BMPHeader;
import image.converting.pojo.headers.Header;
import org.apache.commons.compress.utils.BitInputStream;

import java.io.*;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class BMPConvector extends ImageConvector {
    private final int SECTION_SIZE = 4;
    private final int SPEC_SIZE = 2;
    private final int HEADER_SIZE = 14;
    private final int INFO_HEADER_SIZE = 40;
    private final int BIT_NUM = 16;
    private final int PER_COLOR = 5;


    @Override
    public ImageInstance read(ImageInstance inst) {

        try {
            InputStream is = inst.getIs();
            inst.setHeader(readHeader(is, inst.getSourcePath()));
            inst.setRgb(readPixes(is, (BMPHeader) inst.getHeader(), inst.getSourcePath()));
        } catch (ImageMappingException | IOException e) {
            e.printStackTrace();
        }
        return inst;
    }

    public ColorSpace readPixes(InputStream is, BMPHeader header, String fileName) throws IOException, ImageMappingException {
        switch (header.getCompression()) {
            case 0:
                return readWithoutCompression(is, header);
            case 3:
                return readWithCompression(is, header);
            default:
                throw new ImageMappingException("Unknown compression type for 16-bit bmp image", fileName);

        }
    }

    private ColorSpace readWithoutCompression(InputStream is, BMPHeader header) throws IOException {
        int[][] r = new int[header.getHeight()][header.getWidth()];
        int[][] g = new int[header.getHeight()][header.getWidth()];
        int[][] b = new int[header.getHeight()][header.getWidth()];
        is.readNBytes(header.getOffset() - HEADER_SIZE - INFO_HEADER_SIZE);
        BitInputStream reader = new BitInputStream(is, ByteOrder.LITTLE_ENDIAN);
        for (int i = 0; i < header.getHeight(); i++) {
            for (int j = 0; j < header.getWidth(); j++) {
                b[i][j] = BigInteger.valueOf(reader.readBits(PER_COLOR)).intValue();
                g[i][j] = BigInteger.valueOf(reader.readBits(PER_COLOR)).intValue();
                r[i][j] = BigInteger.valueOf(reader.readBits(PER_COLOR)).intValue();
                reader.readBits(1);
            }

        }
        return new ColorSpace(r, g, b);
    }

    private ColorSpace readWithCompression(InputStream is, BMPHeader header) throws IOException {
        int[][] r = new int[header.getHeight()][header.getWidth()];
        int[][] g = new int[header.getHeight()][header.getWidth()];
        int[][] b = new int[header.getHeight()][header.getWidth()];
        byte[] pixel;
        int index;
        if (header.getNumColors() == 0) {
            System.out.println("Error occurred while parsing file. No palette defined for compression type\n" +
                    "Retry with no compressed algorithm");
            return readWithoutCompression(is, header);
        }
        byte[] palette = is.readNBytes(header.getOffset() - INFO_HEADER_SIZE - HEADER_SIZE);
        for (int i = 0; i < header.getHeight(); i++) {
            for (int j = 0; j < header.getWidth(); j++) {
                pixel = is.readNBytes(2);
                index = (pixel[0] | pixel[1] << 8);
                r[i][j] = palette[index];
                g[i][j] = palette[index + 1];
                b[i][j] = palette[index + 2];
            }

        }

        return new ColorSpace(r, g, b);
    }

    @Override
    public BMPHeader readHeader(InputStream is, String fileName) throws ImageMappingException, IOException {
        byte[] header = is.readNBytes(14);
        String fileStart = new String(Arrays.copyOfRange(header, 0, SPEC_SIZE));
        if (!ImageType.BMP.getExpectedHeader().contains(fileStart)) {
            throw new ImageMappingException("unexpected start of bmp file", fileName);
        }
        int offset = ByteBuffer
                .wrap(Arrays.copyOfRange(header, HEADER_SIZE - SECTION_SIZE, HEADER_SIZE))
                .order(ByteOrder.LITTLE_ENDIAN)
                .getInt();
        byte[] infoHeader = is.readNBytes(INFO_HEADER_SIZE);
        byte[] colorSpec = Arrays.copyOfRange(infoHeader, 3 * SECTION_SIZE + SPEC_SIZE, 3 * SECTION_SIZE + 2 * SPEC_SIZE);
        if (colorSpec[0] != 16) {
            throw new ImageMappingException("only 16bit bmp files allowed", fileName);
        }

        int compression =
                getIntFromByteArr(infoHeader,
                        3 * SECTION_SIZE + 2 * SPEC_SIZE, 4 * SECTION_SIZE + 2 * SPEC_SIZE);
        int imageSize =
                getIntFromByteArr(infoHeader,
                        4 * SECTION_SIZE + 2 * SPEC_SIZE, 5 * SECTION_SIZE + 2 * SPEC_SIZE);

        int width = getIntFromByteArr(infoHeader, SECTION_SIZE, 2 * SECTION_SIZE);
        int height = getIntFromByteArr(infoHeader, 2 * SECTION_SIZE, 3 * SECTION_SIZE);
        int numColors = getIntFromByteArr(infoHeader,
                INFO_HEADER_SIZE - 2 * SECTION_SIZE, INFO_HEADER_SIZE - SECTION_SIZE);

        return new BMPHeader(width, height, offset, compression, imageSize, numColors);
    }

    private int getIntFromByteArr(byte[] arr, int start, int end) {
        return ByteBuffer
                .wrap(Arrays.copyOfRange(arr,
                        start, end)).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }

    @Override
    public String write(ImageInstance inst) throws IOException, ImageMappingException {
        throw new ImageMappingException("Not supported for now", inst.getSourcePath());
//        String fileName = inst.getOutputPath() == null ? inst.getSourcePath() : inst.getOutputPath();
//        File result = new File(fileName);
//        Header header = inst.getHeader();
//        OutputStream writer = new FileOutputStream(result);
//        writeHeader(writer, header);
//
//        int[][] r = inst.getRgb().getR();
//        int[][] g = inst.getRgb().getG();
//        int[][] b = inst.getRgb().getG();
//        for (int i = 0; i < header.getHeight(); i++) {
//            for (int j = 0; j < header.getWidth(); j++) {
//                writer.write(normalize(r[i][j]) & 0x00011111);
//                writer.write(normalize(g[i][j]) & 0x00011111);
//                writer.write(normalize(b[i][j]) & 0x00011111);
//            }
//        }
//        return fileName;
    }

    private byte normalize(int i){
        return (byte) (i);
    }

    public void writeHeader(OutputStream writer, Header header) throws IOException {
        writer.write("BM".getBytes());
        int size = calcFileSize(header.getWidth(), header.getHeight());
        writer.write(ByteBuffer.allocate(4).putInt(size).order(ByteOrder.LITTLE_ENDIAN).array());
        writer.write(new byte[4]);
        writer.write(ByteBuffer.allocate(4).putInt(INFO_HEADER_SIZE + HEADER_SIZE).order(ByteOrder.LITTLE_ENDIAN).array());
        writer.write(ByteBuffer.allocate(4).putInt(INFO_HEADER_SIZE).order(ByteOrder.LITTLE_ENDIAN).array());
        writer.write(ByteBuffer.allocate(4).putInt(header.getWidth()).order(ByteOrder.LITTLE_ENDIAN).array());
        writer.write(ByteBuffer.allocate(4).putInt(header.getHeight()).order(ByteOrder.LITTLE_ENDIAN).array());
//        planes
        writer.write(new byte[]{1, 0});
        writer.write(new byte[]{16, 0});
//        compression and image size, ...
        writer.write(new byte[20]);

    }

    private int calcFileSize(int width, int height) {
        return (width * height * 16 + 54) / 8;
    }
}
