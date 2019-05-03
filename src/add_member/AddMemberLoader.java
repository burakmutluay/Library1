package add_member;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class AddMemberLoader extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader root= new FXMLLoader(getClass().getResource("add_member.fxml"));

        Pane pane = root.load();

        primaryStage.setScene(new Scene(pane));
        primaryStage.setTitle("Add Member");
        primaryStage.show();
    }
}
