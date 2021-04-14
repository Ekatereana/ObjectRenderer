import org.di.framework.Injector;

public class Main {

    public static void main(String[] args) {
        Injector.startApplication(AppRunner.class);
        Injector.getService(AppRunner.class).start(args);

    }
}

