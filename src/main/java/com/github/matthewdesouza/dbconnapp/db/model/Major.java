package com.github.matthewdesouza.dbconnapp.db.model;

public enum Major {
    NIL("N/A"),
    CS("Computer Science"),
    IT("Information Technology"),
    SE("Software Engineering"),
    EE("Electrical Engineering"),
    ME("Mechanical Engineering"),
    CE("Civil Engineering"),
    CHE("Chemical Engineering"),
    BME("Biomedical Engineering"),
    AE("Aerospace Engineering"),
    IE("Industrial Engineering"),
    ECE("Electrical and Computer Engineering"),
    CSE("Computer Science and Engineering");

    private final String displayName;

    Major(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public static Major[] getMajors() {
        return new Major[] {
            NIL,
            CS,
            IT,
            SE,
            EE,
            ME,
            CE,
            CHE,
            BME,
            AE,
            IE,
            ECE,
            CSE
        };
    }

    /**
     * Returns all major string, separated by commas
     * @return String of all major strings
     */
    public static String getMajorStringsSQLFormat() {
        StringBuffer sb = new StringBuffer();
        for (Major m : Major.getMajors()) {
            sb.append("'");
            sb.append(m.name());
            sb.append("'");
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length()); // Remove trailing comma and space
        return sb.toString();
    }
}
