package com.ensarsarajcic.neovim.java.explorer.list;

import com.ensarsarajcic.neovim.java.explorer.api.*;
import com.ensarsarajcic.neovim.java.explorer.api.discovery.ApiDiscovery;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.TextFlow;
import javafx.util.Callback;

import java.io.IOException;
import java.util.Map;

public final class ApiListController {
    // Functions
    @FXML
    public TableColumn<NeovimFunction, Boolean> functionColMethod;
    @FXML
    public TableColumn<NeovimFunction, String> functionColName;
    @FXML
    public TableColumn<NeovimFunction, String> functionColReturnType;
    @FXML
    public TableColumn<NeovimFunction, Integer> functionColSince;
    @FXML
    public TableColumn<NeovimFunction, Integer> functionColDepSince;
    @FXML
    public TableColumn<NeovimFunction, String> functionColParams;
    @FXML
    public TableView<NeovimFunction> functionTable;

    // Ui events
    @FXML
    public TableColumn<NeovimUiEvent, String> uiEventsColName;
    @FXML
    public TableColumn<NeovimUiEvent, Boolean> uiEventsColSince;
    @FXML
    public TableColumn<NeovimUiEvent, String> uiEventsColParams;
    @FXML
    public TableView<NeovimUiEvent> uiEventsTable;

    // Errors
    @FXML
    public TableView<Map.Entry<String, NeovimError>> errorsTable;
    @FXML
    public TableColumn<Map.Entry<String, NeovimError>, String> errorsColName;
    @FXML
    public TableColumn<Map.Entry<String, NeovimError>, String> errorsColId;

    // Types
    @FXML
    public TableColumn<Map.Entry<String, NeovimType>, String> typesColName;
    @FXML
    public TableColumn<Map.Entry<String, NeovimType>, String> typesColId;
    @FXML
    public TableColumn<Map.Entry<String, NeovimType>, String> typesColPrefix;
    @FXML
    public TableView<Map.Entry<String, NeovimType>> typesTable;

    // Version info
    @FXML
    public Label labelInformation;

    public void initialize() {
        try {
            // Load up API
            NeovimApiList apiList = ApiDiscovery.discoverApi();

            // Show Functions
            ObservableList<NeovimFunction> neovimFunctions = FXCollections.observableArrayList(apiList.getFunctions());
            functionColMethod.setCellValueFactory(new PropertyValueFactory<>("method"));
            functionColName.setCellValueFactory(new PropertyValueFactory<>("name"));
            functionColReturnType.setCellValueFactory(new PropertyValueFactory<>("returnType"));
            functionColSince.setCellValueFactory(new PropertyValueFactory<>("since"));
            functionColDepSince.setCellValueFactory(new PropertyValueFactory<>("deprecatedSince"));
            functionColParams.setCellValueFactory(new PropertyValueFactory<>("parameters"));
            functionTable.setItems(neovimFunctions);

            // Show Ui Events
            ObservableList<NeovimUiEvent> neovimUiEvents = FXCollections.observableArrayList(apiList.getUiEvents());
            uiEventsColName.setCellValueFactory(new PropertyValueFactory<>("name"));
            uiEventsColSince.setCellValueFactory(new PropertyValueFactory<>("since"));
            uiEventsColParams.setCellValueFactory(new PropertyValueFactory<>("parameters"));
            uiEventsTable.setItems(neovimUiEvents);

            // Show Errors
            ObservableMap<String, NeovimError> neovimErrors = FXCollections.observableMap(apiList.getErrorTypes());
            errorsColName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getKey()));
            errorsColId.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().getValue().getId())));
            errorsTable.setItems(FXCollections.observableArrayList(neovimErrors.entrySet()));

            // Show types
            ObservableMap<String, NeovimType> neovimTypes = FXCollections.observableMap(apiList.getTypes());
            typesColName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getKey()));
            typesColId.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().getValue().getId())));
            typesColPrefix.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getPrefix()));
            typesTable.setItems(FXCollections.observableArrayList(neovimTypes.entrySet()));

            // Show information
            ObservableValue<NeovimVersion> neovimVersions = new ObservableValueBase<>() {
                @Override
                public NeovimVersion getValue() {
                    return apiList.getVersion();
                }
            };
            labelInformation.setText(getVersionInfo(neovimVersions.getValue()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getVersionInfo(NeovimVersion neovimVersion) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(
                String.format(
                        "Neovim version: %s. Api level: %s",
                        getNeovimVersion(neovimVersion),
                        neovimVersion.getApiLevel()
                )
        );

        if (neovimVersion.isApiPreRelease()) {
            stringBuilder.append(" (pre-release)");
        }
        stringBuilder.append(". ");

        stringBuilder.append("Compatible version: ");
        stringBuilder.append(neovimVersion.getApiCompatible());

        return stringBuilder.toString();
    }

    private String getNeovimVersion(NeovimVersion neovimVersion) {
        return String.format("%d.%d.%d", neovimVersion.getMajor(), neovimVersion.getMinor(), neovimVersion.getPatch());
    }
}
