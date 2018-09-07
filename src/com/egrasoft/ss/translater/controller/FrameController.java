package com.egrasoft.ss.translater.controller;

import com.egrasoft.ss.translater.service.FileManagerService;
import com.egrasoft.ss.translater.service.LocalizationService;
import com.egrasoft.ss.translater.service.impl.FileManagerServiceImpl;
import com.egrasoft.ss.translater.service.impl.LocalizationServiceImpl;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

public class FrameController {
    private static final String ABOUT_TITLE_KEY = "frame.about.title";
    private static final String ABOUT_CONTENT_TEXT_KEY = "frame.about.contenttext";
    private static final String FILE_ERROR_TITLE_KEY = "frame.file.error.title";
    private static final String FILE_OPEN_DIALOG_TITLE_KEY = "frame.file.open.title";
    private static final String FILE_OPEN_ERROR_CONTENT_TEXT_KEY = "frame.file.open.error.contenttext";
    private static final String ASK_FOR_SAVING_TITLE_KEY = "frame.askforsaving.title";
    private static final String ASK_FOR_SAVING_CONTENT_TEXT_KEY = "frame.askforsaving.contenttext";
    private static final String ASK_FOR_SAVING_CANCEL_KEY = "frame.askforsaving.cancel";
    private static final String ASK_FOR_SAVING_SAVE_KEY = "frame.askforsaving.save";
    private static final String ASK_FOR_SAVING_DONT_SAVE_KEY = "frame.askforsaving.dontsave";
    private static final String FILE_DIALOG_TEXT_EXTENSIONS_DESCRIPTION = "frame.file.textextensions.description";

    private LocalizationService localizationService = LocalizationServiceImpl.getInstance();
    private FileManagerService fileManagerService = FileManagerServiceImpl.getInstance();

    private File currentFile;
    private boolean savedState;

    @FXML
    private TextArea fileTextArea;

    @FXML
    private void initialize() {
        fileTextArea.textProperty().addListener((observable, oldValue, newValue) -> savedState = false);
    }

    @FXML
    private void doOpen() {
        if (currentFile != null && !savedState) {
            UserSaveSelection selection = askForSaving();
            if (selection == UserSaveSelection.CANCEL || (selection == UserSaveSelection.SAVE && !doSave()))
                return;
        }

        File file = createFileChooser(FILE_OPEN_DIALOG_TITLE_KEY).showOpenDialog(null);
        if (file != null)
            openNewFile(file);
    }

    @FXML
    private boolean doSave() {
        //todo
        return false;
    }

    @FXML
    private void doSaveAs() {
        //todo
    }

    @FXML
    private void doClose() {
        Platform.exit();
    }

    @FXML
    private void doTranslationSettings() {
        //todo
    }

    @FXML
    private void doTranslate() {
        //todo
    }

    @FXML
    private void doAbout() {
        createMessageDialog(Alert.AlertType.INFORMATION, ABOUT_TITLE_KEY, ABOUT_CONTENT_TEXT_KEY).showAndWait();
    }

    private UserSaveSelection askForSaving() {
        return createAskForSavingDialog().showAndWait()
                .map(ButtonType::getButtonData)
                .map(buttonData -> {
                    if (buttonData.equals(ButtonBar.ButtonData.YES))
                        return UserSaveSelection.SAVE;
                    else if (buttonData.equals(ButtonBar.ButtonData.NO))
                        return UserSaveSelection.DONT_SAVE;
                    return UserSaveSelection.CANCEL;
                }).orElse(UserSaveSelection.CANCEL);
    }

    private void openNewFile(File file) {
        try {
            String content = fileManagerService.getContent(file);
            fileTextArea.setVisible(true);
            fileTextArea.setText(content);
            currentFile = file;
            savedState = true;
        } catch (IOException exc) {
            createMessageDialog(Alert.AlertType.ERROR, FILE_ERROR_TITLE_KEY, FILE_OPEN_ERROR_CONTENT_TEXT_KEY).showAndWait();
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
        alert.setTitle(localizationService.getString(ASK_FOR_SAVING_TITLE_KEY));
        alert.setHeaderText(null);
        alert.setContentText(localizationService.getString(ASK_FOR_SAVING_CONTENT_TEXT_KEY));

        ButtonType saveButtonType = new ButtonType(localizationService.getString(ASK_FOR_SAVING_SAVE_KEY),
                ButtonBar.ButtonData.YES);
        ButtonType dontSaveButtonType = new ButtonType(localizationService.getString(ASK_FOR_SAVING_DONT_SAVE_KEY),
                ButtonBar.ButtonData.NO);
        ButtonType cancelButtonType = new ButtonType(localizationService.getString(ASK_FOR_SAVING_CANCEL_KEY),
                ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(saveButtonType, dontSaveButtonType, cancelButtonType);

        return alert;
    }

    private FileChooser createFileChooser(String titleKey) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(localizationService.getString(titleKey));
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(
                localizationService.getString(FILE_DIALOG_TEXT_EXTENSIONS_DESCRIPTION), "txt");
        fileChooser.setSelectedExtensionFilter(extensionFilter);
        return fileChooser;
    }

    private enum UserSaveSelection {CANCEL, SAVE, DONT_SAVE}
}
