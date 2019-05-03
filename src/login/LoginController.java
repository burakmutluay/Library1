package login;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.codec.digest.DigestUtils;
import settings.Preferences;

import java.io.IOException;

public class LoginController {

    @FXML
    private JFXTextField username;

    @FXML
    private JFXPasswordField password;

    Preferences preferences;

    @FXML
    public void initialize(){
        preferences = Preferences.getPreferences();
    }

    @FXML
    void loadCancelAction(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void loadLoginAction(ActionEvent event) {
        String uname = username.getText();
        String pword = DigestUtils.sha1Hex(password.getText());

        if (uname.equals(preferences.getUsername()) && pword.equals(preferences.getPassword())){
            //Login
            closeStage();
            loadMain();
        }else {
            username.getStyleClass().add("wrong-credentials");
            password.getStyleClass().add("wrong-credentials");

        }
    }

    void closeStage(){
        ((Stage) username.getScene().getWindow()).close();
    }

    void loadMain(){
        try {
            FXMLLoader root= new FXMLLoader(getClass().getResource("/main/main.fxml"));
            Parent parent = root.load();
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Library Assistant");
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
