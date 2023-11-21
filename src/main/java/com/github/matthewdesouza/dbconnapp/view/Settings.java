package com.github.matthewdesouza.dbconnapp.view;

import atlantafx.base.theme.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

import java.util.ArrayList;
import java.util.List;

public class Settings {
    @FXML
    private ChoiceBox themeChoiceBox;

    private void populateThemeChoiceBox() {
        List<Theme> themeList = new ArrayList<>();
        themeList.add(new CupertinoDark());
        themeList.add(new CupertinoLight());
        themeList.add(new Dracula());
        themeList.add(new NordDark());
        themeList.add(new NordLight());
        themeList.add(new PrimerDark());
        themeList.add(new PrimerLight());

        themeList.forEach(e -> themeChoiceBox.getItems().add(e.getName()));
    }

    private void onItemSelect(ActionEvent actionEvent) {

    }
}
