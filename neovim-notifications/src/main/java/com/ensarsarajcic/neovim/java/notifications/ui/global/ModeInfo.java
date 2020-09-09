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

package com.ensarsarajcic.neovim.java.notifications.ui.global;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public final class ModeInfo {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public enum CursorShape {
        BLOCK("block"),
        HORIZONTAL("horizontal"),
        VERTICAL("vertical");

        private final String value;

        @JsonCreator
        public static CursorShape fromString(String value) {
            for (CursorShape cursorShape : values()) {
                if (cursorShape.value.equals(value)) {
                    return cursorShape;
                }
            }
            throw new IllegalArgumentException(String.format("CursorShape (%s) does not exist", value));
        }

        CursorShape(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "CursorShape{" + "value='" + value + '\'' + '}';
        }
    }

    private final CursorShape cursorShape;
    private final int cellPercentage;
    private final int blinkWait;
    private final int blinkOn;
    private final int blinkOff;
    private final int highlightId;
    private final int highlightLangmapId;
    private final String shortName;
    private final String fullName;
    private final Object mouseShape;

    public ModeInfo(
            @JsonProperty(value = "cursor_shape", index = 0) CursorShape cursorShape,
            @JsonProperty(value = "cell_percentage", index = 1) int cellPercentage,
            @JsonProperty(value = "blinkwait", index = 2) int blinkWait,
            @JsonProperty(value = "blinkon", index = 3) int blinkOn,
            @JsonProperty(value = "blinkoff", index = 4) int blinkOff,
            @JsonProperty(value = "hl_id", index = 5) int highlightId,
            @JsonProperty(value = "id_lm", index = 6) int highlightLangmapId,
            @JsonProperty(value = "short_name", index = 7) String shortName,
            @JsonProperty(value = "name", index = 8) String fullName,
            @JsonProperty(value = "mouse_shape", index = 9) Object mouseShape) {
        this.cursorShape = cursorShape;
        this.cellPercentage = cellPercentage;
        this.blinkWait = blinkWait;
        this.blinkOn = blinkOn;
        this.blinkOff = blinkOff;
        this.highlightId = highlightId;
        this.highlightLangmapId = highlightLangmapId;
        this.shortName = shortName;
        this.fullName = fullName;
        this.mouseShape = mouseShape;
    }

    public CursorShape getCursorShape() {
        return cursorShape;
    }

    public int getCellPercentage() {
        return cellPercentage;
    }

    public int getBlinkWait() {
        return blinkWait;
    }

    public int getBlinkOn() {
        return blinkOn;
    }

    public int getBlinkOff() {
        return blinkOff;
    }

    public int getHighlightId() {
        return highlightId;
    }

    public int getHighlightLangmapId() {
        return highlightLangmapId;
    }

    public String getShortName() {
        return shortName;
    }

    public String getFullName() {
        return fullName;
    }

    public Object getMouseShape() {
        return mouseShape;
    }

    @Override
    public String toString() {
        return "ModeInfo{"
                + "cursorShape=" + cursorShape
                + ", cellPercentage=" + cellPercentage
                + ", blinkWait=" + blinkWait
                + ", blinkOn=" + blinkOn
                + ", blinkOff=" + blinkOff
                + ", highlightId=" + highlightId
                + ", highlightLangmapId=" + highlightLangmapId
                + ", shortName='" + shortName + '\''
                + ", fullName='" + fullName + '\''
                + ", mouseShape=" + mouseShape + '}';
    }
}
