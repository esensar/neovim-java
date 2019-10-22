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

import com.fasterxml.jackson.annotation.JsonProperty;

public final class CellData {

    private String text;
    private int highlightId;
    private int repeat;

    public CellData(
            @JsonProperty(value = "text", index = 0, required = true) String text,
            @JsonProperty(value = "hl_id", index = 1, required = false) int highlightId,
            @JsonProperty(value = "repeat", index = 2, required = false) int repeat) {
        this.text = text;
        this.highlightId = highlightId;
        this.repeat = repeat;
    }

    public String getText() {
        return text;
    }

    public int getHighlightId() {
        return highlightId;
    }

    public int getRepeat() {
        return repeat;
    }

    @Override
    public String toString() {
        return "CellData{" +
                "text='" + text + '\'' +
                ", highlightId=" + highlightId +
                ", repeat=" + repeat +
                '}';
    }
}
