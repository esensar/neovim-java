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

import com.ensarsarajcic.neovim.java.api.types.api.VimCoords;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Buffer;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Window;
import com.ensarsarajcic.neovim.java.api.window.NeovimWindowApi;
import io.reactivex.Completable;
import io.reactivex.Single;

import java.util.Map;
import java.util.Objects;

public final class NeovimWindowRxWrapper implements NeovimWindowRxApi {

    private NeovimWindowApi neovimWindowApi;

    public NeovimWindowRxWrapper(NeovimWindowApi neovimWindowApi) {
        Objects.requireNonNull(neovimWindowApi, "neovimWindowApi is required to wrap it in RX interface");
        this.neovimWindowApi = neovimWindowApi;
    }

    @Override
    public Window get() {
        return neovimWindowApi.get();
    }

    @Override
    public Single<NeovimBufferRxApi> getBuffer() {
        return Single.fromFuture(neovimWindowApi.getBuffer())
                .map(NeovimBufferRxWrapper::new);
    }

    @Override
    public Completable setBuffer(Buffer buffer) {
        return Completable.fromFuture(neovimWindowApi.setBuffer(buffer));
    }

    @Override
    public Single<VimCoords> getCursor() {
        return Single.fromFuture(neovimWindowApi.getCursor());
    }

    @Override
    public Completable setCursor(VimCoords vimCoords) {
        return Completable.fromFuture(neovimWindowApi.setCursor(vimCoords));
    }

    @Override
    public Single<Integer> getHeight() {
        return Single.fromFuture(neovimWindowApi.getHeight());
    }

    @Override
    public Completable setHeight(int height) {
        return Completable.fromFuture(neovimWindowApi.setHeight(height));
    }

    @Override
    public Single<Integer> getWidth() {
        return Single.fromFuture(neovimWindowApi.getWidth());
    }

    @Override
    public Completable setWidth(int width) {
        return Completable.fromFuture(neovimWindowApi.setWidth(width));
    }

    @Override
    public Single<Object> getVar(String name) {
        return Single.fromFuture(neovimWindowApi.getVar(name));
    }

    @Override
    public Completable setVar(String name, Object value) {
        return Completable.fromFuture(neovimWindowApi.setVar(name, value));
    }

    @Override
    public Completable deleteVar(String name) {
        return Completable.fromFuture(neovimWindowApi.deleteVar(name));
    }

    @Override
    public Single<Object> getOption(String name) {
        return Single.fromFuture(neovimWindowApi.getOption(name));
    }

    @Override
    public Completable setOption(String name, Object value) {
        return Completable.fromFuture(neovimWindowApi.setOption(name, value));
    }

    @Override
    public Single<Map<String, Object>> getConfig() {
        return Single.fromFuture(neovimWindowApi.getConfig());
    }

    @Override
    public Completable setConfig(Map<String, Object> config) {
        return Completable.fromFuture(neovimWindowApi.setConfig(config));
    }

    @Override
    public Single<NeovimTabpageRxApi> getTabpage() {
        return Single.fromFuture(neovimWindowApi.getTabpage())
                .map(NeovimTabpageRxWrapper::new);
    }

    @Override
    public Single<Integer> getNumber() {
        return Single.fromFuture(neovimWindowApi.getNumber());
    }

    @Override
    public Single<Boolean> isValid() {
        return Single.fromFuture(neovimWindowApi.isValid());
    }

    @Override
    public Completable close(boolean force) {
        return Completable.fromFuture(neovimWindowApi.close(force));
    }
}
