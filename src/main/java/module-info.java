module dictionary.mydictionary {
    requires javafx.controls;
    requires javafx.fxml;
    requires jlayer;
    requires javafx.web;


    opens dictionary.mydictionary to javafx.fxml;
    exports dictionary.mydictionary;
}