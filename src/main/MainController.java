package main;

import Util.DBUtil;
import Util.DialogMaker;
import Util.LibAssistantUtil;
import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.postgresql.util.PSQLException;

import java.io.IOException;
import java.sql.*;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class MainController {

    @FXML
    private StackPane rootPane;

    @FXML
    private AnchorPane rootAnchor;

    @FXML
    private HBox book_info;

    @FXML
    private HBox member_info;

    @FXML
    private HBox submissionDataContainer;

    @FXML
    private Text bookName;

    @FXML
    private Text bookAuthor;

    @FXML
    private Text bookStatus;

    @FXML
    private TextField bookIDInput;

    @FXML
    private Text memberName;

    @FXML
    private Text memberContact;

    @FXML
    private Text memberNameHolder;

    @FXML
    private Text memberEmailHolder;

    @FXML
    private Text memberPhoneHolder;

    @FXML
    private Text bookNameHolder;

    @FXML
    private Text bookAuthorHolder;

    @FXML
    private Text bookPublisherHolder;

    @FXML
    private Text issueDateHolder;

    @FXML
    private Text daysHolder;

    @FXML
    private Text fineHolder;

    @FXML
    private TextField memberIDInput;

    @FXML
    private TextField bookIDRS;

    @FXML
    private ListView<String> issueDataList;

    @FXML
    private JFXButton submissionButton;

    @FXML
    private JFXButton renewButton;

    @FXML
    private JFXHamburger hamburger;

    @FXML
    private JFXDrawer drawer;

    @FXML
    private BorderPane borderPane;


    private Boolean isReadyForSubmission = false;

    public void initialize(){


        initDrawer();

    }

    private void initDrawer(){
        try {
            VBox toolbar = FXMLLoader.load(getClass().getResource("/main/toolbar/toolbar.fxml"));
            drawer.setSidePane(toolbar);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HamburgerSlideCloseTransition task = new HamburgerSlideCloseTransition(hamburger);
        task.setRate(-1);
        hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, (Event event) ->{

            task.setRate(task.getRate() * -1);
            task.play();
            if(drawer.isClosed()){
                drawer.setPrefWidth(120);
                drawer.open();
            }else {
                drawer.setPrefWidth(0);
                drawer.close();
            }

        });
    }

    @FXML
    void loadAddBook(ActionEvent event) {

        LibAssistantUtil.loadWindow(getClass().getResource("/add_book/add_book.fxml"),"Add Book", null);
    }

    @FXML
    void loadAddMember(ActionEvent event) {
        LibAssistantUtil.loadWindow(getClass().getResource("/add_member/add_member.fxml"),"Add Member", null);
    }

    @FXML
    void loadBookTable(ActionEvent event) {
        LibAssistantUtil.loadWindow(getClass().getResource("/book_list/book_list.fxml"),"Book List", null);
    }

    @FXML
    void loadMemberTable(ActionEvent event) {
        LibAssistantUtil.loadWindow(getClass().getResource("/member_list/member_list.fxml"),"Member List", null);
    }

    @FXML
    void loadSettings(ActionEvent event) {
        LibAssistantUtil.loadWindow(getClass().getResource("/settings/settings.fxml"),"Settings", null);
    }

    @FXML
    void loadBookInfo(ActionEvent event){
        String bookId = bookIDInput.getText();
        String query = "SELECT * FROM book WHERE id = '" + bookId + "'";
        DBUtil dbUtil = DBUtil.getInstance();
        Boolean flag = false;

        Connection connection = dbUtil.createConnection();
        try {

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()){

                String bName = rs.getString("title");
                String bAuthor = rs.getString("author");
                Boolean bStatus = rs.getBoolean("isavailable");

                bookName.setText(bName);
                bookAuthor.setText(bAuthor);
                if (bStatus){
                    bookStatus.setText("Available");
                    bookStatus.setStyle("-fx-fill: green");
                }else {
                    bookStatus.setText("Unavailable");
                    bookStatus.setStyle("-fx-fill: red");
                }
                flag = true;
            }
            if (!flag){
                bookName.setText("There is no such book");
                bookAuthor.setText("");
                bookStatus.setText("");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void loadMemberInfo(){
        String memberId = memberIDInput.getText();
        String query = "SELECT * FROM members WHERE memberid = '" + memberId + "'";
        DBUtil dbUtil = DBUtil.getInstance();
        Boolean flag = false;

        Connection connection = dbUtil.createConnection();
        try {

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()){

                String mName = rs.getString("name");
                String mContact = rs.getString("mobile");

                memberName.setText(mName);
                memberContact.setText(mContact);

                flag = true;
            }
            if (!flag){
                memberName.setText("There is no such member");
                bookAuthor.setText("");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void loadIssueOperation(){
        String bookID = bookIDInput.getText();
        String memberID = memberIDInput.getText();

        JFXButton yesButton = new JFXButton("YES");

        yesButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {

            String firstQuery = "INSERT INTO issue(bookID, memberID) VALUES (?,?)";
            String secondQuery = "UPDATE book SET isavailable = false WHERE id = ?";

            DBUtil dbUtil = DBUtil.getInstance();
            Connection connection = dbUtil.createConnection();

            try {
                PreparedStatement stmt1 = connection.prepareStatement(firstQuery);
                stmt1.setString(1,bookID);
                stmt1.setString(2,memberID);
                stmt1.executeUpdate();

                PreparedStatement stmt2 = connection.prepareStatement(secondQuery);
                stmt2.setString(1,bookID);
                stmt2.executeUpdate();

                JFXButton button = new JFXButton("Okay");
                DialogMaker.displayDialog(rootPane, rootAnchor, Arrays.asList(button), "Book issue operation completed");
                clearIssueInfo();

            } catch (PSQLException p) {
                JFXButton button = new JFXButton("Okay");
                DialogMaker.displayDialog(rootPane, rootAnchor, Arrays.asList(button), "The book is unavailable");

            }catch (SQLException e) {
                e.printStackTrace();
            }


        });

        JFXButton noButton = new JFXButton("NO");
        noButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            JFXButton button = new JFXButton("Okay");
            DialogMaker.displayDialog(rootPane, rootAnchor, Arrays.asList(button), "Book issue operation cancelled");
            clearIssueInfo();
        });

        DialogMaker.displayDialog(rootPane, rootAnchor, Arrays.asList(yesButton, noButton), "Are you sure want to issue the book "
                + bookName.getText() +
                " to \n" + memberName.getText() + "?");

    }

    private void clearIssueInfo() {
        bookIDInput.clear();
        memberIDInput.clear();

        bookName.setText("");
        bookAuthor.setText("");
        bookStatus.setText("");

        memberName.setText("");
        memberContact.setText("");
    }

    @FXML
    void loadIssueInfo(){
        clearEntries();
        String bookID = bookIDRS.getText();

        isReadyForSubmission= false;
        String myQuery = "SELECT issue.bookid, issue.memberid, issue.issuetime, issue.renew_count, \n"
                + "members.name, members.email, members.mobile, \n"
                + "book.title, book.author, book.publisher, book.isavailable \n"
                + "FROM issue \n"
                + "LEFT JOIN members \n"
                + "ON issue.memberid = members.memberid \n"
                + "LEFT JOIN book \n"
                + "ON issue.bookid = book.id \n"
                + "WHERE issue.bookid = '" + bookID + "'";
        Connection connection = DBUtil.getInstance().createConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(myQuery);
            if (rs.next()){

                memberNameHolder.setText(rs.getString("name"));
                memberPhoneHolder.setText(rs.getString("mobile"));
                memberEmailHolder.setText(rs.getString("email"));

                bookNameHolder.setText(rs.getString("title"));
                bookAuthorHolder.setText(rs.getString("author"));
                bookPublisherHolder.setText(rs.getString("publisher"));

                Timestamp timeStamp = rs.getTimestamp("issuetime");
                Date issueDate = new Date(timeStamp.getTime());

                issueDateHolder.setText(issueDate.toString());

                Long timeElapsed = System.currentTimeMillis() - timeStamp.getTime();
                Long daysElapsed = TimeUnit.DAYS.convert(timeElapsed, TimeUnit.MILLISECONDS);

                daysHolder.setText(daysElapsed.toString());
                fineHolder.setText("Not Supporting Yet");

                isReadyForSubmission = true;
                disableEnableControls(true);
                submissionDataContainer.setOpacity(1);
            }else{
                JFXButton button = new JFXButton("Okay, I'll Check");
                DialogMaker.displayDialog(rootPane, rootAnchor, Arrays.asList(button), "No Such Book Exists in the Issue Table");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    private void clearEntries() {
        memberNameHolder.setText("");
        memberEmailHolder.setText("");
        memberPhoneHolder.setText("");

        bookPublisherHolder.setText("");
        bookAuthorHolder.setText("");
        bookNameHolder.setText("");

        issueDateHolder.setText("");
        fineHolder.setText("");
        daysHolder.setText("");

        disableEnableControls(false);
        submissionDataContainer.setOpacity(0);
    }

    private void disableEnableControls(Boolean enable){

        if (enable){
            submissionButton.setDisable(false);
            renewButton.setDisable(false);
        }else {
            submissionButton.setDisable(true);
            renewButton.setDisable(true);
        }

    }

    @FXML
    void loadSubmission(ActionEvent event) {

        if (!isReadyForSubmission){
            JFXButton button = new JFXButton("Okay");
            DialogMaker.displayDialog(rootPane, rootAnchor, Arrays.asList(button), "Please select a book to submit");
            return;
        }


        JFXButton yesButton = new JFXButton("YES");
        yesButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event1)->{
            String bookID = bookIDRS.getText();
            String deleteQuery= "DELETE FROM issue WHERE bookid = '" + bookID + "'";
            String updateQuery= "UPDATE book SET isavailable = true WHERE id = '" + bookID + "'";

            DBUtil dbUtil = DBUtil.getInstance();
            Connection connection = dbUtil.createConnection();

            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate(deleteQuery);
                statement.executeUpdate(updateQuery);

                JFXButton button = new JFXButton("Okay");
                DialogMaker.displayDialog(rootPane, rootAnchor, Arrays.asList(button), "The book has been submitted");
                loadIssueInfo();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                JFXButton button = new JFXButton("Okay");
                DialogMaker.displayDialog(rootPane, rootAnchor, Arrays.asList(button), "The book couldn't be submitted");
            }
        });

        JFXButton noButton = new JFXButton("NO");

        DialogMaker.displayDialog(rootPane, rootAnchor, Arrays.asList(yesButton,noButton), "Are you sure want to return the book?");

    }

    @FXML
    void loadRenew(){
        if (!isReadyForSubmission){
            JFXButton button = new JFXButton("Okay");
            DialogMaker.displayDialog(rootPane, rootAnchor, Arrays.asList(button), "Please select a book to renew");
            return;
        }


        JFXButton yesButton = new JFXButton("YES");
        yesButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event2)->{

            String query = "UPDATE issue SET issuetime = CURRENT_TIMESTAMP WHERE bookid = '" + bookIDRS.getText() + "'";
            String renewQuery = "UPDATE issue SET renew_count = renew_count + 1 WHERE bookid = '" + bookIDRS.getText() + "'";
            DBUtil dbUtil = DBUtil.getInstance();
            Connection connection = dbUtil.createConnection();
            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate(query);
                statement.executeUpdate(renewQuery);
                loadIssueInfo();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        JFXButton noButton = new JFXButton("NO");

        DialogMaker.displayDialog(rootPane, rootAnchor, Arrays.asList(yesButton, noButton), "Are you sure want to renew the issue?");

    }

    @FXML
    void menuCloseAction(ActionEvent event) {
        ((Stage) rootPane.getScene().getWindow()).close();
    }

    @FXML
    void handleMenuFullscreen(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.setFullScreen(!stage.isFullScreen());
    }

    void loadWindow(String location, String title){

        try {
            FXMLLoader root= new FXMLLoader(getClass().getResource(location));
            Parent parent = root.load();
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
