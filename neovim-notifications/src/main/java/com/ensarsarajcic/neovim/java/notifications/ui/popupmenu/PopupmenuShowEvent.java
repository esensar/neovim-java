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

import java.util.List;

public final class PopupmenuShowEvent implements UIPopupmenuEvent {
    public static final String NAME = "popupmenu_show";

    public static final class Item {
        private String word;
        private String kind;
        private String menu;
        private String info;

        public Item(String word, String kind, String menu, String info) {
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

    public PopupmenuShowEvent(List<Item> items, int selected, int row, int col) {
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
}
