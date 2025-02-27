package org.example;

import javafx.fxml.FXML;
import java.io.IOException;

public class FavoritesController {
    @FXML
    private void handleBack() throws IOException {
        Main.switchScene("MovieCatalog.fxml");
    }
}
