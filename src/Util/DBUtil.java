package Util;

import book_list.BookListController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import member_list.MemberListController;

import java.sql.*;

public class DBUtil {

    private static DBUtil dbUtil = null;

    private static final String DB_URL = "jdbc:postgresql://localhost:5433/Library";
    private static Connection conn = null;
    private static Statement stmt = null;


    private DBUtil(){

    }

    public static DBUtil getInstance(){
        if (dbUtil == null){
            dbUtil = new DBUtil();
        }
        return dbUtil;
    }



    public Connection createConnection(){
        try {
            Class.forName("org.postgresql.Driver").newInstance();
            conn = DriverManager.getConnection(DB_URL, "postgres", "235711");
        }catch (Exception e){
            e.printStackTrace();
        }
        return conn;
    }

    public ResultSet execQuery(String query){
        ResultSet result;
        try {
            stmt = conn.createStatement();
            result = stmt.executeQuery(query);
        }catch (Exception e){
            System.out.println("Exception at DBUtil:execQuery" + e.getLocalizedMessage());
            return null;
        }
        return result;
    }

    public boolean deleteBook(BookListController.Book book){
        String deleteStatement = "DELETE FROM book WHERE id= ?";

        Connection connection = dbUtil.createConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(deleteStatement);
            statement.setString(1,book.getId());
            int res = statement.executeUpdate();
            System.out.println(res);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean editBook(BookListController.Book book){
        String updateQuery = "UPDATE book SET title =?, author = ?, publisher = ? WHERE id=?";

        Connection connection = DBUtil.getInstance().createConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(updateQuery);
            statement.setString(1,book.getTitle());
            statement.setString(2,book.getAuthor());
            statement.setString(3,book.getPublisher());
            statement.setString(4,book.getId());
            int rs = statement.executeUpdate();

            return (rs>0);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isBookAlreadyIssued(BookListController.Book book){
        String checkStatement = "SELECT COUNT(*) FROM issue WHERE bookid= ?";

        Connection connection = dbUtil.createConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(checkStatement);
            statement.setString(1,book.getId());

            ResultSet rs = statement.executeQuery();
            if (rs.next()){
                int count = rs.getInt(1);
                System.out.println(count);
                if (count>0){
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }

    public static boolean isMemberAlreadyIssued(MemberListController.Member member){
        String checkStatement = "SELECT COUNT(*) FROM issue WHERE memberid= ?";

        Connection connection = dbUtil.createConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(checkStatement);
            statement.setString(1,member.getId());

            ResultSet rs = statement.executeQuery();
            if (rs.next()){
                int count = rs.getInt(1);
                System.out.println(count + 0);
                if (count>0){
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
        return false;

    }

    public boolean deleteMember(MemberListController.Member member){
        String deleteStatement = "DELETE FROM members WHERE memberid= ?";

        Connection connection = dbUtil.createConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(deleteStatement);
            statement.setString(1,member.getId());
            int res = statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean editMember(MemberListController.Member member){
        String updateQuery = "UPDATE members SET name =?, mobile = ?, email = ? WHERE memberid=?";

        Connection connection = DBUtil.getInstance().createConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(updateQuery);
            statement.setString(1,member.getName());
            statement.setString(2,member.getMobile());
            statement.setString(3,member.getEmail());
            statement.setString(4,member.getId());
            int rs = statement.executeUpdate();

            return (rs>0);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    //TODO: Complete the method getBookGraphStatistics()
    /*
    public ObservableList<PieChart.Data> getBookGraphStatistics(){
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        String bookCountQuery = "SELECT COUNT(*) FROM book";
        String issuedBookCountQuery = "SELECT COUNT(*) FROM issue";

        Connection connection = DBUtil.getInstance().createConnection();
        try {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(bookCountQuery);
            int bookCount = resultSet.getInt(1);

            resultSet = statement.executeQuery(issuedBookCountQuery);
            int issuedBookCount = resultSet.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    } */
}
