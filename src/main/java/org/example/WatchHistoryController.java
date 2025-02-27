package org.example;

import javafx.fxml.FXML;
import java.io.IOException;

public class WatchHistoryController {
    @FXML
    private void handleBack() throws IOException {
        Main.switchScene("MovieCatalog.fxml");
    }
}
