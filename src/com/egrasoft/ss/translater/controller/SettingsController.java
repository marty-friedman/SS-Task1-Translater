package com.egrasoft.ss.translater.controller;

import com.egrasoft.ss.translater.service.TranslationService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;

import java.util.AbstractMap;
import java.util.Map;

public class SettingsController {
    private TranslationService translationService = TranslationService.getInstance();

    private ObservableList<Map.Entry<String, String>> rulesEdited = FXCollections.observableArrayList();

    @FXML
    private TableView<Map.Entry<String, String>> rulesTable;
    @FXML
    private TableColumn<Map.Entry<String, String>, String> sourceColumn;
    @FXML
    private TableColumn<Map.Entry<String, String>, String> targetColumn;

    @FXML
    private void initialize() {
        rulesEdited.addAll(translationService.getRules().entrySet());
        sourceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKey()));
        sourceColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        targetColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue()));
        targetColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        rulesTable.setOnKeyPressed(keyEvent -> {
            Map.Entry<String, String> selectedItem = rulesTable.getSelectionModel().getSelectedItem();
            if (selectedItem != null && keyEvent.getCode() == KeyCode.DELETE)
                rulesEdited.remove(selectedItem);
        });
        rulesTable.setItems(rulesEdited);
    }

    @FXML
    private void doSettingsAddRule() {
        rulesEdited.add(new AbstractMap.SimpleEntry<>("sample", "sample"));
    }

    @FXML
    private void doSettingsOk() {

    }

    @FXML
    private void doSettingsCancel() {

    }
}
