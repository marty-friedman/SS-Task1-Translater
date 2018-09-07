package com.egrasoft.ss.translater;

import com.egrasoft.ss.translater.controller.FrameController;
import com.egrasoft.ss.translater.service.LocalizationService;
import com.egrasoft.ss.translater.util.Constants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;

public class TranslaterApplication extends Application {
    private static final String FRAME_VIEW_LOCATION = "translater/view/frame.fxml";

    private LocalizationService localizationService = LocalizationService.getInstance();

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL view = getClass().getClassLoader().getResource(FRAME_VIEW_LOCATION);
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(view));
        Parent root = loader.load();
        ((FrameController) loader.getController()).setStage(primaryStage);
        primaryStage.setTitle(localizationService.getString(Constants.Frame.FRAME_TITLE_KEY));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
