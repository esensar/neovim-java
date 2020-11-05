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

package com.ensarsarajcic.neovim.java.notifications.buffer;

import com.ensarsarajcic.neovim.java.api.types.msgpack.Buffer;
import com.ensarsarajcic.neovim.java.api.util.ObjectMappers;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

public final class BufferChangedTickEvent implements BufferEvent {
    public static final String NAME = "nvim_buf_changedtick_event";

    public static final Function<List, BufferEvent> CREATOR = list -> {
        try {
            ObjectMapper objectMapper = ObjectMappers.defaultNeovimMapper();
            return new BufferChangedTickEvent(
                    objectMapper.readerFor(Buffer.class).readValue(objectMapper.writeValueAsBytes(list.get(0))),
                    (Long) list.get(1)
            );
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    };

    private final Buffer buffer;
    private final long changedTick;

    public BufferChangedTickEvent(Buffer buffer, long changedTick) {
        this.buffer = buffer;
        this.changedTick = changedTick;
    }

    public Buffer getBuffer() {
        return buffer;
    }

    public long getChangedTick() {
        return changedTick;
    }

    @Override
    public String getNotificationName() {
        return NAME;
    }

    @Override
    public String toString() {
        return "BufferChangedTickEvent{"
                + "buffer=" + buffer
                + ", changedTick=" + changedTick + '}';
    }
}
