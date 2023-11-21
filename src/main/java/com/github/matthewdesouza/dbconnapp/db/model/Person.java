package com.github.matthewdesouza.dbconnapp.db.model;

public class Person {
    private Integer id;
    private String firstName;
    private String lastName;
    private String dept;
    private Major major;

    public Person() {
    }


    public Person(Integer id, String firstName, String lastName, String dept, Major major) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dept = dept;
        this.major = major;
    }

    public static Person fromCSVFormat(String csvString) {
        String[] data = csvString.split(",", -1); // Split with limit to include trailing empty strings

        // Assuming the CSV format is id, firstName, lastName, department, major
        Integer id = data[0].isEmpty() ? null : Integer.parseInt(data[0]);
        String firstName = data[1];
        String lastName = data[2];
        String department = data[3];
        String major = data[4];

        return new Person(id, firstName, lastName, department, Major.valueOf(major));
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public Major getMajor() {
        return major;
    }

    public void setMajor(Major major) {
        this.major = major;
    }


    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String toCSVFormat() {
        StringBuilder sb = new StringBuilder();

        sb.append(id != null ? id : "").append(",");
        sb.append(escapeSpecialCharacters(firstName)).append(",");
        sb.append(escapeSpecialCharacters(lastName)).append(",");
        sb.append(escapeSpecialCharacters(dept)).append(",");
        sb.append(escapeSpecialCharacters(major.toString()));

        return sb.toString();
    }

    // Helper method to escape special characters in CSV
    private String escapeSpecialCharacters(String data) {
        String escapedData = data.replace("\"", "\"\"");
        if (data.contains(",") || data.contains("\"") || data.contains("\n")) {
            data = "\"" + escapedData + "\"";
        }
        return data;
    }

}
