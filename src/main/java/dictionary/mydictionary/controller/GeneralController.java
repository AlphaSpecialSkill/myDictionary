package dictionary.mydictionary.controller;

import dictionary.mydictionary.Model.NewDictionary;
import dictionary.mydictionary.Model.Word;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class GeneralController extends MainController implements Initializable {
    private static final String EV_FILE_PATH = "src/main/resources/dictionary/mydictionary/data/E_V.txt";
    private static final String EV_HISTORY_PATH = "src/main/resources/dictionary/mydictionary/data/E_V_Recent.txt";
    private static final String EV_BOOKMARK_PATH = "src/main/resources/dictionary/mydictionary/data/E_V_Bookmark.txt";
    private static final String VE_FILE_PATH = "src/main/resources/dictionary/mydictionary/data/V_E.txt";
    private static final String VE_HISTORY_PATH = "src/main/resources/dictionary/mydictionary/data/V_E_Recent.txt";
    private static final String VE_BOOKMARK_PATH = "src/main/resources/dictionary/mydictionary/data/V_E_Bookmark.txt";

    @FXML
    private ListView<String> listView;
    @FXML
    private WebView definitionView;
    @FXML
    private TextField textField;
    @FXML
    private Label label;

    protected static NewDictionary evDic = new NewDictionary(EV_FILE_PATH, EV_HISTORY_PATH, EV_BOOKMARK_PATH);

    protected static NewDictionary veDic = new NewDictionary(VE_FILE_PATH, VE_HISTORY_PATH, VE_BOOKMARK_PATH);
    public static final String SPLITTING_CHARACTERS = "<html>";

    private static boolean isEVDic = true;

    public void setListView(ListView<String> listView) {
        this.listView = listView;
    }

    public void setDefinitionView(WebView definitionView) {
        this.definitionView = definitionView;
    }

    public void setTextField(TextField textField) {
        this.textField = textField;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public NewDictionary getCurrentDic(){
        if (isEVDic)
            return evDic;
        return veDic;
    }

    public void initComponents(AnchorPane view, Map<String, Word> temp, String listViewId, String definitionViewId) {
        this.definitionView = (WebView) view.lookup(definitionViewId);
        this.listView = (ListView<String>) view.lookup(listViewId);
        this.listView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    Word word = temp.get(newValue.trim());
                    String definition = word.getDef();
                    this.definitionView.getEngine().loadContent(definition, "text/html");
                    label.setText(word.getWord());
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
        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                this.listView.getItems().clear();
                this.listView.getItems().addAll(searching(textField.getText(), temp));
            }
        });
    }

    public void textFieldInput(TreeMap<String, Word> temp) {
        textField.setOnKeyPressed(event -> {
            this.listView.getItems().clear();
            this.listView.getItems().addAll(searching(textField.getText(), temp));
            if(textField.getText() == null){
                loadWordList(temp);
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}