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

import com.ensarsarajcic.neovim.java.api.NeovimApiFunction;
import com.ensarsarajcic.neovim.java.api.buffer.NeovimBufferApi;
import com.ensarsarajcic.neovim.java.api.tabpage.NeovimTabpageApi;
import com.ensarsarajcic.neovim.java.api.types.api.VimCoords;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Buffer;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Window;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Interface representing neovim windows methods
 */
public interface NeovimWindowApi {
    // region Supported functions names
    String GET_BUFFER = "nvim_win_get_buf";
    String SET_BUFFER = "nvim_win_set_buf";
    String GET_CURSOR = "nvim_win_get_cursor";
    String SET_CURSOR = "nvim_win_set_cursor";
    String GET_HEIGHT = "nvim_win_get_height";
    String SET_HEIGHT = "nvim_win_set_height";
    String GET_WIDTH = "nvim_win_get_width";
    String SET_WIDTH = "nvim_win_set_width";
    String GET_VAR = "nvim_win_get_var";
    String DEL_VAR = "nvim_win_del_var";
    String SET_VAR = "nvim_win_set_var";
    String GET_OPTION = "nvim_win_get_option";
    String SET_OPTION = "nvim_win_set_option";
    String GET_CONFIG = "nvim_win_get_config";
    String SET_CONFIG = "nvim_win_set_config";
    String GET_TABPAGE = "nvim_win_get_tabpage";
    String GET_NUMBER = "nvim_win_get_number";
    String IS_VALID = "nvim_win_is_valid";
    String CLOSE = "nvim_win_close";
    // endregion

    Window get();

    @NeovimApiFunction(name = GET_BUFFER, since = 1)
    CompletableFuture<NeovimBufferApi> getBuffer();

    @NeovimApiFunction(name = SET_BUFFER, since = 5)
    CompletableFuture<Void> setBuffer(Buffer buffer);

    @NeovimApiFunction(name = GET_CURSOR, since = 1)
    CompletableFuture<VimCoords> getCursor();

    @NeovimApiFunction(name = SET_CURSOR, since = 1)
    CompletableFuture<Void> setCursor(VimCoords vimCoords);

    @NeovimApiFunction(name = GET_HEIGHT, since = 1)
    CompletableFuture<Integer> getHeight();

    @NeovimApiFunction(name = SET_HEIGHT, since = 1)
    CompletableFuture<Void> setHeight(int height);

    @NeovimApiFunction(name = GET_WIDTH, since = 1)
    CompletableFuture<Integer> getWidth();

    @NeovimApiFunction(name = SET_WIDTH, since = 1)
    CompletableFuture<Void> setWidth(int width);

    @NeovimApiFunction(name = GET_VAR, since = 1)
    CompletableFuture<Object> getVar(String name);

    @NeovimApiFunction(name = SET_VAR, since = 1)
    CompletableFuture<Void> setVar(String name, Object value);

    @NeovimApiFunction(name = DEL_VAR, since = 1)
    CompletableFuture<Void> deleteVar(String name);

    @NeovimApiFunction(name = GET_OPTION, since = 1)
    CompletableFuture<Object> getOption(String name);

    @NeovimApiFunction(name = SET_OPTION, since = 1)
    CompletableFuture<Void> setOption(String name, Object value);

    @NeovimApiFunction(name = GET_CONFIG, since = 6)
    CompletableFuture<Map<String, Object>> getConfig();

    @NeovimApiFunction(name = SET_CONFIG, since = 6)
    CompletableFuture<Void> setConfig(Map<String, Object> config);

    @NeovimApiFunction(name = GET_TABPAGE, since = 1)
    CompletableFuture<NeovimTabpageApi> getTabpage();

    @NeovimApiFunction(name = GET_NUMBER, since = 1)
    CompletableFuture<Integer> getNumber();

    @NeovimApiFunction(name = IS_VALID, since = 1)
    CompletableFuture<Boolean> isValid();

    @NeovimApiFunction(name = CLOSE, since = 6)
    CompletableFuture<Void> close(boolean force);
}
