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

import com.ensarsarajcic.neovim.java.corerpc.message.RPCError;
import com.ensarsarajcic.neovim.java.corerpc.message.RequestMessage;
import com.ensarsarajcic.neovim.java.corerpc.message.ResponseMessage;
import com.ensarsarajcic.neovim.java.corerpc.reactive.RPCException;
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
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionException;
import java.util.function.Function;

public final class TestFunctionController {

    @FXML
    public VBox vbox;
    private NeovimFunction neovimFunction;

    public void initialize() {

    }

    public void setFunctionData(NeovimFunction neovimFunction) {
        this.neovimFunction = neovimFunction;
        List<Node> nodes = new ArrayList<>();
        List<Map.Entry<String, Node>> inputNodes = new ArrayList<>();

        nodes.add(new Label(neovimFunction.getName()));
        nodes.add(new Label("Returns: " + neovimFunction.getReturnType()));
        if (neovimFunction.getDeprecatedSince() > 0) {
            Label deprecatedLabel = new Label("Deprecated!");
            deprecatedLabel.setTextFill(Color.RED);

            nodes.add(deprecatedLabel);
        }

        for (List<String> strings : neovimFunction.getParameters()) {
            String type = strings.get(0);
            String name = strings.get(1);
            Label label = new Label(name + ": ");
            Node node = NodeHandler.generateNodeForType(type);
            label.setLabelFor(node);
            nodes.add(new HBox(label, node));
            inputNodes.add(new AbstractMap.SimpleEntry<>(type, node));
        }

        Label resultLabel = new Label("Result: ");
        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);
        Button send = new Button();
        send.setText("SEND");
        send.setOnAction(event -> {
            ArrayList<Object> args = new ArrayList<>();
            for (Map.Entry<String, Node> node : inputNodes) {
                args.add(NodeHandler.generateValueFromNodeOfType(node.getValue(), node.getKey()));
            }
            ConnectionHolder.getReactiveRPCStreamer().response(
                    new RequestMessage.Builder(neovimFunction.getName())
                            .addArguments(args)
            ).exceptionally(throwable -> {
                if (throwable instanceof  CompletionException) {
                    throwable = throwable.getCause();
                }
                if (throwable instanceof RPCException) {
                    return new ResponseMessage(0, ((RPCException) throwable).getRpcError(), null);
                } else {
                    return new ResponseMessage(
                            0,
                            new RPCError(-1000, "LOCAL ERROR: " + throwable.toString()),
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
