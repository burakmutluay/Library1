package settings;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class SettingsController {

    @FXML
    private JFXTextField nDaysWithoutFine;

    @FXML
    private JFXTextField finePerDay;

    @FXML
    private JFXTextField username;

    @FXML
    private JFXPasswordField password;

    @FXML
    public void initialize(){
        initDefaultValues();
    }

    private void initDefaultValues() {
        Preferences preferences = Preferences.getPreferences();

        nDaysWithoutFine.setText(String.valueOf(preferences.getnDaysWithoutFine()));
        finePerDay.setText(String.valueOf(preferences.getFinePerDay()));
        username.setText(String.valueOf(preferences.getUsername()));
        password.setText(String.valueOf(preferences.getPassword()));
    }

    @FXML
    void cancel(ActionEvent event) {
        ((Stage)nDaysWithoutFine.getScene().getWindow()).close();
    }

    @FXML
    void saveSettings(ActionEvent event) {
        int nDays = Integer.parseInt(nDaysWithoutFine.getText());
        float fine = Float.parseFloat(finePerDay.getText());
        String uName = username.getText();
        String pass = password.getText();

        Preferences preferences = Preferences.getPreferences();

        preferences.setnDaysWithoutFine(nDays);
        preferences.setFinePerDay(fine);
        preferences.setUsername(uName);
        preferences.setPassword(pass);

        Preferences.writePreferencesToFile(preferences);

    }



}
