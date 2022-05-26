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

package com.ensarsarajcic.neovim.java.explorer;

import com.ensarsarajcic.neovim.java.corerpc.client.ProcessRpcConnection;
import com.ensarsarajcic.neovim.java.corerpc.client.RpcConnection;
import com.ensarsarajcic.neovim.java.corerpc.client.TcpSocketRpcConnection;
import com.ensarsarajcic.neovim.java.explorer.api.ConnectionHolder;
import com.ensarsarajcic.neovim.java.unix.socket.UnixDomainSocketRpcConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

public final class ConnectionPickerController {
    @FXML
    public Button executableButton;
    @FXML
    public TextField executableField;
    @FXML
    public Button exploreBtn;
    @FXML
    public Button embedBtn;
    @FXML
    public Button connectBtn;
    @FXML
    public TextField ipField;
    @FXML
    public Button connectFileBtn;
    @FXML
    public TextField fileField;

    public void initialize() {
        executableField.setText(ConnectionHolder.getExecutable());
        executableField.textProperty().addListener((observable, oldValue, newValue) -> {
            ConnectionHolder.setExecutable(newValue);
        });
        executableButton.setOnAction(event -> {
            var fileChooser = new FileChooser();
            fileChooser.setTitle("Find neovim executable");
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            var file = fileChooser.showOpenDialog(executableButton.getScene().getWindow());
            ConnectionHolder.setExecutable(file.getAbsolutePath());
            executableField.setText(ConnectionHolder.getExecutable());
        });
        exploreBtn.setOnAction(event -> showApiList(exploreBtn));
        embedBtn.setOnAction(event -> {
            try {
                var pb = new ProcessBuilder(ConnectionHolder.getExecutable(), "--embed");
                var neovim = pb.start();
                ConnectionHolder.setConnection(new ProcessRpcConnection(neovim));
                ConnectionHolder.setConnectedIpPort("embedded instance");
                showApiList(embedBtn);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        connectBtn.setOnAction(event -> {
            try {
                var text = ipField.getText();
                var ip = text.split(":")[0];
                int port = Integer.valueOf(text.split(":")[1]);
                ConnectionHolder.setConnection(new TcpSocketRpcConnection(new Socket(ip, port)));
                ConnectionHolder.setConnectedIpPort(text);
                showApiList(connectBtn);
            } catch (Exception ex) {
                ipField.setText(ex.toString());
            }
        });
        connectFileBtn.setOnAction(event -> {
            try {
                String file = fileField.getText();
                File path = new File(file);
                RpcConnection rpcConnection = new UnixDomainSocketRpcConnection(path);
                ConnectionHolder.setConnection(rpcConnection);
                ConnectionHolder.setConnectedIpPort("File: " + path);
                showApiList(connectFileBtn);
            } catch (Exception ex) {
                fileField.setText(ex.toString());
            }
        });
    }

    private void showApiList(Button button) {
        try {
            var loader = new FXMLLoader(getClass().getClassLoader().getResource("api-list.fxml"));
            Parent root = null;
            root = loader.load();
            var scene = new Scene(root);
            ((Stage) button.getScene().getWindow()).setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
