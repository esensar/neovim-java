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

package com.ensarsarajcic.neovim.java.api.notifications;

import com.ensarsarajcic.neovim.java.api.notifications.buffer.BufferEvent;
import com.ensarsarajcic.neovim.java.api.notifications.ui.NeovimRedrawEvent;

import java.util.concurrent.Flow;

public interface NeovimNotificationHandler {

    /**
     * Passes down a publisher of {@link NeovimRedrawEvent} objects received
     * {@link NeovimRedrawEvent} is a special type of notification that is received when
     * attached to Neovim
     * It will never complete
     * @return {@link Flow.Publisher} passing down ui events as they come
     */
    Flow.Publisher<NeovimRedrawEvent> uiEvents();

    /**
     * Passes down a publisher of {@link BufferEvent} objects received
     * {@link BufferEvent} is a special type of notification that is received when
     * attached to a buffer in Neovim
     * It will never complete
     * @return {@link Flow.Publisher} passing down buffer events as they come
     */
    Flow.Publisher<BufferEvent> bufferEvents();
}
