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

import com.ensarsarajcic.neovim.java.api.NeovimApiFunction;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Tabpage;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Window;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface NeovimTabpageApi {

    // region Supported functions names
    String LIST_WINDOWS = "nvim_tabpage_list_wins";
    String GET_WINDOW = "nvim_tabpage_get_win";
    String GET_VAR = "nvim_tabpage_get_var";
    String DEL_VAR = "nvim_tabpage_del_var";
    String SET_VAR = "nvim_tabpage_set_var";
    String GET_NUMBER = "nvim_tabpage_get_number";
    String IS_VALID = "nvim_tabpage_is_valid";
    // endregion

    Tabpage get();

    @NeovimApiFunction(name = LIST_WINDOWS, since = 1)
    CompletableFuture<List<Window>> getWindows();

    @NeovimApiFunction(name = GET_WINDOW, since = 1)
    CompletableFuture<Window> getWindow();

    @NeovimApiFunction(name = GET_VAR, since = 1)
    CompletableFuture<Object> getVar(String name);

    @NeovimApiFunction(name = SET_VAR, since = 1)
    CompletableFuture<Void> setVar(String name, Object value);

    @NeovimApiFunction(name = DEL_VAR, since = 1)
    CompletableFuture<Void> deleteVar(String name);

    @NeovimApiFunction(name = GET_NUMBER, since = 1)
    CompletableFuture<Integer> getNumber();

    @NeovimApiFunction(name = IS_VALID, since = 1)
    CompletableFuture<Boolean> isValid();
}
