package com.github.matthewdesouza.dbconnapp.view;

import com.github.matthewdesouza.dbconnapp.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.Objects;

public class AlertBoxController {

    @FXML
    private ImageView alertImageView;

    @FXML
    private TextArea messageTextArea;

    public void setData(AlertType type, String message) {
        alertImageView.setImage(new Image(Objects.requireNonNull(Controller.class.getResource(type.getImagePath())).toString()));
        messageTextArea.setText(message);
    }

    @FXML
    public void closeAlert() {
        Stage stage = (Stage) alertImageView.getScene().getWindow();
        stage.close();
    }
}
