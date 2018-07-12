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
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.msgpack.jackson.dataformat.MessagePackFactory;

public final class NeovimJacksonModule {

    private NeovimJacksonModule() {
    }

    /**
     * Create a simple Jackson module for serializing and deserializing custom neovim msgpack types
     */
    public static SimpleModule createModule() {
        var simpleModule = new SimpleModule();
        for (var neovimCustomType : NeovimCustomType.values()) {
            simpleModule.addSerializer(neovimCustomType.getType(), neovimCustomType.getSerializer());
            simpleModule.addDeserializer(neovimCustomType.getType(), neovimCustomType.getDeserializer());
        }
        return simpleModule;
    }

    /**
     * Create an instance of ObjectMapper with proper support for mgspack and neovim msgpack types
     */
    public static ObjectMapper createNeovimObjectMapper() {
        var factory = new MessagePackFactory();
        factory.disable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
        factory.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        var objectMapper = new ObjectMapper(factory);
        objectMapper.registerModule(NeovimJacksonModule.createModule());
        return objectMapper;
    }
}
