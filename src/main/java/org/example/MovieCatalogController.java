package org.example;

import javafx.fxml.FXML;
import java.io.IOException;

public class MovieCatalogController {

    @FXML
    private void handleSearch() {
        System.out.println("Search button clicked");
    }

    @FXML
    private void handleFavorites() throws IOException {
        Main.switchScene("FavoritesScreen.fxml");
    }

    @FXML
    private void handleHistory() throws IOException {
        Main.switchScene("fxml/WatchHistoryScreen.fxml");
    }

    @FXML
    private void handleLogout() throws IOException {
        Main.switchScene("LoginScreen.fxml");
    }
}
