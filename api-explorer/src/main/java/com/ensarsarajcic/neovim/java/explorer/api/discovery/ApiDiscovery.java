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

package com.ensarsarajcic.neovim.java.explorer.api.discovery;

import com.ensarsarajcic.neovim.java.explorer.api.NeovimApiList;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.msgpack.jackson.dataformat.MessagePackFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class ApiDiscovery {

    private static List<String> createArgs(String executable) {
        List<String> allArgs = new ArrayList<>(2);
        allArgs.add(executable);
        allArgs.add("--api-info");
        return allArgs;
    }

    public static NeovimApiList discoverApi() throws IOException {
        ProcessBuilder pb = new ProcessBuilder(createArgs("nvim"));
        Process neovim = pb.start();
        MessagePackFactory factory = new MessagePackFactory();
        factory.disable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
        factory.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        return new ObjectMapper(factory).readerFor(NeovimApiList.class).readValue(neovim.getInputStream());
    }
}
