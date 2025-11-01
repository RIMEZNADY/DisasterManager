module com.project.jaba24 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.sql;
    requires mysql.connector.j;
    requires jbcrypt;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires java.prefs;

    opens com.project.jaba24 to javafx.fxml;
    exports com.project.jaba24;
    exports com.project.jaba24.controllers;
    opens com.project.jaba24.controllers to javafx.fxml;
}