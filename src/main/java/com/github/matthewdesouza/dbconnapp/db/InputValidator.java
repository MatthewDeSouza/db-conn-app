package com.github.matthewdesouza.dbconnapp.db;

public class InputValidator {
    public static boolean isValidString(String input) {
        return input != null && !input.trim().isEmpty();
    }
}
