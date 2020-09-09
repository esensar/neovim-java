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

package com.ensarsarajcic.neovim.java.explorer.test;

import com.ensarsarajcic.neovim.java.corerpc.message.RequestMessage;
import com.ensarsarajcic.neovim.java.corerpc.message.ResponseMessage;
import com.ensarsarajcic.neovim.java.corerpc.message.RpcError;
import com.ensarsarajcic.neovim.java.corerpc.reactive.RpcException;
import com.ensarsarajcic.neovim.java.explorer.api.ConnectionHolder;
import com.ensarsarajcic.neovim.java.explorer.api.NeovimFunction;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CompletionException;

public final class TestFunctionController {

    @FXML
    public VBox vbox;
    private NeovimFunction neovimFunction;

    public void initialize() {

    }

    public void setFunctionData(NeovimFunction neovimFunction) {
        this.neovimFunction = neovimFunction;
        var nodes = new ArrayList<Node>();
        var inputNodes = new ArrayList<Map.Entry<String, Node>>();

        nodes.add(new Label(neovimFunction.getName()));
        nodes.add(new Label("Returns: " + neovimFunction.getReturnType()));
        if (neovimFunction.getDeprecatedSince() > 0) {
            var deprecatedLabel = new Label("Deprecated!");
            deprecatedLabel.setTextFill(Color.RED);

            nodes.add(deprecatedLabel);
        }

        for (var strings : neovimFunction.getParameters()) {
            String type = strings.get(0);
            String name = strings.get(1);
            var label = new Label(name + ": ");
            var node = NodeHandler.generateNodeForType(type);
            label.setLabelFor(node);
            nodes.add(new HBox(label, node));
            inputNodes.add(new AbstractMap.SimpleEntry<>(type, node));
        }

        var resultLabel = new Label("Result: ");
        var resultArea = new TextArea();
        resultArea.setEditable(false);
        var send = new Button();
        send.setText("SEND");
        send.setOnAction(event -> {
            var args = new ArrayList<>();
            for (var node : inputNodes) {
                args.add(NodeHandler.generateValueFromNodeOfType(node.getValue(), node.getKey()));
            }
            ConnectionHolder.getReactiveRpcStreamer().response(
                    new RequestMessage.Builder(neovimFunction.getName())
                            .addArguments(args)
            ).exceptionally(throwable -> {
                if (throwable instanceof CompletionException) {
                    throwable = throwable.getCause();
                }
                if (throwable instanceof RpcException) {
                    return new ResponseMessage(0, ((RpcException) throwable).getRpcError(), null);
                } else {
                    return new ResponseMessage(
                            0,
                            new RpcError(-1000, "LOCAL ERROR: " + throwable.toString()),
                            null);
                }
            }).thenAccept(responseMessage -> resultArea.setText(responseMessage.toString()));
        });

        nodes.add(resultLabel);
        nodes.add(send);
        nodes.add(resultArea);

        vbox.getChildren().setAll(nodes);
    }
}
