package dictionary.mydictionary.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    protected BorderPane borderPane;
    @FXML
    protected AnchorPane root;

    @FXML
    protected Button homebtn;
    @FXML
    protected Button bookmarkbtn;
    @FXML
    protected Button googlebtn;
    @FXML
    protected Button historybtn;

    @FXML
    protected VBox navBar;

    @FXML
    protected SearchController searchController;
    @FXML
    protected BookmarkController bookmarkController;
    @FXML
    protected HistoryController historyController;

    @FXML
    public void pressedSearch(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("search-view.fxml"));
        AnchorPane searchPane = loader.load();
        borderPane.setCenter(searchPane);

        searchController = loader.getController();
        searchController.init();
    }

    @FXML
    void pressedHistory(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader((getClass().getResource("history-view.fxml")));
        AnchorPane historyPage = loader.load();
        borderPane.setCenter(historyPage);

        historyController = loader.getController();
        historyController.init();
    }

    @FXML
    void pressedBookmark(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("bookmark-view.fxml"));
        AnchorPane bookmarkPane = loader.load();
        borderPane.setCenter(bookmarkPane);

        bookmarkController = loader.getController();
        bookmarkController.init();
    }

    @FXML
    void pressedGoogle(MouseEvent event) throws IOException {
        AnchorPane APIpane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("googleAPI-view.fxml")));
        borderPane.setCenter(APIpane);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("search-view.fxml"));
        AnchorPane searchPane = null;
        try {
            searchPane = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        borderPane.setCenter(searchPane);

        searchController = loader.getController();
        searchController.init();
    }
}