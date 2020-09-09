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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.msgpack.core.MessagePack;
import org.msgpack.jackson.dataformat.MessagePackExtensionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.function.Function;

public final class NeovimTypeDeserializer<T extends BaseCustomIdType> extends JsonDeserializer<T> {
    private static final Logger log = LoggerFactory.getLogger(NeovimTypeDeserializer.class);

    private final byte typeId;
    private final Class<T> type;
    private final Function<Long, T> constructor;

    public NeovimTypeDeserializer(byte typeId, Class<T> type, Function<Long, T> constructor) {
        this.typeId = typeId;
        this.type = type;
        this.constructor = constructor;
    }

    @Override
    public Class<?> handledType() {
        return type;
    }

    @Override
    public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        var messagePackExtensionType = (MessagePackExtensionType) jsonParser.getEmbeddedObject();

        if (messagePackExtensionType.getType() != typeId) {
            log.error("Tried to parse a bad type ({})", messagePackExtensionType.getType());
            throw new JsonParseException(jsonParser, "Bad custom type");
        }

        return constructor.apply(
                MessagePack.newDefaultUnpacker(messagePackExtensionType.getData()).unpackLong()
        );
    }
}
