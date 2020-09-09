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

package com.ensarsarajcic.neovim.java.api.types.msgpack;

import java.util.function.Function;

public enum NeovimCustomType {
    BUFFER(0, Buffer.class, Buffer::new),
    WINDOW(1, Window.class, Window::new),
    TABPAGE(2, Tabpage.class, Tabpage::new);

    private final NeovimTypeSerializer<? extends BaseCustomIdType> serializer;
    private final NeovimTypeDeserializer<? extends BaseCustomIdType> deserializer;
    private final Class<? extends BaseCustomIdType> type;
    private final int typeId;

    <T extends BaseCustomIdType> NeovimCustomType(
            int typeId,
            Class<T> type,
            Function<Long, T> constructor) {
        this.serializer = new NeovimTypeSerializer<>((byte) typeId, type);
        this.deserializer = new NeovimTypeDeserializer<>((byte) typeId, type, constructor);
        this.type = type;
        this.typeId = typeId;
    }

    public NeovimTypeSerializer getSerializer() {
        return serializer;
    }

    public NeovimTypeDeserializer getDeserializer() {
        return deserializer;
    }

    public Class<?> getType() {
        return type;
    }

    public int getTypeId() {
        return typeId;
    }
}
