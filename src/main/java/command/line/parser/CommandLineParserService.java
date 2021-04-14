package command.line.parser;

import command.line.parser.instances.AbstractInstance;
import org.di.framework.annotations.Component;

import java.util.regex.Pattern;

@Component
public class CommandLineParserService implements services.CommandLineParserService {

    @Override
    public AbstractInstance parseSingleArg(String arg, AbstractInstance object) {
        return null;
    }
    @Override
    public AbstractInstance parseCommandLine(String[] args) {
        AbstractInstance imageInstance = new AbstractInstance();
        Pattern matcher = Pattern.compile("--.*=.+");
        StringBuffer argBuffer = new StringBuffer();
        for (String arg : args) {
            if (matcher.matcher(arg).matches()) {
                imageInstance = parseSingleArg(arg, imageInstance);
            } else {
                argBuffer.append(arg);
                if (matcher.matcher(argBuffer.toString()).matches()) {
                    imageInstance = parseSingleArg(argBuffer.toString(), imageInstance);
                    argBuffer.delete(0, argBuffer.length());
                }
            }
        }
        return imageInstance;
    }


}
