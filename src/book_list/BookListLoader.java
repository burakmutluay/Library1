package book_list;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class BookListLoader extends Application{


    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader root = new FXMLLoader(getClass().getResource("book_list.fxml"));
        Pane pane = root.load();

        primaryStage.setScene(new Scene(pane));
        primaryStage.setTitle("Book List");
        primaryStage.show();

    }
}
