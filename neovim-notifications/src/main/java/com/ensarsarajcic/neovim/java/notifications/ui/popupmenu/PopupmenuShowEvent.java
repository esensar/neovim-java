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

package com.ensarsarajcic.neovim.java.notifications.ui.popupmenu;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
public final class PopupmenuShowEvent implements UiPopupmenuEvent {
    public static final String NAME = "popupmenu_show";

    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    public static final class Item {
        private String word;
        private String kind;
        private String menu;
        private String info;

        public Item(
                @JsonProperty(value = "word", index = 0) String word,
                @JsonProperty(value = "kind", index = 1) String kind,
                @JsonProperty(value = "menu", index = 2) String menu,
                @JsonProperty(value = "info", index = 3) String info) {
            this.word = word;
            this.kind = kind;
            this.menu = menu;
            this.info = info;
        }

        public String getWord() {
            return word;
        }

        public String getKind() {
            return kind;
        }

        public String getMenu() {
            return menu;
        }

        public String getInfo() {
            return info;
        }
    }

    private List<Item> items;
    private int selected;
    private int row;
    private int col;

    public PopupmenuShowEvent(
            @JsonProperty(value = "items", index = 0) List<Item> items,
            @JsonProperty(value = "selected", index = 1) int selected,
            @JsonProperty(value = "row", index = 2) int row,
            @JsonProperty(value = "col", index = 3) int col) {
        this.items = items;
        this.selected = selected;
        this.row = row;
        this.col = col;
    }

    public List<Item> getItems() {
        return items;
    }

    public int getSelected() {
        return selected;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public String getEventName() {
        return NAME;
    }

    @Override
    public String toString() {
        return "PopupmenuShowEvent{" +
                "items=" + items +
                ", selected=" + selected +
                ", row=" + row +
                ", col=" + col +
                '}';
    }
}
