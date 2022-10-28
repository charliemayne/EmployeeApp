module application.finalproject_ist311 {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;

    opens application.finalproject_ist311 to javafx.fxml;
    exports application.finalproject_ist311;
}