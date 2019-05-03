package add_book;

import Util.DBUtil;
import book_list.BookListController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.postgresql.util.PSQLException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ADDBookController {

    @FXML
    private TextField title;

    @FXML
    private TextField id;

    @FXML
    private TextField author;

    @FXML
    private TextField publisher;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private AnchorPane rootPane;

    private Boolean isInEditMode = Boolean.FALSE;


    @FXML
    void addBook(ActionEvent event) {
        String bookID = id.getText();
        String bookAuthor = author.getText();
        String bookPublisher = publisher.getText();
        String bookTitle = title.getText();
        boolean isAdded = true;

        if (bookID.isEmpty()||bookAuthor.isEmpty()||bookPublisher.isEmpty()||bookAuthor.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please fill all the fields");
            alert.showAndWait();
            return;
        }

        if (isInEditMode){
            handleEditOperation();
            return;
        }
        
        

        try {
            PreparedStatement addBookStmt;
            DBUtil dbUtil = DBUtil.getInstance();
            Connection conn = dbUtil.createConnection();

            String myStr = "INSERT INTO book " + "VALUES (?, ?, ?, ?, true)";
            addBookStmt = conn.prepareStatement(myStr);
            addBookStmt.setString(1,bookID);
            addBookStmt.setString(2,bookTitle);
            addBookStmt.setString(3,bookAuthor);
            addBookStmt.setString(4,bookPublisher);

            addBookStmt.executeUpdate();



        }catch(PSQLException p){
            Alert duplicateAlert = new Alert(Alert.AlertType.ERROR);
            duplicateAlert.setHeaderText(null);
            duplicateAlert.setContentText("The ID is already exists");
            duplicateAlert.showAndWait();
            isAdded = false;
        }catch (Exception e){
            e.printStackTrace();
        }

        if (isAdded){
            id.setText(null);
            author.setText(null);
            publisher.setText(null);
            title.setText(null);

            Alert confirmAlert = new Alert(Alert.AlertType.INFORMATION);
            confirmAlert.setHeaderText(null);
            confirmAlert.setContentText("The book has been added to the database successfully");
            confirmAlert.showAndWait();
        }

    }

    private void handleEditOperation() {
        BookListController.Book book = new BookListController.Book(title.getText(), id.getText()
                ,author.getText(), publisher.getText(), true);

        if (DBUtil.getInstance().editBook(book)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Success");
            alert.setContentText("The book updated successfully");
            alert.showAndWait();
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("The book couldn't be updated");
            alert.showAndWait();
        }
    }


    @FXML
    void cancel(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    public void infliateUI(BookListController.Book book){
        title.setText(book.getTitle());
        id.setText(book.getId());
        author.setText(book.getAuthor());
        publisher.setText(book.getPublisher());
        id.setEditable(false);
        isInEditMode = Boolean.TRUE;
    }

}
