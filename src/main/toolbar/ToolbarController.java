package main.toolbar;

import Util.LibAssistantUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ToolbarController {


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

}


