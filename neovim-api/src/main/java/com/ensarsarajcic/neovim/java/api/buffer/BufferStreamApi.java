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

import com.ensarsarajcic.neovim.java.api.BaseStreamApi;
import com.ensarsarajcic.neovim.java.api.NeovimApiClient;
import com.ensarsarajcic.neovim.java.api.types.api.CommandInfo;
import com.ensarsarajcic.neovim.java.api.types.api.DeleteBufferOptions;
import com.ensarsarajcic.neovim.java.api.types.api.ExtmarkInfo;
import com.ensarsarajcic.neovim.java.api.types.api.GetBufferExtmarkOptions;
import com.ensarsarajcic.neovim.java.api.types.api.GetBufferExtmarksOptions;
import com.ensarsarajcic.neovim.java.api.types.api.GetCommandsOptions;
import com.ensarsarajcic.neovim.java.api.types.api.HighlightedText;
import com.ensarsarajcic.neovim.java.api.types.api.VimCoords;
import com.ensarsarajcic.neovim.java.api.types.api.VimKeyMap;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Buffer;
import com.ensarsarajcic.neovim.java.corerpc.message.RequestMessage;
import com.ensarsarajcic.neovim.java.corerpc.reactive.ReactiveRpcStreamer;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * Implementation of {@link NeovimBufferApi} based on {@link ReactiveRpcStreamer}
 */
@NeovimApiClient(name = "full_buffer_api", target = 9)
public final class BufferStreamApi extends BaseStreamApi implements NeovimBufferApi {

    private final Buffer model;

    public BufferStreamApi(ReactiveRpcStreamer reactiveRpcStreamer,
                           Buffer model) {
        super(reactiveRpcStreamer);
        Objects.requireNonNull(model, "buffer model is required to work with it");
        this.model = model;
    }

    @Override
    public Buffer get() {
        return model;
    }

    @Override
    public CompletableFuture<Integer> getLineCount() {
        return sendWithResponseOfType(
                prepareMessage(GET_LINE_COUNT), Integer.class
        );
    }

    @Override
    public CompletableFuture<List<String>> getLines(int start, int end, boolean strictIndexing) {
        return sendWithResponseOfListType(
                prepareMessage(GET_LINES)
                        .addArgument(start)
                        .addArgument(end)
                        .addArgument(strictIndexing),
                String.class
        );
    }

    @Override
    public CompletableFuture<Void> setLines(int start, int end, boolean strictIndexing, List<String> replacement) {
        return sendWithNoResponse(
                prepareMessage(SET_LINES)
                        .addArgument(start)
                        .addArgument(end)
                        .addArgument(strictIndexing)
                        .addArgument(replacement)
        );
    }

    @Override
    public CompletableFuture<Integer> getOffset(int index) {
        return sendWithResponseOfType(
                prepareMessage(GET_OFFSET)
                        .addArgument(index),
                Integer.class
        );
    }

    @Override
    public CompletableFuture<Object> getVar(String name) {
        return sendWithGenericResponse(
                prepareMessage(GET_VAR).addArgument(name)
        );
    }

    @Override
    public CompletableFuture<Void> deleteVar(String name) {
        return sendWithNoResponse(prepareMessage(DEL_VAR).addArgument(name));
    }

    @Override
    public CompletableFuture<Void> setVar(String name, Object value) {
        return sendWithNoResponse(prepareMessage(SET_VAR).addArgument(name).addArgument(value));
    }

    @Override
    public CompletableFuture<Object> getOption(String name) {
        return sendWithGenericResponse(prepareMessage(GET_OPTION).addArgument(name));
    }

    @Override
    public CompletableFuture<Void> setOption(String name, Object value) {
        return sendWithNoResponse(prepareMessage(SET_OPTION).addArgument(name).addArgument(value));
    }

    @Override
    public CompletableFuture<Integer> getNumber() {
        return sendWithResponseOfType(prepareMessage(GET_NUMBER), Integer.class);
    }

    @Override
    public CompletableFuture<String> getName() {
        return sendWithResponseOfType(prepareMessage(GET_NAME), String.class);
    }

    @Override
    public CompletableFuture<Void> setName(String name) {
        return sendWithNoResponse(prepareMessage(SET_NAME).addArgument(name));
    }

    @Override
    public CompletableFuture<Boolean> isLoaded() {
        return sendWithResponseOfType(prepareMessage(IS_LOADED), Boolean.class);
    }

    @Override
    public CompletableFuture<Boolean> isValid() {
        return sendWithResponseOfType(prepareMessage(IS_VALID), Boolean.class);
    }

    @Override
    public CompletableFuture<VimCoords> getMark(String name) {
        return sendWithResponseOfType(prepareMessage(GET_MARK).addArgument(name), VimCoords.class);
    }

    @Override
    public CompletableFuture<Object> getChangedTick() {
        return sendWithGenericResponse(prepareMessage(GET_CHANGEDTICK));
    }

    @Override
    public CompletableFuture<List<VimKeyMap>> getKeymap(String mode) {
        return sendWithResponseOfListType(prepareMessage(GET_KEYMAP).addArgument(mode), VimKeyMap.class);
    }

    @Override
    public CompletableFuture<Void> setKeymap(String mode, String lhs, String rhs, Map<String, Boolean> options) {
        return sendWithNoResponse(prepareMessage(SET_KEYMAP)
                .addArgument(mode)
                .addArgument(lhs)
                .addArgument(rhs)
                .addArgument(options));
    }

    @Override
    public CompletableFuture<Void> deleteKeymap(String mode, String lhs) {
        return sendWithNoResponse(prepareMessage(DEL_KEYMAP)
                .addArgument(mode)
                .addArgument(lhs));
    }

    @Override
    public CompletableFuture<Integer> addHighlight(int srcId, String hlGroup, int line, int colStart, int colEnd) {
        return sendWithResponseOfType(
                prepareMessage(ADD_HIGHLIGHT)
                        .addArgument(srcId)
                        .addArgument(hlGroup)
                        .addArgument(line)
                        .addArgument(colStart)
                        .addArgument(colEnd),
                Integer.class
        );
    }

    @Override
    public CompletableFuture<Void> clearHighlight(int srcId, int lineStart, int lineEnd) {
        return sendWithNoResponse(
                prepareMessage(CLEAR_HIGHLIGHT)
                        .addArgument(srcId)
                        .addArgument(lineStart)
                        .addArgument(lineEnd)
        );
    }

    @Override
    public CompletableFuture<Void> clearNamespace(int namespaceId, int lineStart, int lineEnd) {
        return sendWithNoResponse(
                prepareMessage(CLEAR_NAMESPACE)
                        .addArgument(namespaceId)
                        .addArgument(lineStart)
                        .addArgument(lineEnd)
        );
    }

    @Override
    public CompletableFuture<Integer> setVirtualText(int namespaceId, int line, List<HighlightedText> chunks, Map optionalParams) {
        return sendWithResponseOfType(
                prepareMessage(SET_VIRTUAL_TEXT)
                        .addArgument(namespaceId)
                        .addArgument(line)
                        .addArgument(chunks)
                        .addArgument(optionalParams),
                Integer.class
        );
    }

    @Override
    public CompletableFuture<Boolean> attach(boolean loadFullBufferOnStart, Map opts) {
        return sendWithResponseOfType(
                prepareMessage(ATTACH_BUFFER)
                        .addArgument(loadFullBufferOnStart)
                        .addArgument(opts),
                Boolean.class
        );
    }

    @Override
    public CompletableFuture<Boolean> detach() {
        return sendWithResponseOfType(
                prepareMessage(DETACH_BUFFER),
                Boolean.class
        );
    }

    @Override
    public CompletableFuture<Map<String, CommandInfo>> getCommands(GetCommandsOptions commandsOptions) {
        return sendWithResponseOfMapType(
                prepareMessage(GET_COMMANDS).addArgument(commandsOptions),
                String.class, CommandInfo.class
        );
    }

    @Override
    public CompletableFuture<Void> setText(int startRow, int startCol, int endRow, int endCol, List<String> lines) {
        return sendWithNoResponse(
                prepareMessage(SET_TEXT)
                        .addArgument(startRow)
                        .addArgument(startCol)
                        .addArgument(endRow)
                        .addArgument(endCol)
                        .addArgument(lines));
    }

    @Override
    public CompletableFuture<Void> delete(DeleteBufferOptions options) {
        return sendWithNoResponse(prepareMessage(DELETE).addArgument(options));
    }

    @Override
    public CompletableFuture<Object> call(Object luaFun) {
        return sendWithGenericResponse(prepareMessage(CALL).addArgument(luaFun));
    }

    @Override
    public CompletableFuture<VimCoords> getExtmarkById(int nsId, int extmarkId, GetBufferExtmarkOptions options) {
        return sendWithResponseOfType(
                prepareMessage(GET_EXTMARK_BY_ID)
                        .addArgument(nsId)
                        .addArgument(extmarkId)
                        .addArgument(options),
                VimCoords.class);
    }

    @Override
    public CompletableFuture<List<ExtmarkInfo>> getExtmarks(int nsId, VimCoords start, VimCoords end, GetBufferExtmarksOptions options) {
        return sendWithResponseOfListType(
                prepareMessage(GET_EXTMARKS)
                        .addArgument(nsId)
                        .addArgument(start)
                        .addArgument(end)
                        .addArgument(options),
                ExtmarkInfo.class
        );
    }

    @Override
    public CompletableFuture<Integer> setExtmark(int nsId, int line, int col, Map<String, Object> options) {
        return sendWithResponseOfType(
                prepareMessage(SET_EXTMARK)
                        .addArgument(nsId)
                        .addArgument(line)
                        .addArgument(col)
                        .addArgument(options),
                Integer.class
        );
    }

    @Override
    public CompletableFuture<Boolean> deleteExtmark(int nsId, int extmarkId) {
        return sendWithResponseOfType(
                prepareMessage(DEL_EXTMARK)
                        .addArgument(nsId)
                        .addArgument(extmarkId),
                Boolean.class
        );
    }

    @Override
    public CompletableFuture<Boolean> deleteMark(String name) {
        return sendWithResponseOfType(
                prepareMessage(DEL_MARK)
                        .addArgument(name),
                Boolean.class
        );
    }

    @Override
    public CompletableFuture<Boolean> setMark(String name, int line, int col, Map<String, Object> options) {
        return sendWithResponseOfType(
                prepareMessage(SET_MARK)
                        .addArgument(name)
                        .addArgument(line)
                        .addArgument(col)
                        .addArgument(options),
                Boolean.class
        );
    }

    @Override
    public CompletableFuture<List<String>> getText(int startRow, int startCol, int endRow, int endCol, Map<String, Object> options) {
        return sendWithResponseOfListType(
                prepareMessage(GET_TEXT)
                        .addArgument(startRow)
                        .addArgument(startCol)
                        .addArgument(endRow)
                        .addArgument(endCol)
                        .addArgument(options),
                String.class
        );
    }

    @Override
    public CompletableFuture<Void> createUserCommand(String name, String command, Map<String, Object> options) {
        return sendWithNoResponse(
                prepareMessage(CREATE_USER_COMMAND)
                        .addArgument(name)
                        .addArgument(command)
                        .addArgument(options)
        );
    }

    @Override
    public CompletableFuture<Void> deleteUserCommand(String name) {
        return sendWithNoResponse(
                prepareMessage(DEL_USER_COMMAND)
                        .addArgument(name)
        );
    }

    private RequestMessage.Builder prepareMessage(String name) {
        return new RequestMessage.Builder(name).addArgument(model);
    }

    @Override
    public String toString() {
        return "BufferStreamApi{" + "model=" + model + '}';
    }
}
