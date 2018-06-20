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

package com.ensarsarajcic.neovim.java.rxapi;

import com.ensarsarajcic.neovim.java.api.buffer.NeovimBufferApi;
import com.ensarsarajcic.neovim.java.api.types.api.CommandInfo;
import com.ensarsarajcic.neovim.java.api.types.api.GetCommandsOptions;
import com.ensarsarajcic.neovim.java.api.types.api.VimCoords;
import com.ensarsarajcic.neovim.java.api.types.api.VimKeyMap;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Buffer;
import io.reactivex.Completable;
import io.reactivex.Single;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class NeovimBufferRxWrapper implements NeovimBufferRxApi {

    private NeovimBufferApi neovimBufferApi;

    public NeovimBufferRxWrapper(NeovimBufferApi neovimBufferApi) {
        Objects.requireNonNull(neovimBufferApi, "neovimBufferApi is required to wrap it in RX interface");
        this.neovimBufferApi = neovimBufferApi;
    }

    @Override
    public Buffer get() {
        return neovimBufferApi.get();
    }

    @Override
    public Single<Integer> getLineCount() {
        return Single.fromFuture(neovimBufferApi.getLineCount());
    }

    @Override
    public Single<List<String>> getLines(int start, int end, boolean strictIndexing) {
        return Single.fromFuture(neovimBufferApi.getLines(start, end, strictIndexing));
    }

    @Override
    public Completable setLines(int start, int end, boolean strictIndexing, List<String> replacement) {
        return Completable.fromFuture(neovimBufferApi.setLines(start, end, strictIndexing, replacement));
    }

    @Override
    public Single<Object> getVar(String name) {
        return Single.fromFuture(neovimBufferApi.getVar(name));
    }

    @Override
    public Completable deleteVar(String name) {
        return Completable.fromFuture(neovimBufferApi.deleteVar(name));
    }

    @Override
    public Completable setVar(String name, Object value) {
        return Completable.fromFuture(neovimBufferApi.setVar(name, value));
    }

    @Override
    public Single<Object> getOption(String name) {
        return Single.fromFuture(neovimBufferApi.getOption(name));
    }

    @Override
    public Completable setOption(String name, Object value) {
        return Completable.fromFuture(neovimBufferApi.setOption(name, value));
    }

    @Override
    public Single<Integer> getNumber() {
        return Single.fromFuture(neovimBufferApi.getNumber());
    }

    @Override
    public Single<String> getName() {
        return Single.fromFuture(neovimBufferApi.getName());
    }

    @Override
    public Completable setName(String name) {
        return Completable.fromFuture(neovimBufferApi.setName(name));
    }

    @Override
    public Single<Boolean> isValid() {
        return Single.fromFuture(neovimBufferApi.isValid());
    }

    @Override
    public Single<VimCoords> getMark(String name) {
        return Single.fromFuture(neovimBufferApi.getMark(name));
    }

    @Override
    public Single<Object> getChangedTick() {
        return Single.fromFuture(neovimBufferApi.getChangedTick());
    }

    @Override
    public Single<List<VimKeyMap>> getKeymap(String mode) {
        return Single.fromFuture(neovimBufferApi.getKeymap(mode));
    }

    @Override
    public Single<Integer> addHighlight(int srcId, String hlGroup, int line, int colStart, int colEnd) {
        return Single.fromFuture(neovimBufferApi.addHighlight(srcId, hlGroup, line, colStart, colEnd));
    }

    @Override
    public Completable clearHighlight(int srcId, int lineStart, int lineEnd) {
        return Completable.fromFuture(neovimBufferApi.clearHighlight(srcId, lineStart, lineEnd));
    }

    @Override
    public Single<Boolean> attach(boolean loadFullBufferOnStart, Map opts) {
        return Single.fromFuture(neovimBufferApi.attach(loadFullBufferOnStart, opts));
    }

    @Override
    public Single<Boolean> detach() {
        return Single.fromFuture(neovimBufferApi.detach());
    }

    @Override
    public Single<Map<String, CommandInfo>> getCommands(GetCommandsOptions commandsOptions) {
        return Single.fromFuture(neovimBufferApi.getCommands(commandsOptions));
    }
}
