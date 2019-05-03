package Util;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.scene.Node;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.util.List;

public class DialogMaker {

    public static void displayDialog(StackPane root, Node nodeToBeBlurred, List<JFXButton> controls, String bodyText){
        BoxBlur blur = new BoxBlur(3,3,3);

        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        JFXDialog dialog = new JFXDialog(root, dialogLayout, JFXDialog.DialogTransition.TOP);

        controls.forEach(controlButton ->{
            controlButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseevent)->{
                nodeToBeBlurred.setEffect(null);
                dialog.close();
            });

        });

        dialogLayout.setBody(new Text(bodyText));
        dialogLayout.setActions(controls);
        dialog.show();
        nodeToBeBlurred.setEffect(blur);
    }
}
