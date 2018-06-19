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

import com.ensarsarajcic.neovim.java.api.tabpage.NeovimTabpageApi;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Tabpage;
import io.reactivex.Completable;
import io.reactivex.Single;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class NeovimTabpageRxWrapper implements NeovimTabpageRxApi {

    private NeovimTabpageApi neovimTabpageApi;

    public NeovimTabpageRxWrapper(NeovimTabpageApi neovimTabpageApi) {
        Objects.requireNonNull(neovimTabpageApi, "neovimTabpageApi is required to wrap it in RX interface");
        this.neovimTabpageApi = neovimTabpageApi;
    }

    @Override
    public Tabpage get() {
        return neovimTabpageApi.get();
    }

    @Override
    public Single<List<NeovimWindowRxApi>> getWindows() {
        return Single.fromFuture(neovimTabpageApi.getWindows())
                .map(neovimWindowApis -> neovimWindowApis.stream()
                        .map(NeovimWindowRxWrapper::new)
                        .collect(Collectors.toList()));
    }

    @Override
    public Single<NeovimWindowRxApi> getWindow() {
        return Single.fromFuture(neovimTabpageApi.getWindow())
                .map(NeovimWindowRxWrapper::new);
    }

    @Override
    public Single<Object> getVar(String name) {
        return Single.fromFuture(neovimTabpageApi.getVar(name));
    }

    @Override
    public Completable setVar(String name, Object value) {
        return Completable.fromFuture(neovimTabpageApi.setVar(name, value));
    }

    @Override
    public Completable deleteVar(String name) {
        return Completable.fromFuture(neovimTabpageApi.deleteVar(name));
    }

    @Override
    public Single<Integer> getNumber() {
        return Single.fromFuture(neovimTabpageApi.getNumber());
    }

    @Override
    public Single<Boolean> isValid() {
        return Single.fromFuture(neovimTabpageApi.isValid());
    }
}
