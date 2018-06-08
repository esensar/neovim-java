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

package com.ensarsarajcic.neovim.java.api.buffer;

import com.ensarsarajcic.neovim.java.api.NeovimApiFunction;
import com.ensarsarajcic.neovim.java.api.types.api.VimCoords;
import com.ensarsarajcic.neovim.java.api.types.api.VimKeyMap;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Buffer;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface NeovimBufferApi {

    Buffer get();

    String GET_LINE_COUNT = "nvim_buf_line_count";
    @NeovimApiFunction(name = GET_LINE_COUNT, since = 1)
    CompletableFuture<Integer> getLineCount();

    String GET_LINES = "nvim_buf_get_lines";
    @NeovimApiFunction(name = GET_LINES, since = 1)
    CompletableFuture<List<String>> getLines(int start, int end, boolean strictIndexing);

    String SET_LINES = "nvim_buf_set_lines";
    @NeovimApiFunction(name = SET_LINES, since = 1)
    CompletableFuture<Void> setLines(int start, int end, boolean strictIndexing, List<String> replacement);

    String GET_VAR = "nvim_buf_get_var";
    @NeovimApiFunction(name = GET_VAR, since = 1)
    CompletableFuture<Object> getVar(String name);

    String DEL_VAR = "nvim_buf_del_var";
    @NeovimApiFunction(name = DEL_VAR, since = 1)
    CompletableFuture<Void> deleteVar(String name);

    String SET_VAR = "nvim_buf_set_var";
    @NeovimApiFunction(name = SET_VAR, since = 1)
    CompletableFuture<Void> setVar(String name, Object value);

    String GET_OPTION = "nvim_buf_get_option";
    @NeovimApiFunction(name = GET_OPTION, since = 1)
    CompletableFuture<Object> getOption(String name);

    String SET_OPTION = "nvim_buf_set_option";
    @NeovimApiFunction(name = SET_OPTION, since = 1)
    CompletableFuture<Void> setOption(String name, Object value);

    String GET_NUMBER = "nvim_buf_get_number";
    @NeovimApiFunction(name = GET_NUMBER, since = 1, deprecatedIn = 2)
    CompletableFuture<Integer> getNumber();

    String GET_NAME = "nvim_buf_get_name";
    @NeovimApiFunction(name = GET_NAME, since = 1)
    CompletableFuture<String> getName();

    String SET_NAME = "nvim_buf_set_name";
    @NeovimApiFunction(name = SET_NAME, since = 1)
    CompletableFuture<Void> setName(String name);

    String IS_VALID = "nvim_buf_is_valid";
    @NeovimApiFunction(name = IS_VALID, since = 1)
    CompletableFuture<Boolean> isValid();

    String GET_MARK = "nvim_buf_get_mark";
    @NeovimApiFunction(name = GET_MARK, since = 1)
    CompletableFuture<VimCoords> getMark(String name);

    String GET_CHANGEDTICK = "nvim_buf_get_changedtick";
    @NeovimApiFunction(name = GET_CHANGEDTICK, since = 2)
    CompletableFuture<Object> getChangedTick();

    String GET_KEYMAP = "nvim_buf_get_keymap";
    @NeovimApiFunction(name = GET_KEYMAP, since = 3)
    CompletableFuture<List<VimKeyMap>> getKeymap(String mode);

    String ADD_HIGHLIGHT = "nvim_buf_add_highlight";
    @NeovimApiFunction(name = ADD_HIGHLIGHT, since = 1)
    CompletableFuture<Integer> addHighlight(int srcId, String hlGroup, int line, int colStart, int colEnd);

    String CLEAR_HIGHLIGHT = "nvim_buf_clear_highlight";
    @NeovimApiFunction(name = CLEAR_HIGHLIGHT, since = 1)
    CompletableFuture<Void> clearHighlight(int srcId, int lineStart, int lineEnd);
}
