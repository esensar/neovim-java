/*
 * MIT License
 *
 * Copyright (c) 2022 Ensar Sarajčić
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

import com.ensarsarajcic.neovim.java.api.types.msgpack.Window;
import com.ensarsarajcic.neovim.java.api.util.ObjectMappers;
import com.ensarsarajcic.neovim.java.notifications.ui.UiEvent;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
public final class WinViewportEvent implements UiMultigridEvent {
    public static final String NAME = "win_viewport";

    public static final Function<List, UiEvent> CREATOR = list -> {
        try {
            ObjectMapper objectMapper = ObjectMappers.defaultNeovimMapper();
            return new WinViewportEvent(
                    (Integer) list.get(0),
                    objectMapper.readerFor(Window.class).readValue(objectMapper.writeValueAsBytes(list.get(1))),
                    (Integer) list.get(2),
                    (Integer) list.get(3),
                    (Integer) list.get(4),
                    (Integer) list.get(5)
            );
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    };

    private final int grid;
    private final Window win;
    private final int topLine;
    private final int botLine;
    private final int curLine;
    private final int curCol;

    public WinViewportEvent(
            @JsonProperty(value = "grid", index = 0) int grid,
            @JsonProperty(value = "win", index = 1) Window win,
            @JsonProperty(value = "topline", index = 2) int topLine,
            @JsonProperty(value = "botline", index = 2) int botLine,
            @JsonProperty(value = "curline", index = 2) int curLine,
            @JsonProperty(value = "curcol", index = 2) int curCol) {
        this.grid = grid;
        this.win = win;
        this.topLine = topLine;
        this.botLine = botLine;
        this.curLine = curLine;
        this.curCol = curCol;
    }

    public int getGrid() {
        return grid;
    }

    public Window getWin() {
        return win;
    }

    public int getTopLine() {
        return topLine;
    }

    public int getBotLine() {
        return botLine;
    }

    public int getCurLine() {
        return curLine;
    }

    public int getCurCol() {
        return curCol;
    }

    @Override
    public String getEventName() {
        return NAME;
    }

    @Override
    public String toString() {
        return "WinViewportEvent{" +
                "grid=" + grid +
                ", win=" + win +
                ", topLine=" + topLine +
                ", botLine=" + botLine +
                ", curLine=" + curLine +
                ", curCol=" + curCol +
                '}';
    }
}
