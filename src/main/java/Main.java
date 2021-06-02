import org.di.framework.Injector;

public class Main {

    public static void main(String[] args) {
        String [] temp = new String[]{
                "--source",
                "=",
                "C:\\Users\\ekate\\Desktop\\IdeaProjects\\ImageConvecter\\cow.obj",
                "--output=",
                "\"result11.png\""};
        Injector.startApplication(AppRunner.class);
        Injector.getService(AppRunner.class).start(temp);
    }
}

