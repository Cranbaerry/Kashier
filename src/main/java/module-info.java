module com.kashier {
    requires javafx.controls;
    requires javafx.fxml;
    requires core;
    requires java.sql;
    requires dbr;
    requires uk.co.caprica.vlcj;
    requires uk.co.caprica.vlcj.javafx;
    requires javafx.swing;


    opens com.kashier to javafx.fxml;
    exports com.kashier;
    exports com.kashier.models;
    opens com.kashier.models to javafx.fxml, core;
    exports com.kashier.controllers;
    opens com.kashier.controllers to javafx.fxml;
    //    exports com.kashier.placeholders;
    // opens com.kashier.placeholders to javafx.fxml;
}
