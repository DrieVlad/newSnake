package gui;

import javafx.application.Application;
import javafx.stage.Stage;
import log.LogWindowSource;

public class RobotsProgram extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Robots");
        primaryStage.setMaxHeight(Field.Height);
        primaryStage.setMaxWidth(Field.Width);
        new MainApplicationStage(primaryStage, new LogWindowSource(5));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
