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

package com.ensarsarajcic.neovim.java.notifications.ui.grid.linegrid;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public final class HighlightInfo {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public enum Kind {
        UI("ui"),
        SYNTAX("syntax"),
        TERMINAL("terminal");

        private final String value;

        @JsonCreator
        public static Kind fromString(String value) {
            for (Kind kind : values()) {
                if (kind.value.equals(value)) {
                    return kind;
                }
            }
            throw new IllegalArgumentException(String.format("Kind (%s) does not exist", value));
        }

        Kind(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "Kind{" + "value='" + value + '\'' + '}';
        }
    }

    private final Kind kind;
    private final String uiName;
    private final String highlightName;
    private final int id;

    public HighlightInfo(
            @JsonProperty(value = "kind", index = 0, required = true) Kind kind,
            @JsonProperty(value = "ui_name", index = 1, required = false) String uiName,
            @JsonProperty(value = "hi_name", index = 2, required = true) String highlightName,
            @JsonProperty(value = "id", index = 3, required = true) int id) {
        this.kind = kind;
        this.uiName = uiName;
        this.highlightName = highlightName;
        this.id = id;
    }

    public Kind getKind() {
        return kind;
    }

    public String getUiName() {
        return uiName;
    }

    public String getHighlightName() {
        return highlightName;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "HighlightInfo{"
                + "kind=" + kind
                + ", uiName='" + uiName + '\''
                + ", highlightName='" + highlightName + '\''
                + ", id=" + id + '}';
    }
}
