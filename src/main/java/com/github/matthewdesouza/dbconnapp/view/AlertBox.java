package com.github.matthewdesouza.dbconnapp.view;

import com.github.matthewdesouza.dbconnapp.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class AlertBox {
    private final Stage window;
    private final AlertBoxController alertBoxController;

    public AlertBox(AlertType type, String message) {
        try {
            FXMLLoader loader = new FXMLLoader(Controller.class.getResource("fxml/alert_box.fxml"));
            Parent root = loader.load();
            alertBoxController = loader.getController();
            alertBoxController.setData(type, message);

            window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setResizable(false);
            window.setScene(new Scene(root));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showAndWait() {
        window.showAndWait();
    }
}
