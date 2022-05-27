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

import com.ensarsarajcic.neovim.java.api.atomic.AtomicCallResponse;
import com.ensarsarajcic.neovim.java.api.buffer.BufferStreamApi;
import com.ensarsarajcic.neovim.java.api.buffer.NeovimBufferApi;
import com.ensarsarajcic.neovim.java.api.tabpage.NeovimTabpageApi;
import com.ensarsarajcic.neovim.java.api.tabpage.TabpageStreamApi;
import com.ensarsarajcic.neovim.java.api.types.api.ChannelInfo;
import com.ensarsarajcic.neovim.java.api.types.api.ChunkInfo;
import com.ensarsarajcic.neovim.java.api.types.api.ClientAttributes;
import com.ensarsarajcic.neovim.java.api.types.api.ClientType;
import com.ensarsarajcic.neovim.java.api.types.api.ClientVersionInfo;
import com.ensarsarajcic.neovim.java.api.types.api.CommandInfo;
import com.ensarsarajcic.neovim.java.api.types.api.GetCommandsOptions;
import com.ensarsarajcic.neovim.java.api.types.api.MethodInfo;
import com.ensarsarajcic.neovim.java.api.types.api.Mouse;
import com.ensarsarajcic.neovim.java.api.types.api.OptionInfo;
import com.ensarsarajcic.neovim.java.api.types.api.UiInfo;
import com.ensarsarajcic.neovim.java.api.types.api.UiOptions;
import com.ensarsarajcic.neovim.java.api.types.api.VimColorMap;
import com.ensarsarajcic.neovim.java.api.types.api.VimKeyMap;
import com.ensarsarajcic.neovim.java.api.types.api.VimMode;
import com.ensarsarajcic.neovim.java.api.types.apiinfo.ApiInfo;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Buffer;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Tabpage;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Window;
import com.ensarsarajcic.neovim.java.api.window.NeovimWindowApi;
import com.ensarsarajcic.neovim.java.api.window.WindowStreamApi;
import com.ensarsarajcic.neovim.java.corerpc.message.RequestMessage;
import com.ensarsarajcic.neovim.java.corerpc.reactive.ReactiveRpcStreamer;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Full implementation of {@link NeovimApi} based on {@link ReactiveRpcStreamer}
 */
@NeovimApiClient(name = "full_stream_api", target = 6)
public final class NeovimStreamApi extends BaseStreamApi implements NeovimApi {

    public NeovimStreamApi(ReactiveRpcStreamer reactiveRpcStreamer) {
        super(reactiveRpcStreamer);
    }

    @Override
    public CompletableFuture<AtomicCallResponse> sendAtomic(List<RequestMessage> requestMessages) {
        var requestArgs = List.of(
                requestMessages.stream()
                        .map(
                                requestMessage -> List.of(requestMessage.getMethod(), requestMessage.getArguments())
                        ).collect(Collectors.toList())
        );
        return sendWithResponseOfType(
                new RequestMessage.Builder(CALL_ATOMIC)
                        .addArgument(requestArgs),
                AtomicCallResponse.class
        );
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
    public CompletableFuture<Void> attachUi(int width, int height, UiOptions options) {
        return sendWithNoResponse(
                new RequestMessage.Builder(ATTACH_UI)
                        .addArgument(width)
                        .addArgument(height)
                        .addArgument(options));
    }

    @Override
    public CompletableFuture<Void> detachUi() {
        return sendWithNoResponse(new RequestMessage.Builder(DETACH_UI));
    }

    @Override
    public CompletableFuture<Void> resizeUi(int width, int height) {
        return sendWithNoResponse(new RequestMessage.Builder(RESIZE_UI)
                .addArgument(width)
                .addArgument(height));
    }

    @Override
    public CompletableFuture<Void> resizeUiGrid(int width, int height) {
        return sendWithNoResponse(new RequestMessage.Builder(RESIZE_UI_GRID)
                .addArgument(width)
                .addArgument(height));
    }

    @Override
    public CompletableFuture<Void> setPopupmenuHeight(int height) {
        return sendWithNoResponse(new RequestMessage.Builder(SET_POPUPMENU_HEIGHT).addArgument(height));
    }

    @Override
    public CompletableFuture<Void> inputMouse(Mouse.Button button, Mouse.Action action, String modifier, int grid, int row, int col) {
        return sendWithNoResponse(new RequestMessage.Builder(INPUT_MOUSE)
                .addArgument(button)
                .addArgument(action)
                .addArgument(grid)
                .addArgument(row)
                .addArgument(col));
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
    public CompletableFuture<Object> callFunction(String name, List<Object> args) {
        return sendWithGenericResponse(new RequestMessage.Builder(CALL_FUNCTION).addArgument(name).addArgument(args));
    }

    @Override
    public CompletableFuture<Void> feedKeys(String keys, String mode, boolean escape) {
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
    public CompletableFuture<Void> setKeymap(String mode, String lhs, String rhs, Map<String, Boolean> options) {
        return sendWithNoResponse(new RequestMessage.Builder(SET_KEYMAP)
                .addArgument(mode)
                .addArgument(lhs)
                .addArgument(rhs)
                .addArgument(options));
    }

    @Override
    public CompletableFuture<Void> deleteKeymap(String mode, String lhs) {
        return sendWithNoResponse(new RequestMessage.Builder(DEL_KEYMAP)
                .addArgument(mode)
                .addArgument(lhs));
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
    public CompletableFuture<Void> setVimVariable(String name, Object value) {
        return sendWithNoResponse(new RequestMessage.Builder(SET_VIM_VARIABLE)
                .addArgument(name)
                .addArgument(value));
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
                        .map(buffer -> new BufferStreamApi(reactiveRpcStreamer, buffer)).collect(Collectors.toList()));
    }

    @Override
    public CompletableFuture<NeovimBufferApi> createBuffer(boolean listed, boolean scratch) {
        return sendWithResponseOfMsgPackType(
                new RequestMessage.Builder(CREATE_BUF)
                        .addArgument(listed)
                        .addArgument(scratch),
                Buffer.class)
                .thenApply(buffer -> new BufferStreamApi(reactiveRpcStreamer, buffer));
    }

    @Override
    public CompletableFuture<NeovimBufferApi> getCurrentBuffer() {
        return sendWithResponseOfMsgPackType(new RequestMessage.Builder(GET_CURRENT_BUF), Buffer.class)
                .thenApply(buffer -> new BufferStreamApi(reactiveRpcStreamer, buffer));
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
    public CompletableFuture<List<NeovimWindowApi>> getWindows() {
        return sendWithResponseOfListOfMsgPackType(new RequestMessage.Builder(LIST_WINS), Window.class)
                .thenApply(windows -> windows.stream()
                        .map(window -> new WindowStreamApi(reactiveRpcStreamer, window)).collect(Collectors.toList()));
    }

    @Override
    public CompletableFuture<NeovimWindowApi> openWindow(Buffer buffer, boolean enter, Map<String, Object> config) {
        return sendWithResponseOfMsgPackType(new RequestMessage.Builder(OPEN_WIN)
                        .addArgument(buffer)
                        .addArgument(enter)
                        .addArgument(config),
                Window.class)
                .thenApply(window -> new WindowStreamApi(reactiveRpcStreamer, window));
    }

    @Override
    public CompletableFuture<NeovimWindowApi> getCurrentWindow() {
        return sendWithResponseOfMsgPackType(new RequestMessage.Builder(GET_CURRENT_WIN), Window.class)
                .thenApply(window -> new WindowStreamApi(reactiveRpcStreamer, window));
    }

    @Override
    public CompletableFuture<List<NeovimTabpageApi>> getTabpages() {
        return sendWithResponseOfListOfMsgPackType(new RequestMessage.Builder(LIST_TABPAGES), Tabpage.class)
                .thenApply(tabpages -> tabpages.stream()
                        .map(tabpage -> new TabpageStreamApi(reactiveRpcStreamer, tabpage)).collect(Collectors.toList()));
    }

    @Override
    public CompletableFuture<NeovimTabpageApi> getCurrentTabpage() {
        return sendWithResponseOfMsgPackType(new RequestMessage.Builder(GET_CURRENT_TABPAGE), Tabpage.class)
                .thenApply(tabpage -> new TabpageStreamApi(reactiveRpcStreamer, tabpage));
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

    @Override
    public CompletableFuture<Object> callDictFunction(Map map, String function, List args) {
        return sendWithGenericResponse(
                new RequestMessage.Builder(CALL_DICT_FUNCTION)
                        .addArgument(map)
                        .addArgument(function)
                        .addArgument(args)
        );
    }

    @Override
    public CompletableFuture<Map<String, CommandInfo>> getCommands(GetCommandsOptions getCommandsOptions) {
        return sendWithResponseOfMapType(
                new RequestMessage.Builder(GET_COMMANDS)
                        .addArgument(getCommandsOptions),
                String.class, CommandInfo.class
        );
    }

    @Override
    public CompletableFuture<Void> setClientInfo(String name, ClientVersionInfo clientVersionInfo, ClientType clientType, Map<String, MethodInfo> methods, ClientAttributes clientAttributes) {
        return sendWithNoResponse(
                new RequestMessage.Builder(SET_CLIENT_INFO)
                        .addArgument(name)
                        .addArgument(clientVersionInfo)
                        .addArgument(clientType)
                        .addArgument(methods)
                        .addArgument(clientAttributes)
        );
    }

    @Override
    public CompletableFuture<ChannelInfo> getChannelInfo(int channel) {
        return sendWithResponseOfType(
                new RequestMessage.Builder(GET_CHANNEL_INFO).addArgument(channel),
                ChannelInfo.class
        );
    }

    @Override
    public CompletableFuture<List<ChannelInfo>> getChannels() {
        return sendWithResponseOfListType(
                new RequestMessage.Builder(LIST_CHANNELS),
                ChannelInfo.class
        );
    }

    @Override
    public CompletableFuture<Map> parseExpression(String expression, String flags, boolean highlight) {
        return sendWithResponseOfType(
                new RequestMessage.Builder(PARSE_EXPRESSION)
                        .addArgument(expression)
                        .addArgument(flags)
                        .addArgument(highlight),
                Map.class
        );
    }

    @Override
    public CompletableFuture<List<UiInfo>> getUis() {
        return sendWithResponseOfListType(
                new RequestMessage.Builder(LIST_UIS),
                UiInfo.class
        );
    }

    @Override
    public CompletableFuture<List<Integer>> getProcessChildren() {
        return sendWithResponseOfListType(
                new RequestMessage.Builder(GET_PROC_CHILDREN),
                Integer.class
        );
    }

    @Override
    public CompletableFuture<Object> getProcess() {
        return sendWithGenericResponse(new RequestMessage.Builder(GET_PROC));
    }

    @Override
    public CompletableFuture<Map<String, Integer>> getNamespaces() {
        return sendWithResponseOfMapType(
                new RequestMessage.Builder(GET_NAMESPACES),
                String.class,
                Integer.class
        );
    }

    @Override
    public CompletableFuture<Integer> createNamespace(String name) {
        return sendWithResponseOfType(
                new RequestMessage.Builder(CREATE_NAMESPACES)
                        .addArgument(name),
                Integer.class
        );
    }

    @Override
    public CompletableFuture<Boolean> paste(String data, boolean crlf, int phase) {
        return sendWithResponseOfType(new RequestMessage.Builder(PASTE)
                        .addArgument(data)
                        .addArgument(crlf)
                        .addArgument(phase),
                Boolean.class);
    }

    @Override
    public CompletableFuture<Void> put(List<String> lines, String type, boolean after, boolean follow) {
        return sendWithNoResponse(new RequestMessage.Builder(PUT)
                .addArgument(lines)
                .addArgument(type)
                .addArgument(after)
                .addArgument(follow));
    }

    @Override
    public CompletableFuture<Map<String, Object>> getContext(Map<String, Object> options) {
        return sendWithResponseOfMapType(
                new RequestMessage.Builder(GET_CONTEXT)
                        .addArgument(options),
                String.class,
                Object.class
        );
    }

    @Override
    public CompletableFuture<Void> loadContext(Map<String, Object> contextMap) {
        return sendWithNoResponse(new RequestMessage.Builder(LOAD_CONTEXT).addArgument(contextMap));
    }

    @Override
    public CompletableFuture<Void> selectPopupmenuItem(int item, boolean insert, boolean finish, Map<String, Object> options) {
        return sendWithNoResponse(new RequestMessage.Builder(SELECT_POPUPMENU_ITEM)
                .addArgument(item)
                .addArgument(insert)
                .addArgument(finish)
                .addArgument(options));
    }

    @Override
    public CompletableFuture<Void> setDecorationProvider(int hlId, Map<String, Object> options) {
        return sendWithNoResponse(new RequestMessage.Builder(SET_DECORATION_PROVIDER)
                .addArgument(hlId)
                .addArgument(options));
    }

    @Override
    public CompletableFuture<Void> uiPumSetBounds(int width, int height, int row, int col) {
        return sendWithNoResponse(new RequestMessage.Builder(UI_PUM_SET_BOUNDS)
                .addArgument(width)
                .addArgument(height)
                .addArgument(row)
                .addArgument(col));
    }

    @Override
    public CompletableFuture<Integer> getHlIdByName(String name) {
        return sendWithResponseOfType(new RequestMessage.Builder(GET_HL_ID_BY_NAME)
                .addArgument(name),
                Integer.class);
    }

    @Override
    public CompletableFuture<Void> setHl(int hlId, String name, Map hl) {
        return sendWithNoResponse(new RequestMessage.Builder(SET_HL)
                .addArgument(hlId)
                .addArgument(name)
                .addArgument(hl));
    }

    @Override
    public CompletableFuture<Void> execLua(String code, List<String> arguments) {
        return sendWithNoResponse(new RequestMessage.Builder(EXEC_LUA)
                .addArgument(code)
                .addArgument(arguments));
    }

    @Override
    public CompletableFuture<Void> notifyUser(String message, int logLevel, Map<String, Object> options) {
        return sendWithNoResponse(new RequestMessage.Builder(NOTIFY)
                .addArgument(message)
                .addArgument(logLevel)
                .addArgument(options));
    }

    @Override
    public CompletableFuture<List<String>> getRuntimeFile(String name, boolean all) {
        return sendWithResponseOfListType(new RequestMessage.Builder(GET_RUNTIME_FILE)
                .addArgument(name)
                .addArgument(all),
                String.class);
    }

    @Override
    public CompletableFuture<Map<String, OptionInfo>> getAllOptionsInfo() {
        return sendWithResponseOfMapType(new RequestMessage.Builder(GET_ALL_OPTIONS_INFO),
                String.class,
                OptionInfo.class);
    }

    @Override
    public CompletableFuture<OptionInfo> getOptionInfo() {
        return sendWithResponseOfType(new RequestMessage.Builder(GET_OPTION_INFO), OptionInfo.class);
    }

    @Override
    public CompletableFuture<Void> echo(List<ChunkInfo> chunks, boolean history, Map<String, Object> options) {
        return sendWithNoResponse(new RequestMessage.Builder(ECHO)
                .addArgument(chunks)
                .addArgument(history)
                .addArgument(options));
    }

    @Override
    public CompletableFuture<Integer> openTerm(int buffer, Map<String, Object> options) {
        return sendWithResponseOfType(new RequestMessage.Builder(OPEN_TERM)
                .addArgument(buffer)
                .addArgument(options),
                Integer.class);
    }

    @Override
    public CompletableFuture<Void> chanSend(int channel, String data) {
        return sendWithNoResponse(new RequestMessage.Builder(CHAN_SEND)
                .addArgument(channel)
                .addArgument(data));
    }

    @Override
    public CompletableFuture<String> exec(String vimscriptCode, boolean captureOutput) {
        return sendWithResponseOfType(new RequestMessage.Builder(EXEC)
                .addArgument(vimscriptCode)
                .addArgument(captureOutput),
                String.class);
    }
}
