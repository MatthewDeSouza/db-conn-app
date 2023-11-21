package com.github.matthewdesouza.dbconnapp.view;

public enum AlertType {
    ERROR("image/error.png"),
    WARNING("image/warning.png"),
    OK("image/ok.png");


    private final String IMAGE_PATH;

    AlertType(String IMAGE_PATH) {
        this.IMAGE_PATH = IMAGE_PATH;
    }

    public String getImagePath() {
        return IMAGE_PATH;
    }
}
