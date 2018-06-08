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

package com.ensarsarajcic.neovim.java.api;

import com.ensarsarajcic.neovim.java.api.buffer.BufferStreamApi;
import com.ensarsarajcic.neovim.java.api.buffer.NeovimBufferApi;
import com.ensarsarajcic.neovim.java.api.tabpage.NeovimTabpageApi;
import com.ensarsarajcic.neovim.java.api.tabpage.TabpageStreamApi;
import com.ensarsarajcic.neovim.java.api.types.api.VimColorMap;
import com.ensarsarajcic.neovim.java.api.types.api.VimKeyMap;
import com.ensarsarajcic.neovim.java.api.types.api.VimMode;
import com.ensarsarajcic.neovim.java.api.types.msgpack.*;
import com.ensarsarajcic.neovim.java.api.types.apiinfo.ApiInfo;
import com.ensarsarajcic.neovim.java.corerpc.message.RequestMessage;
import com.ensarsarajcic.neovim.java.corerpc.reactive.ReactiveRPCStreamer;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Full implementation of {@link NeovimApi} based on {@link ReactiveRPCStreamer}
 */
@NeovimApiClient(name = "full_stream_api", target = 3)
public final class NeovimStreamApi extends BaseStreamApi implements NeovimApi {

    public NeovimStreamApi(ReactiveRPCStreamer reactiveRPCStreamer) {
        super(reactiveRPCStreamer);
    }

    @Override
    public CompletableFuture<List> sendAtomic(AtomicCallBuilder atomicCallBuilder) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public AtomicCallBuilder prepareAtomic() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    // TODO Add highlight definition
    @Override
    public CompletableFuture<Map> getHighlightById(int id, boolean rgb) {
        return sendWithResponseOfType(
                new RequestMessage.Builder(GET_HIGHLIGHT_BY_ID)
                        .addArgument(id)
                        .addArgument(rgb),
                Map.class);
    }

    @Override
    public CompletableFuture<Map> getHighlightByName(String name, boolean rgb) {
        return sendWithResponseOfType(
                new RequestMessage.Builder(GET_HIGHLIGHT_BY_NAME)
                        .addArgument(name)
                        .addArgument(rgb),
                Map.class);
    }

    @Override
    public CompletableFuture<Void> attachUI(int width, int height, Map<String, String> options) {
        return sendWithNoResponse(
                new RequestMessage.Builder(ATTACH_UI)
                        .addArgument(width)
                        .addArgument(height)
                        .addArgument(options));
    }

    @Override
    public CompletableFuture<Void> detachUI() {
        return sendWithNoResponse(new RequestMessage.Builder(DETACH_UI));
    }

    @Override
    public CompletableFuture<Void> resizeUI(int width, int height) {
        return sendWithNoResponse(new RequestMessage.Builder(RESIZE_UI)
                .addArgument(width)
                .addArgument(height));
    }

    @Override
    public CompletableFuture<Object> executeLua(String luaCode, List<String> args) {
        return sendWithGenericResponse(new RequestMessage.Builder(EXECUTE_LUA)
                .addArgument(luaCode)
                .addArgument(args));
    }

    @Override
    public CompletableFuture<Void> executeCommand(String command) {
        return sendWithNoResponse(new RequestMessage.Builder(EXECUTE_COMMAND).addArgument(command));
    }

    @Override
    public CompletableFuture<Void> setCurrentDir(String directoryPath) {
        return sendWithNoResponse(new RequestMessage.Builder(SET_CURRENT_DIR).addArgument(directoryPath));
    }

    @Override
    public CompletableFuture<Void> subscribeToEvent(String event) {
        return sendWithNoResponse(new RequestMessage.Builder(SUBSCRIBE_TO_EVENT).addArgument(event));
    }

    @Override
    public CompletableFuture<Void> unsubscribeFromEvent(String event) {
        return sendWithNoResponse(new RequestMessage.Builder(UNSUBSCRIBE_FROM_EVENT).addArgument(event));
    }

    @Override
    public CompletableFuture<Object> eval(String expression) {
        return sendWithGenericResponse(new RequestMessage.Builder(EVAL).addArgument(expression));
    }

    @Override
    public CompletableFuture<Object> callFunction(String name, List<String> args) {
        return sendWithGenericResponse(new RequestMessage.Builder(CALL_FUNCTION).addArgument(name).addArgument(args));
    }

    @Override
    public CompletableFuture<Void> feedKeys(String keys, String mode, Boolean escape) {
        return sendWithNoResponse(new RequestMessage.Builder(FEEDKEYS)
                .addArgument(keys)
                .addArgument(mode)
                .addArgument(escape));
    }

    @Override
    public CompletableFuture<Integer> input(String keys) {
        return sendWithResponseOfType(new RequestMessage.Builder(INPUT).addArgument(keys), Integer.class);
    }

    @Override
    public CompletableFuture<List<VimKeyMap>> getKeymap(String mode) {
        return sendWithResponseOfListType(
                new RequestMessage.Builder(GET_KEYMAP)
                        .addArgument(mode),
                VimKeyMap.class
        );
    }

    @Override
    public CompletableFuture<Void> setUiOption(String name, Object value) {
        return sendWithNoResponse(new RequestMessage.Builder(SET_UI_OPTION)
                .addArgument(name)
                .addArgument(value));
    }

    @Override
    public CompletableFuture<Void> setVariable(String name, Object value) {
        return sendWithNoResponse(new RequestMessage.Builder(SET_VAR)
                .addArgument(name)
                .addArgument(value));
    }

    @Override
    public CompletableFuture<Object> getVariable(String name) {
        return sendWithGenericResponse(new RequestMessage.Builder(GET_VAR).addArgument(name));
    }

    @Override
    public CompletableFuture<Void> deleteVariable(String name) {
        return sendWithNoResponse(new RequestMessage.Builder(DEL_VAR).addArgument(name));
    }

    @Override
    public CompletableFuture<Object> getVimVariable(String name) {
        return sendWithGenericResponse(new RequestMessage.Builder(GET_VIM_VARIABLE).addArgument(name));
    }

    @Override
    public CompletableFuture<Void> setOption(String name, Object value) {
        return sendWithNoResponse(new RequestMessage.Builder(SET_OPTION)
                .addArgument(name)
                .addArgument(value));
    }

    @Override
    public CompletableFuture<Object> getOption(String name) {
        return sendWithGenericResponse(new RequestMessage.Builder(GET_OPTION).addArgument(name));
    }

    @Override
    public CompletableFuture<Integer> getColorByName(String name) {
        return sendWithResponseOfType(
                new RequestMessage.Builder(GET_COLOR_BY_NAME)
                        .addArgument(name),
                Integer.class);
    }

    @Override
    public CompletableFuture<String> replaceTermcodes(String strToReplace, boolean fromPart, boolean doLt, boolean special) {
        return sendWithResponseOfType(
                new RequestMessage.Builder(REPLACE_TERMCODES)
                        .addArgument(strToReplace)
                        .addArgument(fromPart)
                        .addArgument(doLt)
                        .addArgument(special),
                String.class
        );
    }

    @Override
    public CompletableFuture<String> commandOutput(String command) {
        return sendWithResponseOfType(
                new RequestMessage.Builder(COMMAND_OUTPUT)
                        .addArgument(command),
                String.class
        );
    }

    @Override
    public CompletableFuture<Void> writeToOutput(String text) {
        return sendWithNoResponse(new RequestMessage.Builder(OUT_WRITE).addArgument(text));
    }

    @Override
    public CompletableFuture<Void> writeToError(String text) {
        return sendWithNoResponse(new RequestMessage.Builder(ERR_WRITE).addArgument(text));
    }

    @Override
    public CompletableFuture<Void> writelnToError(String text) {
        return sendWithNoResponse(new RequestMessage.Builder(ERR_WRITELN).addArgument(text));
    }

    @Override
    public CompletableFuture<Integer> stringWidth(String string) {
        return sendWithResponseOfType(
                new RequestMessage.Builder(STRWIDTH)
                        .addArgument(string),
                Integer.class
        );
    }

    @Override
    public CompletableFuture<List<String>> listRuntimePaths() {
        return sendWithResponseOfListType(
                new RequestMessage.Builder(LIST_RUNTIME_PATHS), String.class);
    }

    @Override
    public CompletableFuture<String> getCurrentLine() {
        return sendWithResponseOfType(
                new RequestMessage.Builder(GET_CURRENT_LINE),
                String.class
        );
    }

    @Override
    public CompletableFuture<Void> setCurrentLine(String lineContent) {
        return sendWithNoResponse(new RequestMessage.Builder(SET_CURRENT_LINE).addArgument(lineContent));
    }

    @Override
    public CompletableFuture<Void> deleteCurrentLine() {
        return sendWithNoResponse(new RequestMessage.Builder(DEL_CURRENT_LINE));
    }

    @Override
    public CompletableFuture<List<NeovimBufferApi>> getBuffers() {
        return sendWithResponseOfListOfMsgPackType(new RequestMessage.Builder(LIST_BUFS), Buffer.class)
                .thenApply(buffers -> buffers.stream()
                        .map((Function<Buffer, NeovimBufferApi>) buffer
                                -> new BufferStreamApi(reactiveRPCStreamer, buffer)).collect(Collectors.toList()));
    }

    @Override
    public CompletableFuture<NeovimBufferApi> getCurrentBuffer() {
        return sendWithResponseOfMsgPackType(new RequestMessage.Builder(GET_CURRENT_BUF), Buffer.class)
                .thenApply(buffer -> new BufferStreamApi(reactiveRPCStreamer, buffer));
    }

    @Override
    public CompletableFuture<Void> setCurrentBuffer(Buffer buffer) {
        return sendWithNoResponse(new RequestMessage.Builder(SET_CURRENT_BUF).addArgument(buffer));
    }

    @Override
    public CompletableFuture<Void> setCurrentWindow(Window window) {
        return sendWithNoResponse(new RequestMessage.Builder(SET_CURRENT_WIN).addArgument(window));
    }

    @Override
    public CompletableFuture<Void> setCurrentTabpage(Tabpage tabpage) {
        return sendWithNoResponse(new RequestMessage.Builder(SET_CURRENT_TABPAGE).addArgument(tabpage));
    }

    @Override
    public CompletableFuture<List<Window>> getWindows() {
        return sendWithResponseOfListOfMsgPackType(new RequestMessage.Builder(LIST_WINS), Window.class);
    }

    @Override
    public CompletableFuture<Window> getCurrentWindow() {
        return sendWithResponseOfMsgPackType(new RequestMessage.Builder(GET_CURRENT_WIN), Window.class);
    }

    @Override
    public CompletableFuture<List<NeovimTabpageApi>> getTabpages() {
        return sendWithResponseOfListOfMsgPackType(new RequestMessage.Builder(LIST_TABPAGES), Tabpage.class)
                .thenApply(tabpages -> tabpages.stream()
                        .map((Function<Tabpage, NeovimTabpageApi>) tabpage
                                -> new TabpageStreamApi(reactiveRPCStreamer, tabpage)).collect(Collectors.toList()));
    }

    @Override
    public CompletableFuture<NeovimTabpageApi> getCurrentTabpage() {
        return sendWithResponseOfMsgPackType(new RequestMessage.Builder(GET_CURRENT_TABPAGE), Tabpage.class)
                .thenApply(tabpage -> new TabpageStreamApi(reactiveRPCStreamer, tabpage));
    }

    @Override
    public CompletableFuture<VimColorMap> getColorMap() {
        return sendWithResponseOfMapType(new RequestMessage.Builder(GET_COLOR_MAP), String.class, Integer.class)
                .thenApply(VimColorMap::new);
    }

    @Override
    public CompletableFuture<VimMode> getMode() {
        return sendWithResponseOfType(new RequestMessage.Builder(GET_MODE), VimMode.class);
    }

    @Override
    public CompletableFuture<ApiInfo> getApiInfo() {
        return sendWithResponseOfType(new RequestMessage.Builder(GET_API_INFO), ApiInfo.class);
    }

}
