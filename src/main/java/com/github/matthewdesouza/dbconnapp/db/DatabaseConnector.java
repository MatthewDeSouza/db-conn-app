package com.github.matthewdesouza.dbconnapp.db;

import com.github.matthewdesouza.dbconnapp.db.model.Person;
import com.github.matthewdesouza.dbconnapp.db.model.Major;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnector {
    // Azure credits have run out, so I'm using a local MariaDB server instead.
    private static final String MYSQL_SERVER_URL = "jdbc:mariadb://localhost:3306/";
    private static final String DB_URL = "jdbc:mariadb://localhost:3306/project";
    private static final String DB_NAME = "project";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "mariadb";

    // Singleton instance
    private static DatabaseConnector instance;

    // Private constructor to prevent instantiation from outside
    private DatabaseConnector() {
        connectToDatabase();
    }

    // Public method to provide access to the singleton instance
    public static DatabaseConnector getInstance() {
        if (instance == null) {
            instance = new DatabaseConnector();
        }
        return instance;
    }

    private void connectToDatabase() {
        try {
            //First, connect to MYSQL server and create the database if not created
            try (Connection conn = DriverManager.getConnection(MYSQL_SERVER_URL, USERNAME, PASSWORD);
                 Statement statement = conn.createStatement()) {

                statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);

                //Second, connect to the database and create the table "people" if not created
                try (Connection conn2 = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                     Statement statement2 = conn2.createStatement()) {

                    String sql = "CREATE TABLE IF NOT EXISTS people ("
                            + "id INT( 10 ) NOT NULL PRIMARY KEY AUTO_INCREMENT,"
                            + "firstName VARCHAR(200) NOT NULL,"
                            + "lastName VARCHAR(200) NOT NULL,"
                            + "dept VARCHAR(200),"
                            + "major ENUM(" + Major.getMajorStringsSQLFormat() +")"
                            + ")";
                    statement2.executeUpdate(sql);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Person> getAllPeople() {
        List<Person> people = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM people")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String dept = resultSet.getString("dept");
                Major major = Major.valueOf(resultSet.getString("major"));

                people.add(new Person(id, firstName, lastName, dept, major));
            }
        } catch (SQLException e) {
            System.out.println("SQLException in `DatabaseConnector#getAllPeople()` occurred: " + e.getLocalizedMessage());
        }

        return people;
    }

    public void insertUser(Person person) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            String sql = "INSERT INTO people (firstName, lastName, dept, major) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, person.getFirstName());
                preparedStatement.setString(2, person.getLastName());
                preparedStatement.setString(3, person.getDept());
                preparedStatement.setString(4, person.getMajor().name());

                int row = preparedStatement.executeUpdate();

                if (row > 0) {
                    System.out.println("A new user was inserted successfully.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUser(Person person) {
        String sql = "UPDATE people SET firstName = ?, lastName = ?, dept = ?, major = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, person.getFirstName());
            preparedStatement.setString(2, person.getLastName());
            preparedStatement.setString(3, person.getDept());
            preparedStatement.setString(4, person.getMajor().name());
            preparedStatement.setInt(5, person.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(int id) {
        String sql = "DELETE FROM people WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
