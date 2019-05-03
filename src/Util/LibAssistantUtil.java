package Util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.print.DocFlavor;
import java.io.IOException;
import java.net.URL;

public class LibAssistantUtil {
    private static final String ICON_LOC = "/img/icon.png";

    public static void setStageIcon(Stage stage){

        stage.getIcons().add(new Image(ICON_LOC));
    }

    public static void loadWindow(URL location, String title, Stage parentStage){

        try {
            FXMLLoader root= new FXMLLoader(location);
            Parent parent = root.load();

            Stage stage = null;
            if (parentStage != null){
                stage = parentStage;
            }else {

                stage = new Stage(StageStyle.DECORATED);
            }

            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
