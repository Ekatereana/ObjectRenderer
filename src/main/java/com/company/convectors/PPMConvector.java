package com.company.convectors;

import com.company.abstruct.ImageConvector;
import com.company.enums.ImageType;
import com.company.pojo.ColorSpace;
import com.company.pojo.ImageInstance;
import com.company.pojo.headers.Header;
import com.sun.jdi.InvalidTypeException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PPMConvector extends ImageConvector {
    private final List<Integer> toSkip = Arrays.asList(10, 32, 9, 0, 35);

    @Override
    public ImageInstance read(ImageInstance inst) {

        try {
            InputStream is = inst.getIs();
            String header = readNextIntASCII(is);
            int width = readNextIntASCIIInteger(is);
            int height = readNextIntASCIIInteger(is);
            int depth = readNextIntASCIIInteger(is);
            System.out.println(header + "|" + width + "|" + height + "|" + depth);
            ColorSpace colorSpace = checkEncoding(header, is, width, height);
            inst.setRgb(colorSpace);
            Header ppmHeader = new Header(width, height, depth);
            inst.setHeader(ppmHeader);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inst;
    }

    //    read helper
    private int readNextIntASCIIInteger(InputStream is) throws IOException {
        return Integer.parseInt(readNextIntASCII(is));
    }

    private String readNextIntASCII(InputStream is) throws IOException {
        int k = is.read();
        List<Character> buffer = new ArrayList<>();
        while (k != -1) {
            if (!toSkip.contains(k)) {
                buffer.add((char) k);
            } else {
                if (k == 35) {
                    while (k != 10) {
                        k = is.read();
                    }
                }
                if (!buffer.isEmpty()) {
                    break;
                }

            }
            k = is.read();
        }

        return buffer.stream().map(Object::toString).collect(Collectors.joining());
    }

    private ColorSpace checkEncoding(String header, InputStream is, int width, int height) throws Exception {
        List<String> allowed = ImageType.PPM.getExpectedHeader();
        if (allowed.contains(header)) {
            return header.equals("P6") ? readPSix(is, width, height) : readPThree(is, width, height);
        } else {
            throw new InvalidTypeException(
                    "Only " + allowed.stream().collect(Collectors.joining(" ")) +
                            " allowed but " + header + " founded");
        }
    }

    private ColorSpace readPThree(InputStream is, int width, int height) {
        return null;
    }

    private ColorSpace readPSix(InputStream is, int width, int height) throws Exception {
        int[][] r = new int[height][width];
        int[][] g = new int[height][width];
        int[][] b = new int[height][width];


        try {
            int k = is.read();
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    r[i][j] = k;
                    g[i][j] = is.read();
                    b[i][j] = is.read();
                    k = is.read();
                }
            }
        } catch (IOException e) {
            throw new Exception("Unexpected end of the file");
        }


        return new ColorSpace(r, g, b);
    }

    @Override
    public String write(ImageInstance inst) throws IOException {
        String fileName = inst.getOutputPath() == null ? inst.getSourcePath() : inst.getOutputPath();
        File result = new File(fileName);
        OutputStream writer = new FileOutputStream(result);
        Header header = inst.getHeader();
        ColorSpace rgb = inst.getRgb();
//        write magic number
        writer.write("P6".getBytes(StandardCharsets.US_ASCII));
//        write  \n line
        writer.write(10);
//        write width
        writer.write(intToByteArr(header.getWidth()));
//        write  space char
        writer.write(32);
//        write height
        writer.write(intToByteArr(header.getHeight()));
        writer.write(10);
        writer.write(intToByteArr(header.getDepth()));
        writer.write(10);


        int[][] r = rgb.getR();
        int[][] b = rgb.getB();
        int[][] g = rgb.getG();

        for (int j = 0; j < header.getHeight(); j++) {
            for (int k = 0; k < header.getWidth(); k++) {
                writer.write(r[j][k]);
                writer.write(g[j][k]);
                writer.write(b[j][k]);
            }

        }

        return "";
    }

    //    write helper
    private byte[] intToByteArr(int number) {
        return String.valueOf(number).getBytes();

    }
}