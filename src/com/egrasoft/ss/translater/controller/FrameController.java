package com.egrasoft.ss.translater.controller;

import com.egrasoft.ss.translater.service.FileManagerService;
import com.egrasoft.ss.translater.service.LocalizationService;
import com.egrasoft.ss.translater.service.FxGuiService;
import com.egrasoft.ss.translater.service.TranslationService;
import com.egrasoft.ss.translater.util.Constants;
import com.egrasoft.ss.translater.util.UserSaveSelection;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class FrameController {
    private LocalizationService localizationService = LocalizationService.getInstance();
    private FileManagerService fileManagerService = FileManagerService.getInstance();
    private TranslationService translationService = TranslationService.getInstance();
    private FxGuiService fxGuiService = FxGuiService.getInstance();

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
            UserSaveSelection selection = fxGuiService.askForSaving();
            if (selection == UserSaveSelection.CANCEL || (selection == UserSaveSelection.SAVE && !doFileSave()))
                return;
        }

        File file = fxGuiService.createFileChooser(Constants.Dialogs.FILE_OPEN_TITLE_KEY).showOpenDialog(null);
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
            File file = fxGuiService.createFileChooser(Constants.Dialogs.FILE_SAVE_TITLE_KEY).showSaveDialog(null);
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
            fxGuiService.loadSettingsFrame().show();
        } catch (IOException e) {
            e.printStackTrace();
            Platform.exit();
        }
    }

    @FXML
    private void doTranslate() {
        if (currentFile.getValue() != null)
            fileTextArea.setText(translationService.translate(fileTextArea.getText()));
    }

    @FXML
    private void doAbout() {
        fxGuiService.createMessageDialog(Alert.AlertType.INFORMATION,
                localizationService.getString(Constants.Dialogs.ABOUT_TITLE_KEY),
                localizationService.getString(Constants.Dialogs.ABOUT_CONTENT_TEXT_KEY))
                .showAndWait();
    }

    private void openNewFile(File file) {
        try {
            String content = fileManagerService.getContent(file);
            fileTextArea.setVisible(true);
            fileTextArea.setText(content);
            currentFile.setValue(file);
            savedState.set(true);
        } catch (IOException exc) {
            fxGuiService.createMessageDialog(Alert.AlertType.ERROR,
                    localizationService.getString(Constants.Dialogs.FILE_ERROR_TITLE_KEY),
                    localizationService.getString(Constants.Dialogs.FILE_OPEN_ERROR_CONTENT_TEXT_KEY))
                    .showAndWait();
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
            fxGuiService.createMessageDialog(Alert.AlertType.ERROR,
                    localizationService.getString(Constants.Dialogs.FILE_ERROR_TITLE_KEY),
                    localizationService.getString(Constants.Dialogs.FILE_SAVE_ERROR_CONTENT_TEXT_KEY))
                    .showAndWait();
            return false;
        }
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
}
