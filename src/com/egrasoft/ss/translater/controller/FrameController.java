package com.egrasoft.ss.translater.controller;

import com.egrasoft.ss.translater.service.LocalizationService;
import com.egrasoft.ss.translater.service.impl.LocalizationServiceImpl;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class FrameController {
    private static final String ABOUT_TITLE_KEY = "frame.about.title";
    private static final String ABOUT_CONTENT_TEXT_KEY = "frame.about.contenttext";

    private LocalizationService localizationService = LocalizationServiceImpl.getInstance();

    @FXML
    private void doOpen() {
        //todo
    }

    @FXML
    private void doSave() {
        //todo
    }

    @FXML
    private void doSaveAs() {
        //todo
    }

    @FXML
    private void doClose() {
        //todo
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
        createAboutDialog().showAndWait();
    }

    private Alert createAboutDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(localizationService.getString(ABOUT_TITLE_KEY));
        alert.setHeaderText(null);
        alert.setContentText(localizationService.getString(ABOUT_CONTENT_TEXT_KEY));
        return alert;
    }
}
