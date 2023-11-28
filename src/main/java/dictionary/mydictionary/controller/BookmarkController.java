package dictionary.mydictionary.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;

public class BookmarkController extends GeneralController{

    @FXML
    protected VBox bookmarkContent;

    @FXML
    protected ListView<String> bookmarkListView;

    @FXML
    protected AnchorPane bookmarkPane;

    @FXML
    protected VBox bookmarkSearch;

    @FXML
    protected TextField bookmarkSearchBar;

    @FXML
    protected WebView bookmarkDefinitionView;

    @FXML
    protected Button bookmarkDeleteButton;

    @FXML
    protected Button bookmarkEditButton;

    @FXML
    protected Label bookmarkWordText;
}