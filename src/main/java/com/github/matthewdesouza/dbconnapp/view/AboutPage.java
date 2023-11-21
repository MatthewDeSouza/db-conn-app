package com.github.matthewdesouza.dbconnapp.view;

import com.github.matthewdesouza.dbconnapp.Controller;
import javafx.fxml.FXML;

public class AboutPage {
    private static final String REPO_URL = "https://github.com/MatthewDeSouza/javafx-gui-database";
    @FXML
    private void openUrl() {
        Controller.getHostService().showDocument(REPO_URL);
    }
}
