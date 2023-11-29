package dictionary.mydictionary.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class ModifyWordController extends GeneralController {
    @FXML
    protected Button editButton;
    @FXML
    public void pressedEdit() {
//        String spelling = searchTextField.getText();
//        if (spelling.equals("")) {
//            searchController.showWarningAlert();
//            return;
//        }
//        if (isOnEditDefinition) {
//            isOnEditDefinition = false;
//            editDefinition.setVisible(false);
//            saveChangeButton.setVisible(false);
//            return;
//        }
//        isOnEditDefinition = true;
//        saveChangeButton.setVisible(true);
//        editDefinition.setVisible(true);
//        int index = Collections.binarySearch(getCurrentDic().getVocab(), new Word(spelling, null));
//        String meaning = getCurrentDic().getVocab().get(index).getMeaning();
//        editDefinition.setHtmlText(meaning);
    }

    @FXML
    public void pressedSave() throws IOException {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("Thông báo");
//        alert.setHeaderText(null);
//        alert.setContentText("Sửa từ thành công!");
//        alert.showAndWait();
//        editDefinition.setVisible(false);
//        isOnEditDefinition = false;
//        saveChangeButton.setVisible(false);
//        String newMeaning = editDefinition.getHtmlText().replace(" dir=\"ltr\"", "");
//        String spelling = searchField.getText();
//        saveWordToFile(getCurrentDic().getPATH(), getCurrentDic().getVocab(), spelling, newMeaning);
//        saveWordToFile(getCurrentDic().getHISTORY_PATH(), getCurrentDic().getHistoryVocab(), spelling, newMeaning);
//        saveWordToFile(getCurrentDic().getBOOKMARK_PATH(), getCurrentDic().getBookmarkVocab(), spelling, newMeaning);
//        definitionView.getEngine().loadContent(newMeaning, "text/html");
    }
}
