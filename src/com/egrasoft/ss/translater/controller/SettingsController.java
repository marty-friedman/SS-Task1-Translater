package com.egrasoft.ss.translater.controller;

import com.egrasoft.ss.translater.service.FxGuiService;
import com.egrasoft.ss.translater.service.LocalizationService;
import com.egrasoft.ss.translater.service.TranslationService;
import com.egrasoft.ss.translater.throwable.RuleParseException;
import com.egrasoft.ss.translater.util.Constants;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SettingsController {
    private TranslationService translationService = TranslationService.getInstance();
    private LocalizationService localizationService = LocalizationService.getInstance();
    private FxGuiService fxGuiService = FxGuiService.getInstance();

    private ObservableList<Map.Entry<ObservableValue<String>, ObservableValue<String>>> rulesEdited = FXCollections.observableArrayList();

    @FXML
    private TableView<Map.Entry<ObservableValue<String>, ObservableValue<String>>> rulesTable;
    @FXML
    private TableColumn<Map.Entry<ObservableValue<String>, ObservableValue<String>>, String> sourceColumn;
    @FXML
    private TableColumn<Map.Entry<ObservableValue<String>, ObservableValue<String>>, String> targetColumn;

    @FXML
    private void initialize() {
        updateTable();
        sourceColumn.setCellValueFactory(cellData -> cellData.getValue().getKey());
        sourceColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        targetColumn.setCellValueFactory(cellData -> cellData.getValue().getValue());
        targetColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        rulesTable.setOnKeyPressed(keyEvent -> {
            Map.Entry<ObservableValue<String>, ObservableValue<String>> selectedItem = rulesTable.getSelectionModel().getSelectedItem();
            if (selectedItem != null && keyEvent.getCode() == KeyCode.DELETE)
                rulesEdited.remove(selectedItem);
        });
    }

    @FXML
    private void doSettingsAddRule() {
        rulesEdited.add(new AbstractMap.SimpleEntry<>(new SimpleStringProperty("sample"),
                new SimpleStringProperty("sample")));
    }

    @FXML
    private void doSettingsOk() {
        Map<String, String> newRules = rulesEdited.stream().collect(Collectors.toMap(entry -> entry.getKey().getValue(),
                entry -> entry.getValue().getValue()));
        try {
            translationService.updateRules(newRules);
            getWindow().close();
        } catch (RuleParseException e) {
            fxGuiService.createMessageDialog(Alert.AlertType.ERROR,
                    localizationService.getString(Constants.Settings.SETTINGS_ERROR_TITLE_KEY),
                    localizationService.getString(Constants.Settings.SETTINGS_RULE_ERROR_CONTENT_TEXT_KEY) +
                            "\n " + e.getRuleDescription())
                    .showAndWait();
            updateTable();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            fxGuiService.createMessageDialog(Alert.AlertType.ERROR,
                    localizationService.getString(Constants.Settings.SETTINGS_ERROR_TITLE_KEY),
                    localizationService.getString(Constants.Settings.SETTINGS_UNKNOWN_ERROR_CONTENT_TEXT_KEY))
                    .showAndWait();
            updateTable();
        }
    }

    @FXML
    private void doSettingsCancel() {
        getWindow().close();
    }

    private Stage getWindow() {
        return (Stage) rulesTable.getScene().getWindow();
    }

    private void updateTable() {
        List<Map.Entry<ObservableValue<String>, ObservableValue<String>>> observableValues = translationService.getRules().entrySet().stream()
                .map(entry -> new AbstractMap.SimpleEntry<ObservableValue<String>, ObservableValue<String>>(
                        new SimpleStringProperty(entry.getKey()), new SimpleStringProperty(entry.getValue())))
                .collect(Collectors.toList());
        rulesEdited = FXCollections.observableArrayList(observableValues);
        rulesTable.setItems(rulesEdited);
    }
}
