module com.demo.kassensystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.demo.kassensystem to javafx.fxml;
    exports com.demo.kassensystem;
    exports com.demo.kassensystem.controller;
    opens com.demo.kassensystem.controller to javafx.fxml;
}