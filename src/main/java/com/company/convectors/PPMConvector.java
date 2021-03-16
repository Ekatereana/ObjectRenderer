package com.company.convectors;

import com.company.abstruct.ImageConvector;
import com.company.enums.ImageType;
import com.company.pojo.ColorSpace;
import com.company.pojo.ImageInstance;
import com.sun.jdi.InvalidTypeException;

import java.io.IOException;
import java.io.InputStream;
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
            System.out.println(header + "|" + width + "|" + height);
            ColorSpace colorSpace = checkEncoding(header, is, width, height);
            inst.setRgb(colorSpace);
            inst.setWidth(width);
            inst.setHeight(height);
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

        int row = 0;
        int column = 0;
        try {
            int k = is.read();
            while (k != -1) {
                r[row][column] = k;

                g[row][column] = is.read();

                b[row][column] = is.read();

                k = is.read();

            }
        } catch (IOException e) {
            throw new Exception("Unexpected end of the file");
        }


        return new ColorSpace(r, g, b);
    }

    @Override
    public ImageInstance write(ImageInstance inst) {
        return null;
    }

//    write helper
}
