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
import com.ensarsarajcic.neovim.java.api.buffer.NeovimBufferApi;
import com.ensarsarajcic.neovim.java.api.types.api.VimCoords;
import com.ensarsarajcic.neovim.java.api.types.api.VimKeyMap;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Buffer;
import io.reactivex.Single;

import java.util.List;

public interface NeovimBufferRxApi {
    Buffer get();

    @NeovimApiFunction(name = NeovimBufferApi.GET_LINE_COUNT, since = 1)
    Single<Integer> getLineCount();

    @NeovimApiFunction(name = NeovimBufferApi.GET_LINES, since = 1)
    Single<List<String>> getLines(int start, int end, boolean strictIndexing);

    @NeovimApiFunction(name = NeovimBufferApi.SET_LINES, since = 1)
    Single<Void> setLines(int start, int end, boolean strictIndexing, List<String> replacement);

    @NeovimApiFunction(name = NeovimBufferApi.GET_VAR, since = 1)
    Single<Object> getVar(String name);

    @NeovimApiFunction(name = NeovimBufferApi.DEL_VAR, since = 1)
    Single<Void> deleteVar(String name);

    @NeovimApiFunction(name = NeovimBufferApi.SET_VAR, since = 1)
    Single<Void> setVar(String name, Object value);

    @NeovimApiFunction(name = NeovimBufferApi.GET_OPTION, since = 1)
    Single<Object> getOption(String name);

    @NeovimApiFunction(name = NeovimBufferApi.SET_OPTION, since = 1)
    Single<Void> setOption(String name, Object value);

    @NeovimApiFunction(name = NeovimBufferApi.GET_NUMBER, since = 1, deprecatedIn = 2)
    Single<Integer> getNumber();

    @NeovimApiFunction(name = NeovimBufferApi.GET_NAME, since = 1)
    Single<String> getName();

    @NeovimApiFunction(name = NeovimBufferApi.SET_NAME, since = 1)
    Single<Void> setName(String name);

    @NeovimApiFunction(name = NeovimBufferApi.IS_VALID, since = 1)
    Single<Boolean> isValid();

    @NeovimApiFunction(name = NeovimBufferApi.GET_MARK, since = 1)
    Single<VimCoords> getMark(String name);

    @NeovimApiFunction(name = NeovimBufferApi.GET_CHANGEDTICK, since = 2)
    Single<Object> getChangedTick();

    @NeovimApiFunction(name = NeovimBufferApi.GET_KEYMAP, since = 3)
    Single<List<VimKeyMap>> getKeymap(String mode);

    @NeovimApiFunction(name = NeovimBufferApi.ADD_HIGHLIGHT, since = 1)
    Single<Integer> addHighlight(int srcId, String hlGroup, int line, int colStart, int colEnd);

    @NeovimApiFunction(name = NeovimBufferApi.CLEAR_HIGHLIGHT, since = 1)
    Single<Void> clearHighlight(int srcId, int lineStart, int lineEnd);
}
