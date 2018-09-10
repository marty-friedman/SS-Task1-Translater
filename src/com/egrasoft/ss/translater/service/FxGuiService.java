package com.egrasoft.ss.translater.service;

import com.egrasoft.ss.translater.util.Constants;
import com.egrasoft.ss.translater.util.UserSaveSelection;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class FxGuiService {
    private static final String SETTINGS_VIEW_LOCATION = "translater/view/settings.fxml";

    private LocalizationService localizationService = LocalizationService.getInstance();

    public Stage loadSettingsFrame() throws IOException {
        URL view = getClass().getClassLoader().getResource(SETTINGS_VIEW_LOCATION);
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(view));
        loader.setResources(localizationService.getCurrentBundle());
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(localizationService.getString(Constants.Settings.SETTINGS_TITLE_KEY));
        stage.setScene(new Scene(root));
        return stage;
    }

    public UserSaveSelection askForSaving() {
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

    public Alert createMessageDialog(Alert.AlertType type, String title, String contentText) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(contentText);
        alert.setHeaderText(null);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        return alert;
    }

    public FileChooser createFileChooser(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(
                localizationService.getString(Constants.Dialogs.FILE_TEXT_EXTENSIONS_DESCRIPTION), "txt");
        fileChooser.setSelectedExtensionFilter(extensionFilter);
        return fileChooser;
    }

    public static FxGuiService getInstance() {
        return SingletonHelper.instance;
    }

    private FxGuiService() {}

    private static class SingletonHelper {
        private static final FxGuiService instance = new FxGuiService();
    }
}
