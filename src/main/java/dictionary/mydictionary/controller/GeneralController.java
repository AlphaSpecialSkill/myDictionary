package dictionary.mydictionary.controller;

import dictionary.mydictionary.Model.NewDictionary;
import dictionary.mydictionary.Model.TextToSpeech;
import dictionary.mydictionary.Model.Word;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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

    protected final ObservableMap<String, String> searchMap = FXCollections.observableHashMap();
    @FXML
    protected Label searchLabel;
    @FXML
    protected AnchorPane searchPane;
    @FXML
    protected Button saveButton;

    @FXML
    protected Button transLangEV;

    @FXML
    protected Button transLangVE;
    @FXML
    protected TextField searchTextField;
    @FXML
    protected ListView<String> searchListView;
    @FXML
    protected WebView searchDefinitionView;
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

    public void setSearchListView(ListView<String> searchListView) {
        this.searchListView = searchListView;
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

    public void initComponents(AnchorPane view, Map<String, Word> temp, String searchListViewId, String definitionViewId) {
        this.definitionView = (WebView) view.lookup(definitionViewId);
        this.searchListView = (ListView<String>) view.lookup(searchListViewId);
        this.searchListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        Word word = temp.get(newValue.trim());
                        if (word != null) {
                            String definition = word.getDef();
                            this.definitionView.getEngine().loadContent(definition, "text/html");
                            label.setText(word.getWord());
                        } else {
                            // Handle the case where the Word object is null
                            System.out.println("Selected word is null");
                        }
                    } else {
                        // Handle the case where newValue is null
                        System.out.println("Selected value is null");
                    }
                }
        );
    }

    public void loadWordList(Map<String, Word> temp) {
        this.searchListView.getItems().addAll(temp.keySet());
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

    public void enterKeyPressed(Map<String, Word> temp) {
        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                this.searchListView.getItems().clear();
                this.searchListView.getItems().addAll(searching(textField.getText(), temp));
            }
        });
    }

    public void textFieldInput(Map<String, Word> temp) {
        textField.setOnKeyPressed(event -> {
            this.searchListView.getItems().clear();
            this.searchListView.getItems().addAll(searching(textField.getText(), temp));
            if (textField.getText() == null) {
                loadWordList(temp);
            }
        });
    }

    public void clearPane() throws IOException {
        searchTextField.clear();
        definitionView.getEngine().loadContent("");
        searchMap.clear();
        this.searchListView.getItems().clear();
        searchLabel.setText("Nghĩa của từ");
        initComponents(this.searchPane, getCurrentDic().getNewWords(), "#searchListView", "#searchDefinitionView");
        readData(getCurrentDic().getPATH(), getCurrentDic().getNewWords());
        for (Map.Entry<String, Word> entry : getCurrentDic().getNewWords().entrySet()) {
            Word temp = entry.getValue();
            searchMap.put(temp.getWord(), temp.getDef());
        }
        searchListView.getItems().addAll(searchMap.keySet());
    }


    public void pressedSpeaker() {
        if (isEVDic) {
            TextToSpeech.playSoundGoogleTranslateEnToVi(searchLabel.getText());
        } else {
            TextToSpeech.playSoundGoogleTranslateViToEn(searchLabel.getText());
        }
    }

    public void setLanguage() {
        if (!isEVDic) {
            transLangEV.setVisible(false);
            transLangVE.setVisible(true);
        } else {
            transLangEV.setVisible(true);
            transLangVE.setVisible(false);
        }
    }

    @FXML
    public void clickTransBtn() throws IOException {
        isEVDic = !isEVDic;
        setLanguage();
        clearPane();
    }

    public NewDictionary getCurrentDic() {
        if (isEVDic)
            return evDic;
        else return veDic;
    }

    @FXML
    public void pressedDelete() throws IOException {
//        String spelling = searchTextField.getText();
//        if (spelling.equals("")) {
//            searchController.showWarningAlert();
//            return;
//        }
//        ButtonType yes = new ButtonType("Có", ButtonBar.ButtonData.OK_DONE);
//        ButtonType no = new ButtonType("Không", ButtonBar.ButtonData.CANCEL_CLOSE);
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Bạn có chắc chắn muốn xoá từ này không?", yes, no);
//        alert.setTitle("Thông báo");
//        alert.setHeaderText(null);
//        alert.showAndWait();
//
//        if (alert.getResult() == yes) {
//            getCurrentDic().removeWord(spelling, getCurrentDic().getPATH(), getCurrentDic().getNewWords());
//            getCurrentDic().removeWord(spelling, getCurrentDic().getHISTORY_PATH(), getCurrentDic().getHistoryNewWords());
//            getCurrentDic().removeWord(spelling, getCurrentDic().getBOOKMARK_PATH(), getCurrentDic().getBookmarkNewWords());
//            searchLabel.setText("Nghĩa của từ");
//            searchTextField.clear();
//            definitionView.getEngine().loadContent("");
//        }
    }

    private void saveWordToFile(String path, ArrayList<Word> temp, String spelling, String meaning) {
//        int index = Collections.binarySearch(temp, new Word(spelling, null));
//        if (index >= 0) {
//            temp.get(index).setMeaning(meaning);
//            getCurrentDic().updateWordToFile(path, temp);
//        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}