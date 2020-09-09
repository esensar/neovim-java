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

package com.ensarsarajcic.neovim.java.notifications.ui.multigrid;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
public final class MessageSetEvent implements UiMultigridEvent {
    public static final String NAME = "msg_set_pos";

    private final int grid;
    private final int row;
    private final boolean scrolled;
    private final String sepChar;

    public MessageSetEvent(
            @JsonProperty(value = "grid", index = 0) int grid,
            @JsonProperty(value = "row", index = 1) int row,
            @JsonProperty(value = "scrolled", index = 2) boolean scrolled,
            @JsonProperty(value = "sep_char", index = 3) String sepChar) {
        this.grid = grid;
        this.row = row;
        this.scrolled = scrolled;
        this.sepChar = sepChar;
    }

    public int getGrid() {
        return grid;
    }

    public int getRow() {
        return row;
    }

    public boolean isScrolled() {
        return scrolled;
    }

    public String getSepChar() {
        return sepChar;
    }

    @Override
    public String getEventName() {
        return NAME;
    }

    @Override
    public String toString() {
        return "MessageSetEvent{"
                + "grid=" + grid
                + ", row=" + row
                + ", scrolled=" + scrolled
                + ", sepChar='" + sepChar + '\'' + '}';
    }
}
