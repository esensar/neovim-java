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

package com.ensarsarajcic.neovim.java.notifications.ui.tabline;

import com.ensarsarajcic.neovim.java.api.types.msgpack.Tabpage;

import java.util.List;

public final class TablineUpdateEvent implements UITablineEvent {
    public static final String NAME = "tabline_update";

    public static final class TabInfo {
        private Tabpage tab;
        private String name;

        public TabInfo(Tabpage tab, String name) {
            this.tab = tab;
            this.name = name;
        }

        public Tabpage getTab() {
            return tab;
        }

        public String getName() {
            return name;
        }
    }

    private Tabpage currentTabpage;
    private List<TabInfo> tabs;

    public TablineUpdateEvent(Tabpage currentTabpage, List<TabInfo> tabs) {
        this.currentTabpage = currentTabpage;
        this.tabs = tabs;
    }

    public Tabpage getCurrentTabpage() {
        return currentTabpage;
    }

    public List<TabInfo> getTabs() {
        return tabs;
    }

    @Override
    public String getEventName() {
        return NAME;
    }
}
