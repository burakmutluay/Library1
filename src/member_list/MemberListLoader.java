package member_list;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MemberListLoader extends Application{
    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader root = new FXMLLoader(getClass().getResource("member_list.fxml"));

        Pane pane = root.load();

        primaryStage.setScene(new Scene(pane));
        primaryStage.setTitle("Member List");
        primaryStage.show();
    }
}
