package com.github.matthewdesouza.dbconnapp;

import atlantafx.base.theme.CupertinoDark;
import com.github.matthewdesouza.dbconnapp.view.AlertBox;
import com.github.matthewdesouza.dbconnapp.view.AlertType;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;

public class Controller extends Application {
    private static HostServices hostServices;

    public static void main(String[] args) {
        launch(args);
    }

    public static HostServices getHostService() {
        return hostServices;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        hostServices = getHostServices();
        Application.setUserAgentStylesheet(new CupertinoDark().getUserAgentStylesheet());
        // Load the splash screen
        Parent splashRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("fxml/splash_screen.fxml")));
        Stage splashStage = new Stage();
        splashStage.initStyle(StageStyle.UNDECORATED); // No title bar
        splashStage.setScene(new Scene(splashRoot));

        // Ensure the splash screen is always on top and centered
        splashStage.setAlwaysOnTop(true);
        splashStage.centerOnScreen();

        splashStage.show();

        // Add a 1.5-second delay
        PauseTransition delay = new PauseTransition(Duration.seconds(1.5));
        delay.setOnFinished(event -> {
            // Load the main scene
            loadMainScene(primaryStage, splashStage, event);
        });
        delay.play();
    }

    private void loadMainScene(Stage primaryStage, Stage splashStage, ActionEvent event) {
        FXMLLoader fxml = new FXMLLoader(Objects.requireNonNull(getClass().getResource("fxml/db_interface_gui.fxml")));
        try {
            Scene mainScene = new Scene(fxml.load(), 807, 535);
            primaryStage.setScene(mainScene);
            primaryStage.setResizable(false);

            // Close the splash screen and show the main window
            splashStage.close();
            primaryStage.show();
        } catch (IOException e) {
            AlertBox alertBox = new AlertBox(AlertType.ERROR, "Error loading main scene.\nMessage:\n" + e.getMessage());
            alertBox.showAndWait();
        }
    }
}
