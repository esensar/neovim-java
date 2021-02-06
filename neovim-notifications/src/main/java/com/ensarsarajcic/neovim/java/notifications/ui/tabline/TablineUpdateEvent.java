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
import com.ensarsarajcic.neovim.java.api.util.ObjectMappers;
import com.ensarsarajcic.neovim.java.notifications.ui.UiEvent;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class TablineUpdateEvent implements UiTablineEvent {
    public static final String NAME = "tabline_update";

    public static final Function<List, UiEvent> CREATOR = list -> {
        try {
            ObjectMapper objectMapper = ObjectMappers.defaultNeovimMapper();

            List<Map> tabsList = (List<Map>) list.get(1);

            List<TabInfo> tabInfos = new ArrayList<>();

            for (Map object : tabsList) {
                tabInfos.add(
                        new TabInfo(
                                objectMapper.readerFor(Tabpage.class).readValue(objectMapper.writeValueAsBytes(object.get("tab"))),
                                (String) object.get("name")
                        )
                );
            }

            return new TablineUpdateEvent(
                    objectMapper.readerFor(Tabpage.class).readValue(objectMapper.writeValueAsBytes(list.get(0))),
                    tabInfos
            );
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    };

    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    public static final class TabInfo {
        private final Tabpage tab;
        private final String name;

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

        @Override
        public String toString() {
            return "TabInfo{"
                    + "tab=" + tab
                    + ", name='" + name + '\'' + '}';
        }
    }

    private final Tabpage currentTabpage;
    private final List<TabInfo> tabs;

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

    @Override
    public String toString() {
        return "TablineUpdateEvent{"
                + "currentTabpage=" + currentTabpage
                + ", tabs=" + tabs + '}';
    }
}
