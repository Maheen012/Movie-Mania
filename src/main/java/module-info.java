module Movie.Mania {
    requires javafx.controls; // JavaFX Controls module
    requires javafx.fxml; // JavaFX FXML module
    requires javafx.base; // JavaFX Base module
    requires javafx.graphics; // JavaFX Graphics module

    requires com.opencsv;
    requires java.desktop; // JUnit 5 parameterized tests

    exports org.example.controller; // Export your controller packages (or any other packages you want to expose)
    exports org.example.view; // Export view packages
    opens org.example.view to javafx.fxml; // Open specific packages to JavaFX (FXML usage)
}
