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

import com.ensarsarajcic.neovim.java.api.util.ObjectMappers;
import com.ensarsarajcic.neovim.java.notifications.ui.UIEvent;

import java.util.List;
import java.util.function.Function;

public final class ModeInfoSetEvent implements UIGlobalEvent {
    public static final String NAME = "mode_info_set";

    public static final Function<List, UIEvent> CREATOR = list -> new ModeInfoSetEvent(
            (Boolean) list.get(1),
            ObjectMappers.defaultNeovimMapper().convertValue(
                    list.get(2),
                    ObjectMappers.defaultNeovimMapper()
                            .getTypeFactory()
                            .constructCollectionType(List.class, ModeInfo.class)
            )
    );

    private boolean cursorStyleEnabled;
    private List<ModeInfo> modeInfoList;

    public ModeInfoSetEvent(boolean cursorStyleEnabled, List<ModeInfo> modeInfoList) {
        this.cursorStyleEnabled = cursorStyleEnabled;
        this.modeInfoList = modeInfoList;
    }

    public boolean isCursorStyleEnabled() {
        return cursorStyleEnabled;
    }

    public List<ModeInfo> getModeInfoList() {
        return modeInfoList;
    }

    @Override
    public String getEventName() {
        return NAME;
    }
}
