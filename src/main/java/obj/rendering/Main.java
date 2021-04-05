package obj.rendering;

import obj.rendering.parsers.ObjectLoader;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            ObjectLoader loader = new ObjectLoader();
            loader.load("C:\\Users\\ekate\\IdeaProjects\\RenderCow\\src\\main\\resources\\cow.obj");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
