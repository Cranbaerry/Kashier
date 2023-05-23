module com.kashier {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.kashier to javafx.fxml;
    exports com.kashier;
}
