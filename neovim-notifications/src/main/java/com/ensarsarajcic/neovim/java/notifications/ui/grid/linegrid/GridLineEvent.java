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

import java.util.List;

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
public final class GridLineEvent implements UiLineGridEvent {
    public static final String NAME = "grid_line";

    private final int grid;
    private final int row;
    private final int colStart;
    private final List<CellData> data;

    public GridLineEvent(
            @JsonProperty(value = "grid", index = 0) int grid,
            @JsonProperty(value = "row", index = 1) int row,
            @JsonProperty(value = "col_start", index = 2) int colStart,
            @JsonProperty(value = "data", index = 3) List<CellData> data) {
        this.grid = grid;
        this.row = row;
        this.colStart = colStart;
        this.data = data;
    }

    public int getGrid() {
        return grid;
    }

    public int getRow() {
        return row;
    }

    public int getColStart() {
        return colStart;
    }

    public List<CellData> getData() {
        return data;
    }

    @Override
    public String getEventName() {
        return NAME;
    }

    @Override
    public String toString() {
        return "GridLineEvent{"
                + "grid=" + grid
                + ", row=" + row
                + ", colStart=" + colStart
                + ", data=" + data + '}';
    }
}
