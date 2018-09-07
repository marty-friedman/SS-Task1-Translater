package com.egrasoft.ss.translater;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;

public class TranslaterApplication extends Application {
    private static final String FRAME_VIEW_LOCATION = "translater/view/frame.fxml";
    private static final String FRAME_TITLE = "translater";

    private static final Integer FRAME_WIDTH = 800;
    private static final Integer FRAME_HEIGHT = 500;

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL view = getClass().getClassLoader().getResource(FRAME_VIEW_LOCATION);
        Parent root = FXMLLoader.load(Objects.requireNonNull(view));
        primaryStage.setTitle(FRAME_TITLE);
        primaryStage.setScene(new Scene(root, FRAME_WIDTH, FRAME_HEIGHT));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
