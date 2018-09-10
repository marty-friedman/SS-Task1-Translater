package com.egrasoft.ss.translater.controller;

import com.egrasoft.ss.translater.service.FileManagerService;
import com.egrasoft.ss.translater.service.LocalizationService;
import com.egrasoft.ss.translater.service.SettingsService;
import com.egrasoft.ss.translater.service.TranslationService;
import com.egrasoft.ss.translater.util.Constants;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class FrameController {
    private LocalizationService localizationService = LocalizationService.getInstance();
    private FileManagerService fileManagerService = FileManagerService.getInstance();
    private TranslationService translationService = TranslationService.getInstance();
    private SettingsService settingsService = SettingsService.getInstance();

    private Property<File> currentFile = new SimpleObjectProperty<>();
    private BooleanProperty savedState = new SimpleBooleanProperty();

    @FXML
    private TextArea fileTextArea;
    private Stage stage;

    @FXML
    private void initialize() {
        fileTextArea.textProperty().addListener((observable, oldValue, newValue) -> savedState.set(false));
        savedState.addListener((observable, oldValue, newValue) -> updateFrameTitle());
        currentFile.addListener((observable, oldValue, newValue) -> updateFrameTitle());
    }

    @FXML
    private void doFileOpen() {
        if (currentFile.getValue() != null && !savedState.get()) {
            UserSaveSelection selection = askForSaving();
            if (selection == UserSaveSelection.CANCEL || (selection == UserSaveSelection.SAVE && !doFileSave()))
                return;
        }

        File file = createFileChooser(Constants.Dialogs.FILE_OPEN_TITLE_KEY).showOpenDialog(null);
        if (file != null)
            openNewFile(file);
    }

    @FXML
    private boolean doFileSave() {
        if (currentFile.getValue() != null)
            return saveCurrentFile();
        return false;
    }

    @FXML
    private void doFileSaveAs() {
        if (currentFile.getValue() != null) {
            File file = createFileChooser(Constants.Dialogs.FILE_SAVE_TITLE_KEY).showSaveDialog(null);
            if (file != null)
                saveToFile(file);
        }
    }

    @FXML
    private void doClose() {
        Platform.exit();
    }

    @FXML
    private void doEditTranslationSettings() {
        try {
            settingsService.loadSettingsFrame().show();
        } catch (IOException e) {
            e.printStackTrace();
            Platform.exit();
        }
    }

    @FXML
    private void doTranslate() {
        fileTextArea.setText(translationService.translate(fileTextArea.getText()));
    }

    @FXML
    private void doAbout() {
        createMessageDialog(Alert.AlertType.INFORMATION, Constants.Dialogs.ABOUT_TITLE_KEY, Constants.Dialogs.ABOUT_CONTENT_TEXT_KEY).showAndWait();
    }

    private UserSaveSelection askForSaving() {
        ButtonBar.ButtonData buttonData =  createAskForSavingDialog().showAndWait()
                .map(ButtonType::getButtonData).orElse(ButtonBar.ButtonData.CANCEL_CLOSE);
        switch (buttonData) {
            case YES:
                return UserSaveSelection.SAVE;
            case NO:
                return UserSaveSelection.DONT_SAVE;
            default:
                return UserSaveSelection.CANCEL;
        }
    }

    private void openNewFile(File file) {
        try {
            String content = fileManagerService.getContent(file);
            fileTextArea.setVisible(true);
            fileTextArea.setText(content);
            currentFile.setValue(file);
            savedState.set(true);
        } catch (IOException exc) {
            createMessageDialog(Alert.AlertType.ERROR, Constants.Dialogs.FILE_ERROR_TITLE_KEY, Constants.Dialogs.FILE_OPEN_ERROR_CONTENT_TEXT_KEY).showAndWait();
        }
    }

    private boolean saveCurrentFile() {
        return saveToFile(currentFile.getValue());
    }

    private boolean saveToFile(File file) {
        try {
            fileManagerService.saveContent(file, fileTextArea.getText());
            currentFile.setValue(file);
            savedState.set(true);
            return true;
        } catch (IOException exc) {
            createMessageDialog(Alert.AlertType.ERROR, Constants.Dialogs.FILE_ERROR_TITLE_KEY, Constants.Dialogs.FILE_SAVE_ERROR_CONTENT_TEXT_KEY).showAndWait();
            return false;
        }
    }

    private Alert createMessageDialog(Alert.AlertType type, String titleKey, String contentTextKey) {
        Alert alert = new Alert(type);
        alert.setTitle(localizationService.getString(titleKey));
        alert.setContentText(localizationService.getString(contentTextKey));
        alert.setHeaderText(null);
        return alert;
    }

    private Alert createAskForSavingDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(localizationService.getString(Constants.Dialogs.ASK_FOR_SAVING_TITLE_KEY));
        alert.setHeaderText(null);
        alert.setContentText(localizationService.getString(Constants.Dialogs.ASK_FOR_SAVING_CONTENT_TEXT_KEY));

        ButtonType saveButtonType = new ButtonType(localizationService.getString(Constants.Dialogs.ASK_FOR_SAVING_SAVE_KEY),
                ButtonBar.ButtonData.YES);
        ButtonType dontSaveButtonType = new ButtonType(localizationService.getString(Constants.Dialogs.ASK_FOR_SAVING_DONT_SAVE_KEY),
                ButtonBar.ButtonData.NO);
        ButtonType cancelButtonType = new ButtonType(localizationService.getString(Constants.Dialogs.ASK_FOR_SAVING_CANCEL_KEY),
                ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(saveButtonType, dontSaveButtonType, cancelButtonType);

        return alert;
    }

    private FileChooser createFileChooser(String titleKey) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(localizationService.getString(titleKey));
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(
                localizationService.getString(Constants.Dialogs.FILE_TEXT_EXTENSIONS_DESCRIPTION), "txt");
        fileChooser.setSelectedExtensionFilter(extensionFilter);
        return fileChooser;
    }

    private void updateFrameTitle() {
        if (currentFile.getValue() != null) {
            String title = localizationService.getString(Constants.Frame.FRAME_TITLE_KEY) + " (" + currentFile.getValue().getName() + ")";
            if (!savedState.get())
                title += "*";
            stage.setTitle(title);
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private enum UserSaveSelection {CANCEL, SAVE, DONT_SAVE}
}
