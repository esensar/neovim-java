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

package com.ensarsarajcic.neovim.java.notifications.ui;

import com.ensarsarajcic.neovim.java.notifications.NeovimNotification;

import java.util.Collections;
import java.util.List;

/**
 * Represents a special notification that consists of multiple {@link UiEvent}s
 */
public final class NeovimRedrawEvent implements NeovimNotification {
    public static final String NAME = "redraw";

    private List<UiEvent> uiEvents;

    public NeovimRedrawEvent(List<UiEvent> uiEvents) {
        this.uiEvents = Collections.unmodifiableList(uiEvents);
    }

    @Override
    public final String getNotificationName() {
        return NAME;
    }

    public List<UiEvent> getUiEvents() {
        return uiEvents;
    }

    @Override
    public String toString() {
        return "NeovimRedrawEvent{" +
                "uiEvents=" + uiEvents +
                '}';
    }
}
