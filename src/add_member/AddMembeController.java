package add_member;

import Util.DBUtil;
import book_list.BookListController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import member_list.MemberListController;
import org.postgresql.util.PSQLException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddMembeController {
    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField name;

    @FXML
    private TextField id;

    @FXML
    private TextField mobile;

    @FXML
    private TextField email;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private Boolean isInEditMode = Boolean.FALSE;

    @FXML
    void addMember(ActionEvent event) {

        String mName = name.getText();
        String mID = id.getText();
        String mMobile = mobile.getText();
        String mEmail = email.getText();


        if (mName.isEmpty()||mID.isEmpty()||mMobile.isEmpty()||mEmail.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please fill all the fields ");
            alert.showAndWait();
            return;
        }

        if (isInEditMode){
            handleEditMemberOperation();
            return;
        }
        boolean isAdded = true;
        DBUtil dbUtil = DBUtil.getInstance();
        Connection connection = dbUtil.createConnection();

        String query = "INSERT INTO members VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,mName);
            preparedStatement.setString(2,mID);
            preparedStatement.setString(3,mEmail);
            preparedStatement.setString(4,mMobile);

            preparedStatement.executeUpdate();
        } catch (PSQLException e) {
            Alert dupAlert = new Alert(Alert.AlertType.ERROR);
            dupAlert.setHeaderText(null);
            dupAlert.setContentText("The Member ID is already exists");
            dupAlert.showAndWait();
            isAdded = false;
        }catch (SQLException e){
            e.printStackTrace();
        }

        if (isAdded){
            id.setText(null);
            name.setText(null);
            mobile.setText(null);
            email.setText(null);

            Alert confirmAlert = new Alert(Alert.AlertType.INFORMATION);
            confirmAlert.setHeaderText(null);
            confirmAlert.setContentText("The member has been added to the database successfully");
            confirmAlert.showAndWait();
        }

    }

    private void handleEditMemberOperation() {
        MemberListController.Member member = new MemberListController.Member(name.getText(), id.getText(), mobile.getText(), email.getText());

        if (DBUtil.getInstance().editMember(member)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Success");
            alert.setContentText("The member updated successfully");
            alert.showAndWait();
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("The member couldn't be updated");
            alert.showAndWait();
        }
    }

    @FXML
    void cancel(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    public void infliateUI(MemberListController.Member member){
        name.setText(member.getName());
        id.setText(member.getId());
        mobile.setText(member.getMobile());
        email.setText(member.getEmail());
        id.setEditable(false);
        isInEditMode = Boolean.TRUE;
    }

}

