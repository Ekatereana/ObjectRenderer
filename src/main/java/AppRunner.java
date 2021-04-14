import command.line.parser.RenderingCommandLineParserService;
import command.line.parser.instances.AbstractInstance;
import command.line.parser.instances.ImageInstance;
import command.line.parser.instances.RenderInstance;
import image.converting.ConvectorController;
import image.converting.pojo.ColorSpace;
import image.converting.pojo.headers.Header;
import org.di.framework.annotations.Autowired;
import org.di.framework.annotations.Component;
import org.di.framework.annotations.Qualifier;
import services.ConvecterService;
import services.RenderService;

import java.io.IOException;
import java.sql.SQLOutput;

@Component
public class AppRunner {
    @Autowired
    private RenderService render;

    @Autowired
    private ConvecterService convector;

    @Autowired
    @Qualifier(value = "RenderingCommandLineParserService")
    private RenderingCommandLineParserService argsParser;

    public void start(String[] args){
        try {
            System.out.println("Please, use PPM as goal format. Other formats not implemented as writer yet");
            AbstractInstance instance = argsParser.parseCommandLine(args);
            ColorSpace colorSpace = render.render(instance.getSourcePath());
            ImageInstance ii = new ImageInstance();
            Header header = new Header(640, 640);
            ii.setHeader(header);
            ii.setOutputPath(instance.getOutputPath());
            ii.setGoalFormat(instance.getGoalFormat());
            ii.setRgb(colorSpace);
            convector.convertTo(ii);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Try again please");
        }

    }
}
