package command.line.parser;

import command.line.parser.instances.AbstractInstance;
import command.line.parser.instances.RenderInstance;
import image.converting.enums.ImageType;
import org.apache.commons.io.FilenameUtils;
import org.di.framework.annotations.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@Component
public class RenderingCommandLineParserService extends CommandLineParserService {

    private final HashMap<Predicate<String>, BiFunction<AbstractInstance, String, AbstractInstance>> keyWords;
    private static LinkedList<String> buffer;

    public RenderingCommandLineParserService() {
        this.keyWords = new HashMap<>();
        this.buffer = new LinkedList<>();
        this.keyWords.put(x -> Pattern.matches("--source *= *.*", x), (inst, field) -> {
            inst.setSourcePath(field.split(" *= *")[1]);
            return inst;
        });
        this.keyWords.put(x -> Pattern.matches("--output *= *.*", x), RenderingCommandLineParserService::parseOutputFormat);
    }

    private static AbstractInstance parseOutputFormat(AbstractInstance inst, String field){
        inst.setGoalFormat(ImageType.getType(FilenameUtils.getExtension(field.split(" *= *")[1])));
        inst.setOutputPath(field.split(" *= *")[1]);
        return inst;
    }

    @Override
    public AbstractInstance parseSingleArg(String arg, AbstractInstance renderInstance) {
        for (Map.Entry<Predicate<String>, BiFunction<AbstractInstance, String, AbstractInstance>> entry :
                keyWords.entrySet()) {
            if (entry.getKey().test(arg)) {
                renderInstance = entry.getValue().apply(renderInstance, arg);
                return renderInstance;
            }
        }
        return null;
    }

    @Override
    public AbstractInstance parseCommandLine(String[] arg) {
        return super.parseCommandLine(arg);
    }
}
