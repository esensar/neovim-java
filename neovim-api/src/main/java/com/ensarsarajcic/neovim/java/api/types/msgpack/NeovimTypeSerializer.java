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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.msgpack.core.MessagePack;
import org.msgpack.jackson.dataformat.MessagePackExtensionType;
import org.msgpack.jackson.dataformat.MessagePackGenerator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public final class NeovimTypeSerializer<T extends BaseCustomIdType> extends JsonSerializer<T> {

    private byte typeId;
    private Class<T> type;

    public NeovimTypeSerializer(byte typeId, Class<T> type) {
        this.typeId = typeId;
        this.type = type;
    }

    @Override
    public Class<T> handledType() {
        return type;
    }

    @Override
    public void serialize(T t, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        MessagePackGenerator messagePackGenerator = (MessagePackGenerator) jsonGenerator;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        MessagePack.newDefaultPacker(byteArrayOutputStream).packLong(t.getId()).close();
        MessagePackExtensionType messagePackExtensionType =
                new MessagePackExtensionType(typeId, byteArrayOutputStream.toByteArray());
        messagePackGenerator.writeExtensionType(messagePackExtensionType);
    }
}
