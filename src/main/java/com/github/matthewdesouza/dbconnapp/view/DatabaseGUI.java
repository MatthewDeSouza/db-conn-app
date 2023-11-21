package com.github.matthewdesouza.dbconnapp.view;

import atlantafx.base.theme.*;
import com.github.matthewdesouza.dbconnapp.Controller;
import com.github.matthewdesouza.dbconnapp.db.DatabaseConnector;
import com.github.matthewdesouza.dbconnapp.db.InputValidator;
import com.github.matthewdesouza.dbconnapp.db.model.Major;
import com.github.matthewdesouza.dbconnapp.db.model.Person;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

public class DatabaseGUI implements Initializable {

    private static final DatabaseConnector db = DatabaseConnector.getInstance();
    @FXML
    TextField first_name, last_name, department;
    @FXML
    ComboBox<Major> major;
    @FXML
    ImageView img_view;

    @FXML
    Button editButton;
    @FXML
    Button addButton;
    @FXML
    Button deleteButton;

    private ObservableList<Person> data;
    @FXML
    private TableView<Person> tv;

    @FXML
    private TableColumn<Person, Integer> tv_id;

    @FXML
    private TableColumn<Person, String> tv_fn, tv_ln, tv_dept, tv_major;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tv_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        tv_fn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tv_ln.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tv_dept.setCellValueFactory(new PropertyValueFactory<>("dept"));
        tv_major.setCellValueFactory(new PropertyValueFactory<>("major"));

        data = FXCollections.observableList(db.getAllPeople());

        tv.setItems(data);

        addButton.setDisable(true);
        editButton.setDisable(true);
        deleteButton.setDisable(true);

        // Add listener to table selection
        tv.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean isItemSelected = (newSelection != null);
            editButton.setDisable(!isItemSelected);
            deleteButton.setDisable(!isItemSelected);
        });

        // Add listeners to text fields for form validation
        first_name.textProperty().addListener((obs, oldText, newText) -> validateForm());
        last_name.textProperty().addListener((obs, oldText, newText) -> validateForm());
        department.textProperty().addListener((obs, oldText, newText) -> validateForm());
        major.setItems(FXCollections.observableArrayList(Major.values()));
    }

    private void validateForm() {
        boolean isValid = !first_name.getText().isEmpty() && !last_name.getText().isEmpty() &&
                !department.getText().isEmpty() && major.getValue() != null;
        addButton.setDisable(!isValid);
    }


    @FXML
    protected void addNewRecord() {
        if (!InputValidator.isValidString(first_name.getText()) ||
                !InputValidator.isValidString(last_name.getText()) ||
                !InputValidator.isValidString(department.getText()) ||
                major.getValue() == null) {

            AlertBox alertBox = new AlertBox(AlertType.WARNING, "Please fill in all fields correctly.");
            alertBox.showAndWait();
            return;
        }
        // Create a new Person object from the form data
        Person newPerson = new Person(null, // ID will be auto-generated by the database
                first_name.getText(), last_name.getText(), department.getText(), major.getValue());

        // Insert the new person into the database
        DatabaseConnector.getInstance().insertUser(newPerson);

        // Refresh the table view to reflect the new data
        refreshTableView();
    }

    private void refreshTableView() {
        // Fetch all people from the database and update the table view
        List<Person> allPeople = DatabaseConnector.getInstance().getAllPeople();
        data.clear();
        data.addAll(allPeople);
        tv.setItems(data);
        tv.refresh();
    }

    @FXML
    protected void clearForm() {
        first_name.clear();
        last_name.setText("");
        department.setText("");
        major.setValue(Major.NIL);
    }

    @FXML
    protected void closeApplication() {
        System.exit(0);
    }


    @FXML
    protected void editRecord() {
        Person selectedPerson = tv.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            int selectedIndex = tv.getSelectionModel().getSelectedIndex();

            // Update the selected person object with the new data
            selectedPerson.setFirstName(first_name.getText());
            selectedPerson.setLastName(last_name.getText());
            selectedPerson.setDept(department.getText());
            selectedPerson.setMajor(major.getValue());

            // Update the record in the database
            db.updateUser(selectedPerson);

            // Refresh the TableView
            tv.getItems().set(selectedIndex, selectedPerson);
        } else {
            // Display some error message or alert to the user
            System.out.println("No record selected to edit!");
        }
    }


    @FXML
    protected void deleteRecord() {
        // Get the selected person from the table view
        Person selectedPerson = tv.getSelectionModel().getSelectedItem();

        if (selectedPerson != null) {
            // Delete the person from the database
            db.deleteUser(selectedPerson.getId());

            // Remove the person from the table view
            data.remove(selectedPerson);

            // Refresh the table view
            refreshTableView();
        } else {
            // Optionally, show a message to the user if no person is selected
            System.out.println("No person selected!");
        }
    }


    @FXML
    protected void showImage() {
        File file = (new FileChooser()).showOpenDialog(img_view.getScene().getWindow());
        if (file != null) {
            img_view.setImage(new Image(file.toURI().toString()));

        }
    }


    @FXML
    protected void selectedItemTV(MouseEvent mouseEvent) {
        Person p = tv.getSelectionModel().getSelectedItem();
        if (p == null) {
            return;
        }
        first_name.setText(p.getFirstName());
        last_name.setText(p.getLastName());
        department.setText(p.getDept());
        major.setValue(p.getMajor());
    }

    @FXML
    public void refreshView(ActionEvent actionEvent) {
        refreshTableView();
    }

    public void showAboutPage(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(Controller.class.getResource("fxml/about_page.fxml"));
            Parent root = loader.load();

            Stage aboutStage = new Stage();
            aboutStage.setTitle("About");
            aboutStage.setScene(new Scene(root));
            aboutStage.setResizable(false);
            aboutStage.initModality(Modality.APPLICATION_MODAL); // Makes the `aboutStage` window modal
            aboutStage.show();
        } catch (Exception e) {
            AlertBox alertBox = new AlertBox(AlertType.ERROR, "Error in `AlertBox#showAboutPage` of type `" + e.getClass() + "`.");
            alertBox.showAndWait();
        }
    }

    @FXML
    private void changeThemeCupertinoDark(ActionEvent actionEvent) {
        Application.setUserAgentStylesheet(new CupertinoDark().getUserAgentStylesheet());
    }

    @FXML
    private void changeThemeCupertinoLight(ActionEvent actionEvent) {
        Application.setUserAgentStylesheet(new CupertinoLight().getUserAgentStylesheet());
    }

    @FXML
    private void changeThemeDracula(ActionEvent actionEvent) {
        Application.setUserAgentStylesheet(new Dracula().getUserAgentStylesheet());
    }

    @FXML
    private void changeThemeNordDark(ActionEvent actionEvent) {
        Application.setUserAgentStylesheet(new NordDark().getUserAgentStylesheet());
    }

    @FXML
    private void changeThemeNordLight(ActionEvent actionEvent) {
        Application.setUserAgentStylesheet(new NordLight().getUserAgentStylesheet());
    }

    @FXML
    private void changeThemePrimerDark(ActionEvent actionEvent) {
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
    }

    @FXML
    private void changeThemePrimerLight(ActionEvent actionEvent) {
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
    }

    public void exportToCSV(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (PrintWriter writer = new PrintWriter(file)) {
                for (Person person : data) {
                    writer.println(person.toCSVFormat());
                }
            } catch (FileNotFoundException e) {
                AlertBox alertBox = new AlertBox(AlertType.ERROR, "File not found.");
                alertBox.showAndWait();
            }
        }
    }

    public void importFromCSV(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    Person person = Person.fromCSVFormat(line);
                    db.insertUser(person);
                }
            } catch (FileNotFoundException e) {
                AlertBox alertBox = new AlertBox(AlertType.ERROR, "File not found.");
                alertBox.showAndWait();
            }
            refreshTableView();
        }
    }
}
