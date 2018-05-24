package com.ensarsarajcic.neovim.java.explorer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public final class ApiExplorer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("api-list.fxml"));
            Parent root = null;
            root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setTitle("Api Explorer v" + getClass().getPackage().getImplementationVersion());
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
