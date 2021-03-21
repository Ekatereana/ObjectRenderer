package com.company;

import com.company.enums.ImageType;
import com.company.pojo.ImageInstance;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandLineParser {
    private final HashMap<Predicate<String>, BiFunction<ImageInstance, String, ImageInstance>> keyWords;
    private static LinkedList<String> buffer;

    public CommandLineParser() {
        this.keyWords = new HashMap<>();
        buffer = new LinkedList<>();
        keyWords.put(x -> Pattern.matches("--source *= *.*", x), CommandLineParser::saveSourcePathVars);
        keyWords.put(x -> Pattern.matches("--goal-format *= *.*", x), (inst, field) -> {
            inst.setGoalFormat(ImageType.getType(field.split(" *= *")[1]));
            return inst;
        });
        keyWords.put(x -> Pattern.matches("--output *= *.*", x), (inst, field) -> {
            inst.setOutputPath(field.split(" *= *")[1]);
            return inst;
        });

    }



    @SneakyThrows
    private static ImageInstance saveSourcePathVars(ImageInstance inst, String field) {
        inst.setSourceFormat(ImageType.getType(FilenameUtils.getExtension(field.split(" *= *")[1])));
        inst.setSourcePath(field.split(" *= *")[1]);
        inst.setIs(new FileInputStream(field.split(" *= *")[1]));

        return inst;
    }

    public ImageInstance parseCommandLineArgs(String[] args) {
        ImageInstance imageInstance = new ImageInstance();
        Pattern matcher = Pattern.compile("--.*=.+");
        StringBuffer argBuffer = new StringBuffer();
        for (String arg : args) {
            if(matcher.matcher(arg).matches()) {
                imageInstance = parseSingleArg(arg, imageInstance);
            } else {
                argBuffer.append(arg);
                if (matcher.matcher(argBuffer.toString()).matches()){
                    imageInstance = parseSingleArg(argBuffer.toString(), imageInstance);
                    argBuffer.delete(0, argBuffer.length());
                }
            }
        }
        return imageInstance;
    }

    private ImageInstance parseSingleArg(String arg, ImageInstance imageInstance){
        for (Map.Entry<Predicate<String>, BiFunction<ImageInstance, String, ImageInstance>> entry :
                keyWords.entrySet()) {
            if (entry.getKey().test(arg)) {
                imageInstance = entry.getValue().apply(imageInstance, arg);
                return imageInstance;
            }
        }
        return null;
    }


}
