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
import com.ensarsarajcic.neovim.java.api.types.api.VimColorMap;
import com.ensarsarajcic.neovim.java.api.types.api.VimKeyMap;
import com.ensarsarajcic.neovim.java.api.types.api.VimMode;
import com.ensarsarajcic.neovim.java.api.types.apiinfo.ApiInfo;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Buffer;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Tabpage;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Window;
import io.reactivex.Single;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public final class NeovimRxWrapper implements NeovimRxApi {

    private NeovimApi neovimApi;

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
    public Single<Void> attachUI(int width, int height, Map<String, String> options) {
        return Single.fromFuture(neovimApi.attachUI(width, height, options));
    }

    @Override
    public Single<Void> detachUI() {
        return Single.fromFuture(neovimApi.detachUI());
    }

    @Override
    public Single<Void> resizeUI(int width, int height) {
        return Single.fromFuture(neovimApi.resizeUI(width, height));
    }

    @Override
    public Single<Object> executeLua(String luaCode, List<String> args) {
        return Single.fromFuture(neovimApi.executeLua(luaCode, args));
    }

    @Override
    public Single<Void> executeCommand(String command) {
        return Single.fromFuture(neovimApi.executeCommand(command));
    }

    @Override
    public Single<Void> setCurrentDir(String directoryPath) {
        return Single.fromFuture(neovimApi.setCurrentDir(directoryPath));
    }

    @Override
    public Single<Void> subscribeToEvent(String event) {
        return Single.fromFuture(neovimApi.subscribeToEvent(event));
    }

    @Override
    public Single<Void> unsubscribeFromEvent(String event) {
        return Single.fromFuture(neovimApi.unsubscribeFromEvent(event));
    }

    @Override
    public Single<Object> eval(String expression) {
        return Single.fromFuture(neovimApi.eval(expression));
    }

    @Override
    public Single<Object> callFunction(String name, List<String> args) {
        return Single.fromFuture(neovimApi.callFunction(name, args));
    }

    @Override
    public Single<Void> feedKeys(String keys, String mode, Boolean escape) {
        return Single.fromFuture(neovimApi.feedKeys(keys, mode, escape));
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
    public Single<Void> setUiOption(String name, Object value) {
        return Single.fromFuture(neovimApi.setUiOption(name, value));
    }

    @Override
    public Single<Void> setVariable(String name, Object value) {
        return Single.fromFuture(neovimApi.setVariable(name, value));
    }

    @Override
    public Single<Object> getVariable(String name) {
        return Single.fromFuture(neovimApi.getVariable(name));
    }

    @Override
    public Single<Void> deleteVariable(String name) {
        return Single.fromFuture(neovimApi.deleteVariable(name));
    }

    @Override
    public Single<Object> getVimVariable(String name) {
        return Single.fromFuture(neovimApi.getVimVariable(name));
    }

    @Override
    public Single<Void> setOption(String name, Object value) {
        return Single.fromFuture(neovimApi.setOption(name, value));
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
    public Single<Void> writeToOutput(String text) {
        return Single.fromFuture(neovimApi.writeToOutput(text));
    }

    @Override
    public Single<Void> writeToError(String text) {
        return Single.fromFuture(neovimApi.writeToError(text));
    }

    @Override
    public Single<Void> writelnToError(String text) {
        return Single.fromFuture(neovimApi.writelnToError(text));
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
    public Single<Void> setCurrentLine(String line) {
        return Single.fromFuture(neovimApi.setCurrentLine(line));
    }

    @Override
    public Single<Void> deleteCurrentLine() {
        return Single.fromFuture(neovimApi.deleteCurrentLine());
    }

    @Override
    public Single<List<NeovimBufferRxApi>> getBuffers() {
        return Single.fromFuture(neovimApi.getBuffers())
                .map(neovimBufferApis -> neovimBufferApis.stream()
                        .map(NeovimBufferRxWrapper::new)
                        .collect(Collectors.toList()));
    }

    @Override
    public Single<NeovimBufferRxApi> getCurrentBuffer() {
        return Single.fromFuture(neovimApi.getCurrentBuffer())
                .map(NeovimBufferRxWrapper::new);
    }

    @Override
    public Single<Void> setCurrentBuffer(Buffer buffer) {
        return Single.fromFuture(neovimApi.setCurrentBuffer(buffer));
    }

    @Override
    public Single<List<NeovimWindowRxApi>> getWindows() {
        return Single.fromFuture(neovimApi.getWindows())
                .map(neovimWindowApis -> neovimWindowApis.stream()
                        .map(NeovimWindowRxWrapper::new)
                        .collect(Collectors.toList()));
    }

    @Override
    public Single<NeovimWindowRxApi> getCurrentWindow() {
        return Single.fromFuture(neovimApi.getCurrentWindow())
                .map(NeovimWindowRxWrapper::new);
    }

    @Override
    public Single<Void> setCurrentWindow(Window window) {
        return Single.fromFuture(neovimApi.setCurrentWindow(window));
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
    public Single<Void> setCurrentTabpage(Tabpage tabpage) {
        return Single.fromFuture(neovimApi.setCurrentTabpage(tabpage));
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
}
