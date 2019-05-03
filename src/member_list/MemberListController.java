package member_list;

import Util.DBUtil;
import add_book.ADDBookController;
import add_member.AddMembeController;
import book_list.BookListController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import static Util.DBUtil.isMemberAlreadyIssued;

public class MemberListController {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TableView<Member> tableView;


    @FXML
    private TableColumn<Member, String> nameCol;

    @FXML
    private TableColumn<Member, String> memberIDCol;

    @FXML
    private TableColumn<Member, String> mobileCol;

    @FXML
    private TableColumn<Member, String> emailCol;

    ObservableList<Member> list = FXCollections.observableArrayList();

    public void initialize(){
        initCol();
        loadData();
    }

    public void initCol(){
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        memberIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        mobileCol.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    public void loadData(){
        list.clear();

        DBUtil dbUtil = DBUtil.getInstance();

        Connection connection = dbUtil.createConnection();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM members");

            while (rs.next()){
                String memname = rs.getString(1);
                String memid = rs.getString(2);
                String mememail = rs.getString(3);
                String memmobile = rs.getString(4);
                list.add(new Member(memname, memid, memmobile, mememail));
            }

            tableView.getItems().setAll(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void handleDelete(ActionEvent event) {
        Member selectedForDeletion = tableView.getSelectionModel().getSelectedItem();

        if (selectedForDeletion==null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("No Member Selected");
            alert.setContentText("Please select a member to delete");
            return;
        }

        if (isMemberAlreadyIssued(selectedForDeletion)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("The member couldn't deleted since it is already issued");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Confirmation");
        alert.setContentText("Are you sure want to delete the member " + selectedForDeletion.getName() + "?");

        Optional<ButtonType> answer = alert.showAndWait();

        if (answer.get() == ButtonType.OK){
            Boolean result = DBUtil.getInstance().deleteMember(selectedForDeletion);
            if (result){
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setHeaderText("Success");
                alert1.setContentText("The Member " + selectedForDeletion.getName() +" removed Successfully!");
                list.remove(selectedForDeletion);
                loadData();
            }
        }
    }

    @FXML
    void handleMemberEdit(ActionEvent event) {

        Member selectedForEdit = tableView.getSelectionModel().getSelectedItem();

        if (selectedForEdit==null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("No Member Selected");
            alert.setContentText("Please select a member to edit");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/add_member/add_member.fxml"));

            Parent parent = loader.load();


            AddMembeController controller = loader.getController();
            controller.infliateUI(selectedForEdit);


            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Edit Member");
            stage.setScene(new Scene(parent));
            stage.show();

            stage.setOnCloseRequest((event1 ->
                    handleRefresh(new ActionEvent())));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void handleRefresh(ActionEvent event) {
        loadData();
    }

    public static class Member{
        private final SimpleStringProperty name;
        private final SimpleStringProperty id;
        private final SimpleStringProperty mobile;
        private final SimpleStringProperty email;

        public Member(String name, String id, String mobile, String email) {
            this.name = new SimpleStringProperty(name);
            this.id = new SimpleStringProperty(id);
            this.mobile = new SimpleStringProperty(mobile);
            this.email = new SimpleStringProperty(email);
        }



        public String getName(){
            return this.name.get();
        }

        public String getId(){
            return this.id.get();
        }

        public String getMobile(){
            return this.mobile.get();
        }

        public String getEmail(){
            return this.email.get();
        }
    }
}


