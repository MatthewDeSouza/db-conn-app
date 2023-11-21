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
    public void start(Stage primaryStage) {
        hostServices = getHostServices();
        Application.setUserAgentStylesheet(new CupertinoDark().getUserAgentStylesheet());

        try {
            // Load the splash screen
            Parent splashRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("fxml/splash_screen.fxml")));
            Stage splashStage = new Stage(StageStyle.UNDECORATED); // No title bar
            splashStage.setScene(new Scene(splashRoot));
            splashStage.setAlwaysOnTop(true);
            splashStage.centerOnScreen();
            splashStage.show();

            // Add a 1.5-second delay
            PauseTransition delay = new PauseTransition(Duration.seconds(1.5));
            delay.setOnFinished(event -> {
                splashStage.close(); // Close the splash screen
                loadMainScene(primaryStage); // Load the main scene
            });
            delay.play();
        } catch (IOException e) {
            e.printStackTrace(); // Log the exception
            // Handle the error (e.g., show an error dialog)
        }
    }

    private void loadMainScene(Stage primaryStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("fxml/db_interface_gui.fxml")));
            Scene mainScene = new Scene(fxmlLoader.load(), 807, 535);
            primaryStage.setScene(mainScene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Log the exception
            // Handle the error (e.g., show an error dialog)
        }
    }
}
