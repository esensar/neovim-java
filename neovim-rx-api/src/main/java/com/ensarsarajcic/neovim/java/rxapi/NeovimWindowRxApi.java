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

import com.ensarsarajcic.neovim.java.api.NeovimApiFunction;
import com.ensarsarajcic.neovim.java.api.types.api.VimCoords;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Window;
import com.ensarsarajcic.neovim.java.api.window.NeovimWindowApi;
import io.reactivex.Completable;
import io.reactivex.Single;

public interface NeovimWindowRxApi {

    Window get();

    @NeovimApiFunction(name = NeovimWindowApi.GET_BUFFER, since = 1)
    Single<NeovimBufferRxApi> getBuffer();

    @NeovimApiFunction(name = NeovimWindowApi.GET_CURSOR, since = 1)
    Single<VimCoords> getCursor();

    @NeovimApiFunction(name = NeovimWindowApi.SET_CURSOR, since = 1)
    Completable setCursor(VimCoords vimCoords);

    @NeovimApiFunction(name = NeovimWindowApi.GET_HEIGHT, since = 1)
    Single<Integer> getHeight();

    @NeovimApiFunction(name = NeovimWindowApi.SET_HEIGHT, since = 1)
    Completable setHeight(int height);

    @NeovimApiFunction(name = NeovimWindowApi.GET_WIDTH, since = 1)
    Single<Integer> getWidth();

    @NeovimApiFunction(name = NeovimWindowApi.SET_WIDTH, since = 1)
    Completable setWidth(int width);

    @NeovimApiFunction(name = NeovimWindowApi.GET_VAR, since = 1)
    Single<Object> getVar(String name);

    @NeovimApiFunction(name = NeovimWindowApi.SET_VAR, since = 1)
    Completable setVar(String name, Object value);

    @NeovimApiFunction(name = NeovimWindowApi.DEL_VAR, since = 1)
    Completable deleteVar(String name);

    @NeovimApiFunction(name = NeovimWindowApi.GET_OPTION, since = 1)
    Single<Object> getOption(String name);

    @NeovimApiFunction(name = NeovimWindowApi.SET_OPTION, since = 1)
    Completable setOption(String name, Object value);

    @NeovimApiFunction(name = NeovimWindowApi.GET_TABPAGE, since = 1)
    Single<NeovimTabpageRxApi> getTabpage();

    @NeovimApiFunction(name = NeovimWindowApi.GET_NUMBER, since = 1)
    Single<Integer> getNumber();

    @NeovimApiFunction(name = NeovimWindowApi.IS_VALID, since = 1)
    Single<Boolean> isValid();
}
