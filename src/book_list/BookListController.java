package book_list;

import Util.DBUtil;
import add_book.ADDBookController;
import javafx.beans.property.SimpleBooleanProperty;
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
import java.sql.*;
import java.util.Optional;

import static Util.DBUtil.isBookAlreadyIssued;

public class BookListController {


    @FXML
    private AnchorPane rootPane;

    @FXML
    private TableView<Book> tableView;

    @FXML
    private TableColumn<Book, String> titleCol;

    @FXML
    private TableColumn<Book, String> idCol;

    @FXML
    private TableColumn<Book, String> authorCol;

    @FXML
    private TableColumn<Book, String> publisherCol;

    @FXML
    private TableColumn<Book, Boolean> availabilityCol;

    ObservableList<Book> list = FXCollections.observableArrayList();

    public void initialize(){
        initCol();
        loadData();
    }
    public void initCol(){
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        publisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        availabilityCol.setCellValueFactory(new PropertyValueFactory<>("availability"));
    }
    public void loadData(){
        list.clear();

        String query = "SELECT * FROM book";
        DBUtil dbUtil = DBUtil.getInstance();
        Connection connection = dbUtil.createConnection();

        try {
            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String titleB = rs.getString("title");
                String idB = rs.getString("id");
                String authorB = rs.getString("author");
                String publisherB = rs.getString("publisher");
                boolean availabilityB = rs.getBoolean("isavailable");

                list.add(new Book(titleB, idB, authorB, publisherB, availabilityB));

            }
            }catch (SQLException s){
                s.printStackTrace();
            }
            tableView.setItems(list);

    }

    @FXML
    void handleBookDelete(ActionEvent event) {

        Book selectedForDeletion = tableView.getSelectionModel().getSelectedItem();

        if (selectedForDeletion==null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("No Book Selected");
            alert.setContentText("Please select a book to delete");
            return;
        }

        if (isBookAlreadyIssued(selectedForDeletion)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("The book couldn't deleted since it is already issued");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure want to delete the book " + selectedForDeletion.title + "?");
        alert.setHeaderText("Confirmation");

        Optional<ButtonType> answer = alert.showAndWait();

        if (answer.get() == ButtonType.OK){
            Boolean result = DBUtil.getInstance().deleteBook(selectedForDeletion);
            if (result){
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setHeaderText("Success");
                alert1.setContentText("The Book " + selectedForDeletion.getTitle() +" Removed Successfully!");
                list.remove(selectedForDeletion);

            }
        }
    }

    @FXML
    void handleBookEdit(ActionEvent event) {
        Book selectedForEdit = tableView.getSelectionModel().getSelectedItem();

        if (selectedForEdit==null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("No Book Selected");
            alert.setContentText("Please select a book to edit");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/add_book/add_book.fxml"));

            Parent parent = loader.load();


            ADDBookController controller = loader.getController();
            controller.infliateUI(selectedForEdit);


            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Edit Book");
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



    public static class Book{
        private final SimpleStringProperty title;
        private final SimpleStringProperty id;
        private final SimpleStringProperty author;
        private final SimpleStringProperty publisher;
        private final SimpleBooleanProperty availability;

        public Book(String title, String id, String author, String publisher, boolean avail){
            this.title = new SimpleStringProperty(title);
            this.id = new SimpleStringProperty(id);
            this.author = new SimpleStringProperty(author);
            this.publisher = new SimpleStringProperty(publisher);
            this.availability = new SimpleBooleanProperty(avail);
        }


        public String getTitle(){
            return title.get();
        }
        
        public String getId() {
            return id.get();
        }

        public String getAuthor() {
            return author.get();
        }
        public String getPublisher() {
            return publisher.get();
        }

        public boolean getAvailability() {
            return availability.get();
        }


    }

}
