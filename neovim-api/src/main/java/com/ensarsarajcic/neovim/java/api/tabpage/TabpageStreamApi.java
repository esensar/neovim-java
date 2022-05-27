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

package com.ensarsarajcic.neovim.java.api.tabpage;

import com.ensarsarajcic.neovim.java.api.BaseStreamApi;
import com.ensarsarajcic.neovim.java.api.NeovimApiClient;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Tabpage;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Window;
import com.ensarsarajcic.neovim.java.api.window.NeovimWindowApi;
import com.ensarsarajcic.neovim.java.api.window.WindowStreamApi;
import com.ensarsarajcic.neovim.java.corerpc.message.RequestMessage;
import com.ensarsarajcic.neovim.java.corerpc.reactive.ReactiveRpcStreamer;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Implementation of {@link TabpageStreamApi} based on {@link ReactiveRpcStreamer}
 */
@NeovimApiClient(name = "full_tabpage_api", target = 9)
public final class TabpageStreamApi extends BaseStreamApi implements NeovimTabpageApi {

    private final Tabpage model;

    public TabpageStreamApi(ReactiveRpcStreamer reactiveRpcStreamer, Tabpage model) {
        super(reactiveRpcStreamer);
        Objects.requireNonNull(model, "tabpage model is required to work with it");
        this.model = model;
    }

    @Override
    public Tabpage get() {
        return model;
    }

    @Override
    public CompletableFuture<List<NeovimWindowApi>> getWindows() {
        return sendWithResponseOfListOfMsgPackType(prepareMessage(LIST_WINDOWS), Window.class)
                .thenApply(windows -> windows.stream()
                        .map(window -> new WindowStreamApi(reactiveRpcStreamer, window)).collect(Collectors.toList()));
    }

    @Override
    public CompletableFuture<NeovimWindowApi> getWindow() {
        return sendWithResponseOfMsgPackType(prepareMessage(GET_WINDOW), Window.class)
                .thenApply(window -> new WindowStreamApi(reactiveRpcStreamer, window));
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
        return "TabpageStreamApi{" + "model=" + model + '}';
    }
}
