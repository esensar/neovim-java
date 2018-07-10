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

import com.ensarsarajcic.neovim.java.api.types.msgpack.Buffer;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Tabpage;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Window;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.io.IOException;

public final class NodeHandler {

    private NodeHandler() {
        throw new AssertionError("No instances");
    }

    public static Node generateNodeForType(String type) {
        switch (type) {
            case "Boolean":
                return generateBooleanNode();
            case "Integer":
                return generateIntegerNode();
            case "String":
                return new TextField();
            case "Object":
                return new TextField("Object here (could be JSON)");
            case "Dictionary":
                return new TextField("Dictionary here (JSON)");
            case "Array":
                return new TextField("Array here");
            case "Window":
                return new NeovimTypeField<>(Window::new);
            case "Tabpage":
                return new NeovimTypeField<>(Tabpage::new);
            case "Buffer":
                return new NeovimTypeField<>(Buffer::new);
        }

        if (type.startsWith("Array")) {
            return new TextField("Array here");
        }

        return new TextField(type);
    }

    private static Node generateBooleanNode() {
        ToggleGroup toggleGroup = new ToggleGroup();
        RadioButton trueButton = new RadioButton("True");
        trueButton.setSelected(true);
        RadioButton falseButton = new RadioButton("False");
        trueButton.setToggleGroup(toggleGroup);
        falseButton.setToggleGroup(toggleGroup);
        return new HBox(trueButton, falseButton);
    }

    private static Boolean getBooleanNodeValue(HBox hBox) {
        RadioButton trueButton = (RadioButton) hBox.getChildren().get(0);
        return trueButton.isSelected();
    }

    private static Node generateIntegerNode() {
        return new NumberField();
    }

    public static Object generateValueFromNodeOfType(Node node, String type) {
        switch (type) {
            case "Boolean":
                return getBooleanNodeValue((HBox) node);
            case "Integer":
                return ((NumberField) node).getIntValue();
            case "String":
                return ((TextField) node).getText();
            case "Object":
            case "Dictionary":
            case "Array":
                try {
                    return new ObjectMapper().reader().readValue(((TextField) node).getText());
                } catch (IOException e) {
                    e.printStackTrace();
                    return e;
                }
            case "Buffer":
            case "Window":
            case "Tabpage":
                return ((NeovimTypeField) node).getValue();
        }

        if (type.startsWith("Array")) {
            try {
                return new ObjectMapper().reader().readValue(((TextField) node).getText());
            } catch (IOException e) {
                e.printStackTrace();
                return e;
            }
        }

        return new Object();
    }
}
