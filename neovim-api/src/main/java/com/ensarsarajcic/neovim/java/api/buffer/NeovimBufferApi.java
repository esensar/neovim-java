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
import com.ensarsarajcic.neovim.java.api.types.api.CommandInfo;
import com.ensarsarajcic.neovim.java.api.types.api.GetCommandsOptions;
import com.ensarsarajcic.neovim.java.api.types.api.VimCoords;
import com.ensarsarajcic.neovim.java.api.types.api.VimKeyMap;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Buffer;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Interface representing neovim methods of Buffer object
 */
public interface NeovimBufferApi {

    // region Supported functions names
    String GET_LINE_COUNT = "nvim_buf_line_count";
    String GET_LINES = "nvim_buf_get_lines";
    String SET_LINES = "nvim_buf_set_lines";
    String GET_VAR = "nvim_buf_get_var";
    String DEL_VAR = "nvim_buf_del_var";
    String SET_VAR = "nvim_buf_set_var";
    String GET_OPTION = "nvim_buf_get_option";
    String SET_OPTION = "nvim_buf_set_option";
    String GET_NUMBER = "nvim_buf_get_number";
    String GET_NAME = "nvim_buf_get_name";
    String SET_NAME = "nvim_buf_set_name";
    String IS_VALID = "nvim_buf_is_valid";
    String GET_MARK = "nvim_buf_get_mark";
    String GET_CHANGEDTICK = "nvim_buf_get_changedtick";
    String GET_KEYMAP = "nvim_buf_get_keymap";
    String ADD_HIGHLIGHT = "nvim_buf_add_highlight";
    String CLEAR_HIGHLIGHT = "nvim_buf_clear_highlight";
    String ATTACH_BUFFER = "nvim_buf_attach";
    String DETACH_BUFFER = "nvim_buf_detach";
    String GET_COMMANDS = "nvim_buf_get_commands";
    // endregion

    Buffer get();

    @NeovimApiFunction(name = GET_LINE_COUNT, since = 1)
    CompletableFuture<Integer> getLineCount();

    @NeovimApiFunction(name = GET_LINES, since = 1)
    CompletableFuture<List<String>> getLines(int start, int end, boolean strictIndexing);

    @NeovimApiFunction(name = SET_LINES, since = 1)
    CompletableFuture<Void> setLines(int start, int end, boolean strictIndexing, List<String> replacement);

    @NeovimApiFunction(name = GET_VAR, since = 1)
    CompletableFuture<Object> getVar(String name);

    @NeovimApiFunction(name = DEL_VAR, since = 1)
    CompletableFuture<Void> deleteVar(String name);

    @NeovimApiFunction(name = SET_VAR, since = 1)
    CompletableFuture<Void> setVar(String name, Object value);

    @NeovimApiFunction(name = GET_OPTION, since = 1)
    CompletableFuture<Object> getOption(String name);

    @NeovimApiFunction(name = SET_OPTION, since = 1)
    CompletableFuture<Void> setOption(String name, Object value);

    @NeovimApiFunction(name = GET_NUMBER, since = 1, deprecatedIn = 2)
    @Deprecated
    CompletableFuture<Integer> getNumber();

    @NeovimApiFunction(name = GET_NAME, since = 1)
    CompletableFuture<String> getName();

    @NeovimApiFunction(name = SET_NAME, since = 1)
    CompletableFuture<Void> setName(String name);

    @NeovimApiFunction(name = IS_VALID, since = 1)
    CompletableFuture<Boolean> isValid();

    @NeovimApiFunction(name = GET_MARK, since = 1)
    CompletableFuture<VimCoords> getMark(String name);

    @NeovimApiFunction(name = GET_CHANGEDTICK, since = 2)
    CompletableFuture<Object> getChangedTick();

    @NeovimApiFunction(name = GET_KEYMAP, since = 3)
    CompletableFuture<List<VimKeyMap>> getKeymap(String mode);

    @NeovimApiFunction(name = ADD_HIGHLIGHT, since = 1)
    CompletableFuture<Integer> addHighlight(int srcId, String hlGroup, int line, int colStart, int colEnd);

    @NeovimApiFunction(name = CLEAR_HIGHLIGHT, since = 1)
    CompletableFuture<Void> clearHighlight(int srcId, int lineStart, int lineEnd);

    @NeovimApiFunction(name = ATTACH_BUFFER, since = 4)
    CompletableFuture<Boolean> attach(boolean loadFullBufferOnStart, Map opts);

    @NeovimApiFunction(name = DETACH_BUFFER, since = 4)
    CompletableFuture<Boolean> detach();

    @NeovimApiFunction(name = GET_COMMANDS, since = 4)
    CompletableFuture<Map<String, CommandInfo>> getCommands(GetCommandsOptions commandsOptions);
}
