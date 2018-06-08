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
                new RequestMessage.Builder("nvim_get_hl_by_id")
                        .addArgument(id)
                        .addArgument(rgb),
                Map.class);
    }

    @Override
    public CompletableFuture<Map> getHighlightByName(String name, boolean rgb) {
        return sendWithResponseOfType(
                new RequestMessage.Builder("nvim_get_hl_by_name")
                        .addArgument(name)
                        .addArgument(rgb),
                Map.class);
    }

    @Override
    public CompletableFuture<Void> attachUI(int width, int height, Map<String, String> options) {
        return sendWithNoResponse(
                new RequestMessage.Builder("nvim_ui_attach")
                        .addArgument(width)
                        .addArgument(height)
                        .addArgument(options));
    }

    @Override
    public CompletableFuture<Void> detachUI() {
        return sendWithNoResponse(new RequestMessage.Builder("nvim_ui_detach"));
    }

    @Override
    public CompletableFuture<Void> resizeUI(int width, int height) {
        return sendWithNoResponse(new RequestMessage.Builder("nvim_ui_try_resize")
                .addArgument(width)
                .addArgument(height));
    }

    @Override
    public CompletableFuture<Object> executeLua(String luaCode, List<String> args) {
        return sendWithGenericResponse(new RequestMessage.Builder("nvim_execute_lua")
                .addArgument(luaCode)
                .addArgument(args));
    }

    @Override
    public CompletableFuture<Void> executeCommand(String command) {
        return sendWithNoResponse(new RequestMessage.Builder("nvim_command").addArgument(command));
    }

    @Override
    public CompletableFuture<Void> setCurrentDir(String directoryPath) {
        return sendWithNoResponse(new RequestMessage.Builder("nvim_set_current_dir").addArgument(directoryPath));
    }

    @Override
    public CompletableFuture<Void> subscribeToEvent(String event) {
        return sendWithNoResponse(new RequestMessage.Builder("nvim_subscribe").addArgument(event));
    }

    @Override
    public CompletableFuture<Void> unsubscribeFromEvent(String event) {
        return sendWithNoResponse(new RequestMessage.Builder("nvim_unsubscribe").addArgument(event));
    }

    @Override
    public CompletableFuture<Object> eval(String expression) {
        return sendWithGenericResponse(new RequestMessage.Builder("nvim_eval").addArgument(expression));
    }

    @Override
    public CompletableFuture<Object> callFunction(String name, List<String> args) {
        return sendWithGenericResponse(new RequestMessage.Builder("nvim_call_function").addArgument(name).addArgument(args));
    }

    @Override
    public CompletableFuture<Void> feedKeys(String keys, String mode, Boolean escape) {
        return sendWithNoResponse(new RequestMessage.Builder("nvim_feedkeys")
                .addArgument(keys)
                .addArgument(mode)
                .addArgument(escape));
    }

    @Override
    public CompletableFuture<Integer> input(String keys) {
        return sendWithResponseOfType(new RequestMessage.Builder("nvim_input").addArgument(keys), Integer.class);
    }

    @Override
    public CompletableFuture<List<VimKeyMap>> getKeymap(String mode) {
        return sendWithResponseOfListType(
                new RequestMessage.Builder("nvim_get_keymap")
                        .addArgument(mode),
                VimKeyMap.class
        );
    }

    @Override
    public CompletableFuture<Void> setUiOption(String name, Object value) {
        return sendWithNoResponse(new RequestMessage.Builder("nvim_ui_set_option")
                .addArgument(name)
                .addArgument(value));
    }

    @Override
    public CompletableFuture<Void> setVariable(String name, Object value) {
        return sendWithNoResponse(new RequestMessage.Builder("nvim_set_var")
                .addArgument(name)
                .addArgument(value));
    }

    @Override
    public CompletableFuture<Object> getVariable(String name) {
        return sendWithGenericResponse(new RequestMessage.Builder("nvim_get_var").addArgument(name));
    }

    @Override
    public CompletableFuture<Void> deleteVariable(String name) {
        return sendWithNoResponse(new RequestMessage.Builder("nvim_del_var").addArgument(name));
    }

    @Override
    public CompletableFuture<Object> getVimVariable(String name) {
        return sendWithGenericResponse(new RequestMessage.Builder("nvim_get_vvar").addArgument(name));
    }

    @Override
    public CompletableFuture<Void> setOption(String name, Object value) {
        return sendWithNoResponse(new RequestMessage.Builder("nvim_set_option")
                .addArgument(name)
                .addArgument(value));
    }

    @Override
    public CompletableFuture<Object> getOption(String name) {
        return sendWithGenericResponse(new RequestMessage.Builder("nvim_get_option").addArgument(name));
    }

    @Override
    public CompletableFuture<Integer> getColorByName(String name) {
        return sendWithResponseOfType(
                new RequestMessage.Builder("nvim_get_color_by_name")
                        .addArgument(name),
                Integer.class);
    }

    @Override
    public CompletableFuture<String> replaceTermcodes(String strToReplace, boolean fromPart, boolean doLt, boolean special) {
        return sendWithResponseOfType(
                new RequestMessage.Builder("nvim_replace_termcodes")
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
                new RequestMessage.Builder("nvim_command_output")
                        .addArgument(command),
                String.class
        );
    }

    @Override
    public CompletableFuture<Void> writeToOutput(String text) {
        return sendWithNoResponse(new RequestMessage.Builder("nvim_out_write").addArgument(text));
    }

    @Override
    public CompletableFuture<Void> writeToError(String text) {
        return sendWithNoResponse(new RequestMessage.Builder("nvim_err_write").addArgument(text));
    }

    @Override
    public CompletableFuture<Void> writelnToError(String text) {
        return sendWithNoResponse(new RequestMessage.Builder("nvim_err_writeln").addArgument(text));
    }

    @Override
    public CompletableFuture<Integer> stringWidth(String string) {
        return sendWithResponseOfType(
                new RequestMessage.Builder("nvim_strwidth")
                        .addArgument(string),
                Integer.class
        );
    }

    @Override
    public CompletableFuture<List<String>> listRuntimePaths() {
        return sendWithResponseOfListType(
                new RequestMessage.Builder("nvim_list_runtime_paths"), String.class);
    }

    @Override
    public CompletableFuture<String> getCurrentLine() {
        return sendWithResponseOfType(
                new RequestMessage.Builder("nvim_get_current_line"),
                String.class
        );
    }

    @Override
    public CompletableFuture<Void> setCurrentLine(String lineContent) {
        return sendWithNoResponse(new RequestMessage.Builder("nvim_set_current_line").addArgument(lineContent));
    }

    @Override
    public CompletableFuture<Void> deleteCurrentLine() {
        return sendWithNoResponse(new RequestMessage.Builder("nvim_del_current_line"));
    }

    @Override
    public CompletableFuture<List<NeovimBufferApi>> getBuffers() {
        return sendWithResponseOfListOfMsgPackType(new RequestMessage.Builder("nvim_list_bufs"), Buffer.class)
                .thenApply(buffers -> buffers.stream()
                        .map((Function<Buffer, NeovimBufferApi>) buffer
                                        -> new BufferStreamApi(reactiveRPCStreamer, buffer))
                        .collect(Collectors.toList()));
    }

    @Override
    public CompletableFuture<NeovimBufferApi> getCurrentBuffer() {
        return sendWithResponseOfMsgPackType(new RequestMessage.Builder("nvim_get_current_buf"), Buffer.class)
                .thenApply(buffer -> new BufferStreamApi(reactiveRPCStreamer, buffer));
    }

    @Override
    public CompletableFuture<Void> setCurrentBuffer(Buffer buffer) {
        return sendWithNoResponse(new RequestMessage.Builder("nvim_set_current_buf").addArgument(buffer));
    }

    @Override
    public CompletableFuture<Void> setCurrentWindow(Window window) {
        return sendWithNoResponse(new RequestMessage.Builder("nvim_set_current_win").addArgument(window));
    }

    @Override
    public CompletableFuture<Void> setCurrentTabpage(Tabpage tabpage) {
        return sendWithNoResponse(new RequestMessage.Builder("nvim_set_current_tabpage").addArgument(tabpage));
    }

    @Override
    public CompletableFuture<List<Window>> getWindows() {
        return sendWithResponseOfListOfMsgPackType(new RequestMessage.Builder("nvim_list_wins"), Window.class);
    }

    @Override
    public CompletableFuture<Window> getCurrentWindow() {
        return sendWithResponseOfMsgPackType(new RequestMessage.Builder("nvim_get_current_win"), Window.class);
    }

    @Override
    public CompletableFuture<List<Tabpage>> getTabpages() {
        return sendWithResponseOfListOfMsgPackType(new RequestMessage.Builder("nvim_list_tabpages"), Tabpage.class);
    }

    @Override
    public CompletableFuture<Tabpage> getCurrentTabpage() {
        return sendWithResponseOfMsgPackType(new RequestMessage.Builder("nvim_get_current_tabpage"), Tabpage.class);
    }

    @Override
    public CompletableFuture<VimColorMap> getColorMap() {
        return sendWithResponseOfMapType(new RequestMessage.Builder("nvim_get_color_map"), String.class, Integer.class)
                .thenApply(VimColorMap::new);
    }

    @Override
    public CompletableFuture<VimMode> getMode() {
        return sendWithResponseOfType(new RequestMessage.Builder("nvim_get_mode"), VimMode.class);
    }

    @Override
    public CompletableFuture<ApiInfo> getApiInfo() {
        return sendWithResponseOfType(new RequestMessage.Builder("nvim_get_api_info"), ApiInfo.class);
    }

}
