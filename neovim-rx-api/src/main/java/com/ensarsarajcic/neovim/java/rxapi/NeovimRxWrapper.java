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

import com.ensarsarajcic.neovim.java.api.AtomicCallBuilder;
import com.ensarsarajcic.neovim.java.api.NeovimApi;
import com.ensarsarajcic.neovim.java.api.types.api.ChannelInfo;
import com.ensarsarajcic.neovim.java.api.types.api.ClientAttributes;
import com.ensarsarajcic.neovim.java.api.types.api.ClientType;
import com.ensarsarajcic.neovim.java.api.types.api.ClientVersionInfo;
import com.ensarsarajcic.neovim.java.api.types.api.CommandInfo;
import com.ensarsarajcic.neovim.java.api.types.api.GetCommandsOptions;
import com.ensarsarajcic.neovim.java.api.types.api.MethodInfo;
import com.ensarsarajcic.neovim.java.api.types.api.Mouse;
import com.ensarsarajcic.neovim.java.api.types.api.UiInfo;
import com.ensarsarajcic.neovim.java.api.types.api.UiOptions;
import com.ensarsarajcic.neovim.java.api.types.api.VimColorMap;
import com.ensarsarajcic.neovim.java.api.types.api.VimKeyMap;
import com.ensarsarajcic.neovim.java.api.types.api.VimMode;
import com.ensarsarajcic.neovim.java.api.types.apiinfo.ApiInfo;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Buffer;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Tabpage;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Window;
import io.reactivex.Completable;
import io.reactivex.Single;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public final class NeovimRxWrapper implements NeovimRxApi {

    private final NeovimApi neovimApi;

    public NeovimRxWrapper(NeovimApi neovimApi) {
        Objects.requireNonNull(neovimApi, "neovimApi is required to wrap it in RX interface");
        this.neovimApi = neovimApi;
    }

    @Override
    public Single<List> sendAtomic(AtomicCallBuilder atomicCallBuilder) {
        return Single.fromFuture(neovimApi.sendAtomic(atomicCallBuilder));
    }

    @Override
    public AtomicCallBuilder prepareAtomic() {
        return neovimApi.prepareAtomic();
    }

    @Override
    public Single<Map> getHighlightById(int id, boolean rgb) {
        return Single.fromFuture(neovimApi.getHighlightById(id, rgb));
    }

    @Override
    public Single<Map> getHighlightByName(String name, boolean rgb) {
        return Single.fromFuture(neovimApi.getHighlightByName(name, rgb));
    }

    @Override
    public Completable attachUi(int width, int height, UiOptions options) {
        return Completable.fromFuture(neovimApi.attachUi(width, height, options));
    }

    @Override
    public Completable detachUi() {
        return Completable.fromFuture(neovimApi.detachUi());
    }

    @Override
    public Completable resizeUi(int width, int height) {
        return Completable.fromFuture(neovimApi.resizeUi(width, height));
    }

    @Override
    public Completable resizeUiGrid(int width, int height) {
        return Completable.fromFuture(neovimApi.resizeUiGrid(width, height));
    }

    @Override
    public Completable setPopupmenuHeight(int height) {
        return Completable.fromFuture(neovimApi.setPopupmenuHeight(height));
    }

    @Override
    public Completable inputMouse(Mouse.Button button, Mouse.Action action, String modifier, int grid, int row, int col) {
        return Completable.fromFuture(neovimApi.inputMouse(button, action, modifier, grid, row, col));
    }

    @Override
    public Single<Object> executeLua(String luaCode, List<String> args) {
        return Single.fromFuture(neovimApi.executeLua(luaCode, args));
    }

    @Override
    public Completable executeCommand(String command) {
        return Completable.fromFuture(neovimApi.executeCommand(command));
    }

    @Override
    public Completable setCurrentDir(String directoryPath) {
        return Completable.fromFuture(neovimApi.setCurrentDir(directoryPath));
    }

    @Override
    public Completable subscribeToEvent(String event) {
        return Completable.fromFuture(neovimApi.subscribeToEvent(event));
    }

    @Override
    public Completable unsubscribeFromEvent(String event) {
        return Completable.fromFuture(neovimApi.unsubscribeFromEvent(event));
    }

    @Override
    public Single<Object> eval(String expression) {
        return Single.fromFuture(neovimApi.eval(expression));
    }

    @Override
    public Single<Object> callFunction(String name, List<Object> args) {
        return Single.fromFuture(neovimApi.callFunction(name, args));
    }

    @Override
    public Completable feedKeys(String keys, String mode, Boolean escape) {
        return Completable.fromFuture(neovimApi.feedKeys(keys, mode, escape));
    }

    @Override
    public Single<Integer> input(String keys) {
        return Single.fromFuture(neovimApi.input(keys));
    }

    @Override
    public Single<List<VimKeyMap>> getKeymap(String mode) {
        return Single.fromFuture(neovimApi.getKeymap(mode));
    }

    @Override
    public Completable setKeymap(String mode, String lhs, String rhs, Map<String, Boolean> options) {
        return Completable.fromFuture(neovimApi.setKeymap(mode, lhs, rhs, options));
    }

    @Override
    public Completable deleteKeymap(String mode, String lhs) {
        return Completable.fromFuture(neovimApi.deleteKeymap(mode, lhs));
    }

    @Override
    public Completable setUiOption(String name, Object value) {
        return Completable.fromFuture(neovimApi.setUiOption(name, value));
    }

    @Override
    public Completable setVariable(String name, Object value) {
        return Completable.fromFuture(neovimApi.setVariable(name, value));
    }

    @Override
    public Single<Object> getVariable(String name) {
        return Single.fromFuture(neovimApi.getVariable(name));
    }

    @Override
    public Completable deleteVariable(String name) {
        return Completable.fromFuture(neovimApi.deleteVariable(name));
    }

    @Override
    public Single<Object> getVimVariable(String name) {
        return Single.fromFuture(neovimApi.getVimVariable(name));
    }

    @Override
    public Completable setVimVariable(String name, Object value) {
        return Completable.fromFuture(neovimApi.setVimVariable(name, value));
    }

    @Override
    public Completable setOption(String name, Object value) {
        return Completable.fromFuture(neovimApi.setOption(name, value));
    }

    @Override
    public Single<Object> getOption(String name) {
        return Single.fromFuture(neovimApi.getOption(name));
    }

    @Override
    public Single<Integer> getColorByName(String name) {
        return Single.fromFuture(neovimApi.getColorByName(name));
    }

    @Override
    public Single<String> replaceTermcodes(String strToReplace, boolean fromPart, boolean doLt, boolean special) {
        return Single.fromFuture(neovimApi.replaceTermcodes(strToReplace, fromPart, doLt, special));
    }

    @Override
    public Single<String> commandOutput(String command) {
        return Single.fromFuture(neovimApi.commandOutput(command));
    }

    @Override
    public Completable writeToOutput(String text) {
        return Completable.fromFuture(neovimApi.writeToOutput(text));
    }

    @Override
    public Completable writeToError(String text) {
        return Completable.fromFuture(neovimApi.writeToError(text));
    }

    @Override
    public Completable writelnToError(String text) {
        return Completable.fromFuture(neovimApi.writelnToError(text));
    }

    @Override
    public Single<Integer> stringWidth(String string) {
        return Single.fromFuture(neovimApi.stringWidth(string));
    }

    @Override
    public Single<List<String>> listRuntimePaths() {
        return Single.fromFuture(neovimApi.listRuntimePaths());
    }

    @Override
    public Single<String> getCurrentLine() {
        return Single.fromFuture(neovimApi.getCurrentLine());
    }

    @Override
    public Completable setCurrentLine(String line) {
        return Completable.fromFuture(neovimApi.setCurrentLine(line));
    }

    @Override
    public Completable deleteCurrentLine() {
        return Completable.fromFuture(neovimApi.deleteCurrentLine());
    }

    @Override
    public Single<List<NeovimBufferRxApi>> getBuffers() {
        return Single.fromFuture(neovimApi.getBuffers())
                .map(neovimBufferApis -> neovimBufferApis.stream()
                        .map(NeovimBufferRxWrapper::new)
                        .collect(Collectors.toList()));
    }

    @Override
    public Single<NeovimBufferRxApi> createBuffer(boolean listed, boolean scratch) {
        return Single.fromFuture(neovimApi.createBuffer(listed, scratch))
                .map(NeovimBufferRxWrapper::new);
    }

    @Override
    public Single<NeovimBufferRxApi> getCurrentBuffer() {
        return Single.fromFuture(neovimApi.getCurrentBuffer())
                .map(NeovimBufferRxWrapper::new);
    }

    @Override
    public Completable setCurrentBuffer(Buffer buffer) {
        return Completable.fromFuture(neovimApi.setCurrentBuffer(buffer));
    }

    @Override
    public Single<List<NeovimWindowRxApi>> getWindows() {
        return Single.fromFuture(neovimApi.getWindows())
                .map(neovimWindowApis -> neovimWindowApis.stream()
                        .map(NeovimWindowRxWrapper::new)
                        .collect(Collectors.toList()));
    }

    @Override
    public Single<NeovimWindowRxApi> openWindow(Buffer buffer, boolean enter, Map<String, Object> config) {
        return Single.fromFuture(neovimApi.openWindow(buffer, enter, config))
                .map(NeovimWindowRxWrapper::new);
    }

    @Override
    public Single<NeovimWindowRxApi> getCurrentWindow() {
        return Single.fromFuture(neovimApi.getCurrentWindow())
                .map(NeovimWindowRxWrapper::new);
    }

    @Override
    public Completable setCurrentWindow(Window window) {
        return Completable.fromFuture(neovimApi.setCurrentWindow(window));
    }

    @Override
    public Single<List<NeovimTabpageRxApi>> getTabpages() {
        return Single.fromFuture(neovimApi.getTabpages())
                .map(neovimTabpageApis -> neovimTabpageApis.stream()
                        .map(NeovimTabpageRxWrapper::new)
                        .collect(Collectors.toList()));
    }

    @Override
    public Single<NeovimTabpageRxApi> getCurrentTabpage() {
        return Single.fromFuture(neovimApi.getCurrentTabpage())
                .map(NeovimTabpageRxWrapper::new);
    }

    @Override
    public Completable setCurrentTabpage(Tabpage tabpage) {
        return Completable.fromFuture(neovimApi.setCurrentTabpage(tabpage));
    }

    @Override
    public Single<VimColorMap> getColorMap() {
        return Single.fromFuture(neovimApi.getColorMap());
    }

    @Override
    public Single<VimMode> getMode() {
        return Single.fromFuture(neovimApi.getMode());
    }

    @Override
    public Single<ApiInfo> getApiInfo() {
        return Single.fromFuture(neovimApi.getApiInfo());
    }

    @Override
    public Single<Object> callDictFunction(Map map, String function, List args) {
        return Single.fromFuture(neovimApi.callDictFunction(map, function, args));
    }

    @Override
    public Single<Map<String, CommandInfo>> getCommands(GetCommandsOptions getCommandsOptions) {
        return Single.fromFuture(neovimApi.getCommands(getCommandsOptions));
    }

    @Override
    public Completable setClientInfo(String name, ClientVersionInfo clientVersionInfo, ClientType clientType, Map<String, MethodInfo> methods, ClientAttributes clientAttributes) {
        return Completable.fromFuture(neovimApi.setClientInfo(name, clientVersionInfo, clientType, methods, clientAttributes));
    }

    @Override
    public Single<ChannelInfo> getChannelInfo(int channel) {
        return Single.fromFuture(neovimApi.getChannelInfo(channel));
    }

    @Override
    public Single<List<ChannelInfo>> getChannels() {
        return Single.fromFuture(neovimApi.getChannels());
    }

    @Override
    public Single<Map> parseExpression(String expression, String flags, boolean highlight) {
        return Single.fromFuture(neovimApi.parseExpression(expression, flags, highlight));
    }

    @Override
    public Single<List<UiInfo>> getUis() {
        return Single.fromFuture(neovimApi.getUis());
    }

    @Override
    public Single<List<Integer>> getProcessChildren() {
        return Single.fromFuture(neovimApi.getProcessChildren());
    }

    @Override
    public Single<Object> getProcess() {
        return Single.fromFuture(neovimApi.getProcess());
    }

    @Override
    public Single<Map<String, Integer>> getNamespaces() {
        return Single.fromFuture(neovimApi.getNamespaces());
    }

    @Override
    public Single<Integer> createNamespace(String name) {
        return Single.fromFuture(neovimApi.createNamespace(name));
    }

    @Override
    public Single<Boolean> paste(String data, boolean crlf, int phase) {
        return Single.fromFuture(neovimApi.paste(data, crlf, phase));
    }

    @Override
    public Completable put(List<String> lines, String type, boolean after, boolean follow) {
        return Completable.fromFuture(neovimApi.put(lines, type, after, follow));
    }

    @Override
    public Single<Map<String, Object>> getContext(Map<String, Object> options) {
        return Single.fromFuture(neovimApi.getContext(options));
    }

    @Override
    public Completable loadContext(Map<String, Object> contextMap) {
        return Completable.fromFuture(neovimApi.loadContext(contextMap));
    }

    @Override
    public Completable selectPopupmenuItem(int item, boolean insert, boolean finish, Map<String, Object> options) {
        return Completable.fromFuture(neovimApi.selectPopupmenuItem(item, insert, finish, options));
    }
}
