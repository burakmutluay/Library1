package main;

import Util.LibAssistantUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader root = new FXMLLoader(getClass().getResource("/login/login.fxml"));
        Pane pane = root.load();

        //LibAssistantUtil.setStageIcon(primaryStage);

        primaryStage.setScene(new Scene(pane));
        primaryStage.setTitle("Library Management Tool");
        primaryStage.show();
    }
}
