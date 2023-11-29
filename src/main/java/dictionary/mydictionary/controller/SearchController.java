package dictionary.mydictionary.controller;


import dictionary.mydictionary.Model.TextToSpeech;
import dictionary.mydictionary.Model.Word;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.util.*;


public class SearchController extends GeneralController implements Initializable {

    @FXML
    protected Label searchLabel;

    @FXML
    protected VBox searchContent;

    @FXML
    protected AnchorPane searchPane;

    @FXML
    protected ListView<String> searchListView;

    @FXML
    protected VBox search;

    @FXML
    protected TextField searchTextField;

    @FXML
    protected WebView searchDefinitionView;

    @FXML
    protected Button bookmarkFalseButton;
    @FXML
    protected Button bookmarkTrueButton;
    @FXML
    protected Button deleteButton;


    public AnchorPane getSearchPane() {
        return searchPane;
    }

    public void setSearchPane(AnchorPane searchPane) {
        this.searchPane = searchPane;
    }

    protected void addBookmark(Word word) throws IOException {
        getCurrentDic().initKeys(getCurrentDic().getBookmarkNewWords());
        bookmarkFalseButton.setVisible(false);
        bookmarkTrueButton.setVisible(true);
        getCurrentDic().getBookmarkNewWords().put(word.getWord(), word);
        getCurrentDic().addWordToFile(word.getWord(), word.getDef(), getCurrentDic().getBOOKMARK_PATH());
    }

    protected void removeBookmark(Word word) throws IOException {
        bookmarkFalseButton.setVisible(true);
        bookmarkTrueButton.setVisible(false);
        getCurrentDic().getBookmarkNewWords().remove(word.getWord(), word);
        getCurrentDic().updateWordToFile(getCurrentDic().getBOOKMARK_PATH(), getCurrentDic().getBookmarkNewWords());
    }

    @FXML
    public void handleClickBookmarkButton() throws IOException {
        String spelling = searchLabel.getText();
        if (spelling.equals("")) {
            showWarningAlert();
            return;
        }
        int _index = Collections.binarySearch(getCurrentDic().initKeys(getCurrentDic().getNewWords()), spelling);
        int index = Collections.binarySearch(getCurrentDic().initKeys(getCurrentDic().getBookmarkNewWords()), spelling);
        if (index < 0) {
            addBookmark(getCurrentDic().getNewWords().get(getCurrentDic().initKeys(getCurrentDic().getNewWords()).get(Math.abs(_index))));
            System.out.println(index);
        } else {
            removeBookmark(getCurrentDic().getBookmarkNewWords().get(getCurrentDic().initKeys(getCurrentDic().getBookmarkNewWords()).get(index)));
            System.out.println(index);
        }
    }

    public void showWarningAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText("Không có từ nào được chọn!");
        alert.showAndWait();
    }

    public void pressedSpeaker(){
        TextToSpeech.playSoundGoogleTranslateEnToVi(searchLabel.getText());
    }

    public void init(){
        setSearchListView(this.searchListView);
        setDefinitionView(this.searchDefinitionView);
        setTextField(this.searchTextField);
        setLabel(this.searchLabel);
        initComponents(this.searchPane, getCurrentDic().getNewWords(), "#searchListView", "#searchDefinitionView");
        try {
            readData(getCurrentDic().getPATH(), getCurrentDic().getNewWords());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        loadWordList(getCurrentDic().getNewWords());
        enterKeyPressed(getCurrentDic().getNewWords());
        textFieldInput(getCurrentDic().getNewWords());
    }
}