module com.github.matthewdesouza.weeksixassignment {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires mysql.connector.j;
    requires java.sql;
    requires atlantafx.base;

    opens com.github.matthewdesouza.dbconnapp to javafx.fxml;
    opens com.github.matthewdesouza.dbconnapp.view to javafx.fxml;
    opens com.github.matthewdesouza.dbconnapp.db.model to javafx.base;
    exports com.github.matthewdesouza.dbconnapp;
}