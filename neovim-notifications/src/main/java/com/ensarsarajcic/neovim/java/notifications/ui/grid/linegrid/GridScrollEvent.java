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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
public final class GridScrollEvent implements UiLineGridEvent {
    public static final String NAME = "grid_scroll";

    private int grid;
    private int top;
    private int bot;
    private int left;
    private int right;
    private int rows;
    private int cols;

    public GridScrollEvent(
            @JsonProperty(value = "grid", index = 0) int grid,
            @JsonProperty(value = "top", index = 1) int top,
            @JsonProperty(value = "bot", index = 2) int bot,
            @JsonProperty(value = "left", index = 3) int left,
            @JsonProperty(value = "right", index = 4) int right,
            @JsonProperty(value = "rows", index = 5) int rows,
            @JsonProperty(value = "cols", index = 6) int cols) {
        this.grid = grid;
        this.top = top;
        this.bot = bot;
        this.left = left;
        this.right = right;
        this.rows = rows;
        this.cols = cols;
    }

    public int getGrid() {
        return grid;
    }

    public int getTop() {
        return top;
    }

    public int getBot() {
        return bot;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    @Override
    public String getEventName() {
        return NAME;
    }

    @Override
    public String toString() {
        return "GridScrollEvent{" +
                "grid=" + grid +
                ", top=" + top +
                ", bot=" + bot +
                ", left=" + left +
                ", right=" + right +
                ", rows=" + rows +
                ", cols=" + cols +
                '}';
    }
}
