package services;

import command.line.parser.instances.AbstractInstance;

public interface CommandLineParserService {
    AbstractInstance parseSingleArg(String arg, AbstractInstance object);

    AbstractInstance parseCommandLine(String[] arg);
}
