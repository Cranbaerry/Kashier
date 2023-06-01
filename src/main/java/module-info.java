module com.kashier {
    requires javafx.controls;
    requires javafx.fxml;
    requires core;
    requires modelmapper;
    requires modelmapper.gson;
    requires java.sql;


    opens com.kashier to javafx.fxml;
    exports com.kashier;
    exports com.kashier.models;
    opens com.kashier.models to javafx.fxml, com.google.gson, core;
    exports com.kashier.controllers;
    opens com.kashier.controllers to javafx.fxml, com.google.gson;
    //    exports com.kashier.placeholders;
    // opens com.kashier.placeholders to javafx.fxml;
}
