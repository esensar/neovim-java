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

package com.ensarsarajcic.neovim.java.api.notifications.buffer;

import com.ensarsarajcic.neovim.java.api.types.msgpack.Buffer;

import java.util.List;

public final class BufferLinesEvent implements BufferEvent {
    public static final String NAME = "nvim_buf_lines_event";

    private Buffer buffer;
    private long changedTick;
    private int firstLine;
    private int lastLine;
    private List<String> lineData;
    private boolean more;

    public BufferLinesEvent(Buffer buffer, long changedTick, int firstLine, int lastLine, List<String> lineData, boolean more) {
        this.buffer = buffer;
        this.changedTick = changedTick;
        this.firstLine = firstLine;
        this.lastLine = lastLine;
        this.lineData = lineData;
        this.more = more;
    }

    public Buffer getBuffer() {
        return buffer;
    }

    public long getChangedTick() {
        return changedTick;
    }

    public int getFirstLine() {
        return firstLine;
    }

    public int getLastLine() {
        return lastLine;
    }

    public List<String> getLineData() {
        return lineData;
    }

    public boolean isMore() {
        return more;
    }

    @Override
    public String getNotificationName() {
        return NAME;
    }
}
