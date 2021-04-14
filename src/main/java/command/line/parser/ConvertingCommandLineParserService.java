package command.line.parser;

import command.line.parser.instances.AbstractInstance;
import command.line.parser.instances.ImageInstance;
import image.converting.enums.ImageType;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.di.framework.annotations.Component;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@Component
public class ConvertingCommandLineParserService extends CommandLineParserService {
    private final HashMap<Predicate<String>, BiFunction<ImageInstance, String, ImageInstance>> keyWords;
    private static LinkedList<String> buffer;

    public ConvertingCommandLineParserService() {
        this.keyWords = new HashMap<>();
        this.buffer = new LinkedList<>();
        this.keyWords.put(x -> Pattern.matches("--source *= *.*", x), ConvertingCommandLineParserService::saveSourcePathVars);
        this.keyWords.put(x -> Pattern.matches("--goal-format *= *.*", x), (inst, field) -> {
            inst.setGoalFormat(ImageType.getType(field.split(" *= *")[1]));
            return inst;
        });
        this.keyWords.put(x -> Pattern.matches("--output *= *.*", x), (inst, field) -> {
            inst.setOutputPath(field.split(" *= *")[1]);
            return inst;
        });
    }


    @Override
    public AbstractInstance parseSingleArg(String arg, AbstractInstance imageInstance) {
        for (Map.Entry<Predicate<String>, BiFunction<ImageInstance, String, ImageInstance>> entry :
                keyWords.entrySet()) {
            if (entry.getKey().test(arg)) {
                imageInstance = entry.getValue().apply((ImageInstance) imageInstance, arg);
                return imageInstance;
            }
        }
        return null;
    }

    @SneakyThrows
    private static ImageInstance saveSourcePathVars(ImageInstance inst, String field) {
        inst.setSourceFormat(ImageType.getType(FilenameUtils.getExtension(field.split(" *= *")[1])));
        inst.setSourcePath(field.split(" *= *")[1]);
        inst.setIs(new FileInputStream(field.split(" *= *")[1]));
        return inst;
    }

    @Override
    public AbstractInstance parseCommandLine(String[] args) {
        ImageInstance imageInstance = (ImageInstance) super.parseCommandLine(args);
        if (imageInstance.getOutputPath() == null) {
            imageInstance.
                    setOutputPath(FilenameUtils
                            .getFullPath(imageInstance.getSourcePath())
                            + FilenameUtils.getBaseName(imageInstance.getSourcePath())
                            + "Result."
                            + imageInstance.getGoalFormat().getToken());
        }
        return imageInstance;
    }
}
