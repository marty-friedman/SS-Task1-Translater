package com.egrasoft.ss.translater.service;

import com.egrasoft.ss.translater.util.Constants;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class SettingsService {
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

    public static SettingsService getInstance() {
        return SingletonHelper.instance;
    }

    private SettingsService() {}

    private static class SingletonHelper {
        private static final SettingsService instance = new SettingsService();
    }
}
