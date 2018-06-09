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
import com.ensarsarajcic.neovim.java.api.types.msgpack.Window;
import com.ensarsarajcic.neovim.java.api.window.NeovimWindowApi;
import io.reactivex.Single;

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
    public Single<VimCoords> getCursor() {
        return Single.fromFuture(neovimWindowApi.getCursor());
    }

    @Override
    public Single<Void> setCursor(VimCoords vimCoords) {
        return Single.fromFuture(neovimWindowApi.setCursor(vimCoords));
    }

    @Override
    public Single<Integer> getHeight() {
        return Single.fromFuture(neovimWindowApi.getHeight());
    }

    @Override
    public Single<Void> setHeight(int height) {
        return Single.fromFuture(neovimWindowApi.setHeight(height));
    }

    @Override
    public Single<Integer> getWidth() {
        return Single.fromFuture(neovimWindowApi.getWidth());
    }

    @Override
    public Single<Void> setWidth(int width) {
        return Single.fromFuture(neovimWindowApi.setWidth(width));
    }

    @Override
    public Single<Object> getVar(String name) {
        return Single.fromFuture(neovimWindowApi.getVar(name));
    }

    @Override
    public Single<Void> setVar(String name, Object value) {
        return Single.fromFuture(neovimWindowApi.setVar(name, value));
    }

    @Override
    public Single<Void> deleteVar(String name) {
        return Single.fromFuture(neovimWindowApi.deleteVar(name));
    }

    @Override
    public Single<Object> getOption(String name) {
        return Single.fromFuture(neovimWindowApi.getOption(name));
    }

    @Override
    public Single<Void> setOption(String name, Object value) {
        return Single.fromFuture(neovimWindowApi.setOption(name, value));
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
}
