package dictionary.mydictionary.controller;

import dictionary.mydictionary.Model.NewDictionary;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class GeneralController extends MainController implements Initializable {
    private static final String EV_FILE_PATH = "src/main/resources/dictionary/mydictionary/data/E_V.txt";
    private static final String EV_HISTORY_PATH = "src/main/resources/dictionary/mydictionary/data/E_V_Recent.txt";
    private static final String EV_BOOKMARK_PATH = "src/main/resources/dictionary/mydictionary/data/E_V_Bookmark.txt";
    private static final String VE_FILE_PATH = "src/main/resources/dictionary/mydictionary/data/V_E.txt";
    private static final String VE_HISTORY_PATH = "src/main/resources/dictionary/mydictionary/data/V_E_Recent.txt";
    private static final String VE_BOOKMARK_PATH = "src/main/resources/dictionary/mydictionary/data/V_E_Bookmark.txt";

    protected static NewDictionary evDic = new NewDictionary(EV_FILE_PATH, EV_HISTORY_PATH, EV_BOOKMARK_PATH);
    protected static NewDictionary veDic = new NewDictionary(VE_FILE_PATH, VE_HISTORY_PATH, VE_BOOKMARK_PATH);

    public static final String SPLITTING_CHARACTERS = "<html>";

    private static boolean isEVDic = true;

    public NewDictionary getCurrentDic(){
        if (isEVDic)
            return evDic;
        return veDic;
    }

    public boolean isContain(String str1, String str2) {
        for (int i = 0; i < Math.min(str1.length(), str2.length()); i++) {
            if (str1.charAt(i) > str2.charAt(i)) {
                return false;
            } else if (str1.charAt(i) < str2.charAt(i)) {
                return false;
            }
        }
        if (str1.length() > str2.length()) {
            return false;
        }
        return true;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}