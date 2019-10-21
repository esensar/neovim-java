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

package com.ensarsarajcic.neovim.java.api.window;

import com.ensarsarajcic.neovim.java.api.BaseStreamApi;
import com.ensarsarajcic.neovim.java.api.NeovimApiClient;
import com.ensarsarajcic.neovim.java.api.buffer.BufferStreamApi;
import com.ensarsarajcic.neovim.java.api.buffer.NeovimBufferApi;
import com.ensarsarajcic.neovim.java.api.tabpage.NeovimTabpageApi;
import com.ensarsarajcic.neovim.java.api.tabpage.TabpageStreamApi;
import com.ensarsarajcic.neovim.java.api.types.api.VimCoords;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Buffer;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Tabpage;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Window;
import com.ensarsarajcic.neovim.java.corerpc.message.RequestMessage;
import com.ensarsarajcic.neovim.java.corerpc.reactive.ReactiveRPCStreamer;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * Implementation of {@link NeovimWindowApi} based on {@link ReactiveRPCStreamer}
 */
@NeovimApiClient(name = "full_window_api", target = 5)
public final class WindowStreamApi extends BaseStreamApi implements NeovimWindowApi {

    private Window model;

    public WindowStreamApi(ReactiveRPCStreamer reactiveRPCStreamer, Window model) {
        super(reactiveRPCStreamer);
        Objects.requireNonNull(model, "window model is required to work with it");
        this.model = model;
    }

    @Override
    public Window get() {
        return model;
    }

    @Override
    public CompletableFuture<NeovimBufferApi> getBuffer() {
        return sendWithResponseOfMsgPackType(prepareMessage(GET_BUFFER), Buffer.class)
                .thenApply(buffer -> new BufferStreamApi(reactiveRPCStreamer, buffer));
    }

    @Override
    public CompletableFuture<Void> setBuffer(Buffer buffer) {
        return sendWithNoResponse(prepareMessage(SET_BUFFER).addArgument(buffer));
    }

    @Override
    public CompletableFuture<VimCoords> getCursor() {
        return sendWithResponseOfType(prepareMessage(GET_CURSOR), VimCoords.class);
    }

    @Override
    public CompletableFuture<Void> setCursor(VimCoords vimCoords) {
        return sendWithNoResponse(prepareMessage(SET_CURSOR).addArgument(vimCoords));
    }

    @Override
    public CompletableFuture<Integer> getHeight() {
        return sendWithResponseOfType(prepareMessage(GET_HEIGHT), Integer.class);
    }

    @Override
    public CompletableFuture<Void> setHeight(int height) {
        return sendWithNoResponse(prepareMessage(SET_HEIGHT).addArgument(height));
    }

    @Override
    public CompletableFuture<Integer> getWidth() {
        return sendWithResponseOfType(prepareMessage(GET_WIDTH), Integer.class);
    }

    @Override
    public CompletableFuture<Void> setWidth(int width) {
        return sendWithNoResponse(prepareMessage(SET_WIDTH).addArgument(width));
    }

    @Override
    public CompletableFuture<Object> getVar(String name) {
        return sendWithGenericResponse(prepareMessage(GET_VAR).addArgument(name));
    }

    @Override
    public CompletableFuture<Void> setVar(String name, Object value) {
        return sendWithNoResponse(prepareMessage(SET_VAR).addArgument(name).addArgument(value));
    }

    @Override
    public CompletableFuture<Void> deleteVar(String name) {
        return sendWithNoResponse(prepareMessage(DEL_VAR).addArgument(name));
    }

    @Override
    public CompletableFuture<Object> getOption(String name) {
        return sendWithGenericResponse(prepareMessage(GET_OPTION).addArgument(name));
    }

    @Override
    public CompletableFuture<Void> setOption(String name, Object value) {
        return sendWithNoResponse(prepareMessage(SET_OPTION).addArgument(name).addArgument(value));
    }

    @Override
    public CompletableFuture<NeovimTabpageApi> getTabpage() {
        return sendWithResponseOfMsgPackType(prepareMessage(GET_TABPAGE), Tabpage.class)
                .thenApply(tabpage -> new TabpageStreamApi(reactiveRPCStreamer, tabpage));
    }

    @Override
    public CompletableFuture<Integer> getNumber() {
        return sendWithResponseOfType(prepareMessage(GET_NUMBER), Integer.class);
    }

    @Override
    public CompletableFuture<Boolean> isValid() {
        return sendWithResponseOfType(prepareMessage(IS_VALID), Boolean.class);
    }

    private RequestMessage.Builder prepareMessage(String name) {
        return new RequestMessage.Builder(name).addArgument(model);
    }

    @Override
    public String toString() {
        return "WindowStreamApi{" +
                "model=" + model +
                '}';
    }
}
