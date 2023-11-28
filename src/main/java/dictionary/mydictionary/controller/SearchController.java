package dictionary.mydictionary.controller;


import dictionary.mydictionary.Model.Word;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class SearchController extends GeneralController implements Initializable {
    private static final String EV_FILE_PATH = "src/main/resources/dictionary/myevdictionary/data/E_V.txt";
    private static final String VE_FILE_PATH = "src/main/resources/dictionary/myevdictionary/data/V_E.txt";


    @FXML
    protected Label wordText;

    @FXML
    protected VBox content;

    @FXML
    protected AnchorPane searchPane;

    @FXML
    protected ListView<String> listView;

    @FXML
    protected VBox search;

    @FXML
    protected TextField searchbar;

    @FXML
    protected HBox hboxHome;

    @FXML
    protected WebView definitionView;

    @FXML
    protected Button bookmarkFalseButton;
    @FXML
    protected Button bookmarkTrueButton;
    @FXML
    protected Button deleteButton;
    @FXML
    protected Button editButton;

    public AnchorPane getSearchPane() {
        return searchPane;
    }

    public void setSearchPane(AnchorPane searchPane) {
        this.searchPane = searchPane;
    }

    public ListView<String> getListView() {
        return listView;
    }

    public void setListView(ListView<String> listView) {
        this.listView = listView;
    }

    public void initComponents(AnchorPane view, Map<String, Word> temp) {
        this.definitionView = (WebView) view.lookup("#definitionView");
        this.listView = (ListView<String>) view.lookup("#listView");
//        HomeViewController context = this;
        this.listView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    Word word = temp.get(newValue.trim());
                    String definition = word.getDef();
                    this.definitionView.getEngine().loadContent(definition, "text/html");
                    wordText.setText(word.getWord());
                }
        );
    }

    public void loadWordList(Map<String, Word> temp) {
        this.listView.getItems().addAll(temp.keySet());
    }

    public void readData(String path, Map<String, Word> temp) throws IOException {
        FileReader fis = new FileReader(path);
        BufferedReader br = new BufferedReader(fis);
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(SPLITTING_CHARACTERS);
            String word = parts[0];
            String definition = SPLITTING_CHARACTERS + parts[1];
            Word wordObj = new Word(word, definition);
            temp.put(word, wordObj);
        }
        TreeMap<String, Word> sorted = new TreeMap<>(temp);
        temp.clear();
        temp.putAll(sorted);
    }

    public Set<String> searching(String searchWords, Map<String, Word> temp) {
        Set<String> wordSet = temp.keySet();
        List<String> searchWordsArray = Arrays.asList(searchWords.trim().split(" "));

        return wordSet.stream().filter(input -> {
            return searchWordsArray.stream().allMatch(word -> input.toLowerCase().contains(word.toLowerCase()));
        }).collect(Collectors.toSet());
    }

    public void enterKeyPressed(TreeMap<String, Word> temp) {
        searchbar.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                this.listView.getItems().clear();
                this.listView.getItems().addAll(searching(searchbar.getText(), temp));
            }
        });
    }

    public void searchbarInput(TreeMap<String, Word> temp) {
        searchbar.setOnKeyPressed(event -> {
            this.listView.getItems().clear();
            this.listView.getItems().addAll(searching(searchbar.getText(), temp));
            if(searchbar.getText() == null){
                loadWordList(temp);
            }
        });
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
        String spelling = wordText.getText();
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

    public void init(){
        initComponents(searchPane, getCurrentDic().getNewWords());
        try {
            readData(getCurrentDic().getPATH(), getCurrentDic().getNewWords());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        loadWordList(getCurrentDic().getNewWords());
        enterKeyPressed(getCurrentDic().getNewWords());
        searchbarInput(getCurrentDic().getNewWords());
    }
}