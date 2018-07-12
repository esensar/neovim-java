/*
 * MIT License
 *
 * Copyright (c) 2018 Ensar Sarajčić
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ensarsarajcic.neovim.java.explorer.list;

import com.ensarsarajcic.neovim.java.explorer.ApiExplorer;
import com.ensarsarajcic.neovim.java.explorer.api.*;
import com.ensarsarajcic.neovim.java.explorer.api.discovery.ApiDiscovery;
import com.ensarsarajcic.neovim.java.explorer.test.TestFunctionController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValueBase;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ExecutionException;

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
    public TableColumn functionColOpenInBrowser;
    @FXML
    public TableColumn functionColTestFunc;
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

    // UI Options
    @FXML
    public TableView<String> uiOptionsTable;
    @FXML
    public TableColumn<String, String> uiOptionsColName;

    // Version info
    @FXML
    public Label labelInformation;

    // Search
    @FXML
    public TextField fieldSearch;

    private boolean hasConnection = false;

    public void initialize() throws ExecutionException, InterruptedException {
        try {
            // Load up API
            NeovimApiList apiList;
            if (ConnectionHolder.getConnection() == null) {
                apiList = ApiDiscovery.discoverApi();
            } else {
                apiList = ApiDiscovery.discoverApiFromInstance(ConnectionHolder.getApi());
                hasConnection = true;
            }
            var finalApiList = apiList;

            // Show Functions
            var neovimFunctions = FXCollections.observableArrayList(apiList.getFunctions());
            functionColMethod.setCellValueFactory(new PropertyValueFactory<>("method"));
            functionColName.setCellValueFactory(new PropertyValueFactory<>("name"));
            functionColReturnType.setCellValueFactory(new PropertyValueFactory<>("returnType"));
            functionColSince.setCellValueFactory(new PropertyValueFactory<>("since"));
            functionColDepSince.setCellValueFactory(new PropertyValueFactory<>("deprecatedSince"));
            functionColParams.setCellValueFactory(new PropertyValueFactory<>("parameters"));
            functionColOpenInBrowser.setCellFactory(new Callback<TableColumn, TableCell>() {
                @Override
                public TableCell call(TableColumn param) {
                    return new TableCell<NeovimFunction, String>() {
                        final Button btn = new Button("Read doc");

                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                                setText(null);
                            } else {
                                btn.setOnAction(event -> {
                                    var function = getTableView().getItems().get(getIndex());
                                    ApiExplorer.hostServices.showDocument(
                                            String.format("https://neovim.io/doc/user/api.html#%s()", function.getName())
                                    );
                                });
                                setGraphic(btn);
                                setText(null);
                            }
                        }
                    };
                }
            });
            functionColTestFunc.setCellFactory(new Callback<TableColumn, TableCell>() {
                @Override
                public TableCell call(TableColumn param) {
                    return new TableCell<NeovimFunction, String>() {
                        final Button btn = new Button("Test");

                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                                setText(null);
                            } else {
                                btn.setOnAction(event -> {
                                    var function = getTableView().getItems().get(getIndex());
                                    try {
                                        var loader = new FXMLLoader(getClass().getClassLoader().getResource("test-function.fxml"));
                                        Parent root = null;
                                        root = loader.load();
                                        var stage = new Stage();
                                        var scene = new Scene(root);
                                        scene.getStylesheets().add("styles.css");
                                        stage.setTitle("Testing function " + function.getName());
                                        stage.setScene(scene);
                                        loader.<TestFunctionController>getController().setFunctionData(function);
                                        stage.show();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                });
                                if (hasConnection) {
                                    btn.setDisable(false);
                                } else {
                                    btn.setDisable(true);
                                }
                                setGraphic(btn);
                                setText(null);
                            }
                        }
                    };
                }
            });
            functionTable.setRowFactory(new Callback<>() {
                @Override
                public TableRow<NeovimFunction> call(TableView<NeovimFunction> param) {
                    return new TableRow<>() {
                        @Override
                        protected void updateItem(NeovimFunction item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                return;
                            }
                            if (item.getSince() == finalApiList.getVersion().getApiLevel()) {
                                if (!getStyleClass().contains("newFunc")) {
                                    getStyleClass().add("newFunc");
                                }
                            } else {
                                getStyleClass().removeAll(Collections.singleton("newFunc"));
                            }

                            if (item.getDeprecatedSince() != 0 && item.getDeprecatedSince() <= finalApiList.getVersion().getApiLevel()) {
                                if (!getStyleClass().contains("deprecatedFunc")) {
                                    getStyleClass().add("deprecatedFunc");
                                }
                            } else {
                                getStyleClass().removeAll(Collections.singleton("deprecatedFunc"));
                            }
                        }
                    };
                }
            });
            functionTable.setItems(neovimFunctions);

            // Show Ui Events
            var neovimUiEvents = FXCollections.observableArrayList(apiList.getUiEvents());
            uiEventsColName.setCellValueFactory(new PropertyValueFactory<>("name"));
            uiEventsColSince.setCellValueFactory(new PropertyValueFactory<>("since"));
            uiEventsColParams.setCellValueFactory(new PropertyValueFactory<>("parameters"));
            uiEventsTable.setRowFactory(new Callback<>() {
                @Override
                public TableRow<NeovimUiEvent> call(TableView<NeovimUiEvent> param) {
                    return new TableRow<>() {
                        @Override
                        protected void updateItem(NeovimUiEvent item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                return;
                            }
                            if (item.getSince() == finalApiList.getVersion().getApiLevel()) {
                                if (!getStyleClass().contains("newFunc")) {
                                    getStyleClass().add("newFunc");
                                }
                            } else {
                                getStyleClass().removeAll(Collections.singleton("newFunc"));
                            }
                        }
                    };
                }
            });
            uiEventsTable.setItems(neovimUiEvents);

            // Show Ui Options
            var neovimUiOptions = FXCollections.observableArrayList(apiList.getUiOptions());
            uiOptionsColName.setCellValueFactory(c -> new SimpleStringProperty(c.getValue()));
            uiOptionsTable.setItems(neovimUiOptions);

            // Show Errors
            var neovimErrors = FXCollections.observableMap(apiList.getErrorTypes());
            errorsColName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getKey()));
            errorsColId.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().getValue().getId())));
            errorsTable.setItems(FXCollections.observableArrayList(neovimErrors.entrySet()));

            // Show types
            var neovimTypes = FXCollections.observableMap(apiList.getTypes());
            typesColName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getKey()));
            typesColId.setCellValueFactory(param -> new SimpleStringProperty(String.valueOf(param.getValue().getValue().getId())));
            typesColPrefix.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().getPrefix()));
            typesTable.setItems(FXCollections.observableArrayList(neovimTypes.entrySet()));

            // Show information
            var neovimVersions = new ObservableValueBase<>() {
                @Override
                public NeovimVersion getValue() {
                    return finalApiList.getVersion();
                }
            };
            labelInformation.setText(getVersionInfo(neovimVersions.getValue()));

            var finalApiList1 = apiList;
            fieldSearch.setOnAction(event -> {
                var searchValue = fieldSearch.getText();
                neovimFunctions.removeAll(finalApiList1.getFunctions());
                neovimFunctions.addAll(finalApiList1.getFunctions());
                neovimUiEvents.removeAll(finalApiList1.getUiEvents());
                neovimUiEvents.addAll(finalApiList1.getUiEvents());
                neovimUiOptions.removeAll(finalApiList1.getUiOptions());
                neovimUiOptions.addAll(finalApiList1.getUiOptions());
                neovimFunctions.removeIf(neovimFunction -> !neovimFunction.getName().toUpperCase().contains(searchValue.toUpperCase()) && !searchValue.toUpperCase().contains(neovimFunction.getName().toUpperCase()));
                neovimUiEvents.removeIf(neovimUiEvent -> !neovimUiEvent.getName().toUpperCase().contains(searchValue.toUpperCase()) && !searchValue.toUpperCase().contains(neovimUiEvent.getName().toUpperCase()));
                neovimUiOptions.removeIf(neovimUiOption -> !neovimUiOption.toUpperCase().contains(searchValue.toUpperCase()) && !searchValue.toUpperCase().contains(neovimUiOption.toUpperCase()));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getVersionInfo(NeovimVersion neovimVersion) {
        var stringBuilder = new StringBuilder();

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

        stringBuilder.append(". ");

        if (hasConnection) {
            stringBuilder.append("Connected to: ");
            stringBuilder.append(ConnectionHolder.getConnectedIpPort());
        } else {
            stringBuilder.append("Not connected.");
        }

        return stringBuilder.toString();
    }

    private String getNeovimVersion(NeovimVersion neovimVersion) {
        return String.format("%d.%d.%d", neovimVersion.getMajor(), neovimVersion.getMinor(), neovimVersion.getPatch());
    }
}
