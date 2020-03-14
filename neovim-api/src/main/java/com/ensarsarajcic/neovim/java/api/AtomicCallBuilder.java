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

import com.ensarsarajcic.neovim.java.api.buffer.NeovimBufferApi;
import com.ensarsarajcic.neovim.java.api.tabpage.NeovimTabpageApi;
import com.ensarsarajcic.neovim.java.api.types.api.*;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Buffer;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Tabpage;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Window;
import com.ensarsarajcic.neovim.java.api.types.apiinfo.ApiInfo;
import com.ensarsarajcic.neovim.java.api.window.NeovimWindowApi;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Fake implementation of {@link NeovimApi} which stores calls into an array
 * instead of sending them directly to the neovim instance
 */
public final class AtomicCallBuilder implements NeovimApi {
    @Override
    public CompletableFuture<List> sendAtomic(AtomicCallBuilder atomicCallBuilder) {
        return null;
    }

    @Override
    public AtomicCallBuilder prepareAtomic() {
        return null;
    }

    @Override
    public CompletableFuture<Map> getHighlightById(int id, boolean rgb) {
        return null;
    }

    @Override
    public CompletableFuture<Map> getHighlightByName(String name, boolean rgb) {
        return null;
    }

    @Override
    public CompletableFuture<Void> attachUi(int width, int height, UiOptions options) {
        return null;
    }

    @Override
    public CompletableFuture<Void> detachUi() {
        return null;
    }

    @Override
    public CompletableFuture<Void> resizeUi(int width, int height) {
        return null;
    }

    @Override
    public CompletableFuture<Object> executeLua(String luaCode, List<String> args) {
        return null;
    }

    @Override
    public CompletableFuture<Void> executeCommand(String command) {
        return null;
    }

    @Override
    public CompletableFuture<Void> setCurrentDir(String directoryPath) {
        return null;
    }

    @Override
    public CompletableFuture<Void> subscribeToEvent(String event) {
        return null;
    }

    @Override
    public CompletableFuture<Void> unsubscribeFromEvent(String event) {
        return null;
    }

    @Override
    public CompletableFuture<Object> eval(String expression) {
        return null;
    }

    @Override
    public CompletableFuture<Object> callFunction(String name, List<Object> args) {
        return null;
    }

    @Override
    public CompletableFuture<Void> feedKeys(String keys, String mode, boolean escape) {
        return null;
    }

    @Override
    public CompletableFuture<Integer> input(String keys) {
        return null;
    }

    @Override
    public CompletableFuture<Void> setCurrentLine(String line) {
        return null;
    }

    @Override
    public CompletableFuture<List<VimKeyMap>> getKeymap(String mode) {
        return null;
    }

    @Override
    public CompletableFuture<Void> setUiOption(String name, Object value) {
        return null;
    }

    @Override
    public CompletableFuture<Void> setVariable(String name, Object value) {
        return null;
    }

    @Override
    public CompletableFuture<Object> getVariable(String name) {
        return null;
    }

    @Override
    public CompletableFuture<Void> deleteVariable(String name) {
        return null;
    }

    @Override
    public CompletableFuture<Object> getVimVariable(String name) {
        return null;
    }

    @Override
    public CompletableFuture<Void> setOption(String name, Object value) {
        return null;
    }

    @Override
    public CompletableFuture<Object> getOption(String name) {
        return null;
    }

    @Override
    public CompletableFuture<Integer> getColorByName(String name) {
        return null;
    }

    @Override
    public CompletableFuture<String> replaceTermcodes(String strToReplace, boolean fromPart, boolean doLt, boolean special) {
        return null;
    }

    @Override
    public CompletableFuture<String> commandOutput(String command) {
        return null;
    }

    @Override
    public CompletableFuture<Void> writeToOutput(String text) {
        return null;
    }

    @Override
    public CompletableFuture<Void> writeToError(String text) {
        return null;
    }

    @Override
    public CompletableFuture<Void> writelnToError(String text) {
        return null;
    }

    @Override
    public CompletableFuture<Integer> stringWidth(String string) {
        return null;
    }

    @Override
    public CompletableFuture<List<String>> listRuntimePaths() {
        return null;
    }

    @Override
    public CompletableFuture<String> getCurrentLine() {
        return null;
    }

    @Override
    public CompletableFuture<Void> deleteCurrentLine() {
        return null;
    }

    @Override
    public CompletableFuture<List<NeovimBufferApi>> getBuffers() {
        return null;
    }

    @Override
    public CompletableFuture<NeovimBufferApi> getCurrentBuffer() {
        return null;
    }

    @Override
    public CompletableFuture<Void> setCurrentBuffer(Buffer buffer) {
        return null;
    }

    @Override
    public CompletableFuture<List<NeovimWindowApi>> getWindows() {
        return null;
    }

    @Override
    public CompletableFuture<NeovimWindowApi> getCurrentWindow() {
        return null;
    }

    @Override
    public CompletableFuture<Void> setCurrentWindow(Window window) {
        return null;
    }

    @Override
    public CompletableFuture<List<NeovimTabpageApi>> getTabpages() {
        return null;
    }

    @Override
    public CompletableFuture<NeovimTabpageApi> getCurrentTabpage() {
        return null;
    }

    @Override
    public CompletableFuture<Void> setCurrentTabpage(Tabpage tabpage) {
        return null;
    }

    @Override
    public CompletableFuture<VimColorMap> getColorMap() {
        return null;
    }

    @Override
    public CompletableFuture<VimMode> getMode() {
        return null;
    }

    @Override
    public CompletableFuture<ApiInfo> getApiInfo() {
        return null;
    }

    @Override
    public CompletableFuture<Object> callDictFunction(Map map, String function, List args) {
        return null;
    }

    @Override
    public CompletableFuture<Map<String, CommandInfo>> getCommands(GetCommandsOptions getCommandsOptions) {
        return null;
    }

    @Override
    public CompletableFuture<ChannelInfo> getChannelInfo(int channel) {
        return null;
    }

    @Override
    public CompletableFuture<Void> setClientInfo(String name, ClientVersionInfo clientVersionInfo, ClientType clientType, Map<String, MethodInfo> methods, ClientAttributes clientAttributes) {
        return null;
    }

    @Override
    public CompletableFuture<List<ChannelInfo>> getChannels() {
        return null;
    }

    @Override
    public CompletableFuture<Map> parseExpression(String expression, String flags, boolean highlight) {
        return null;
    }

    @Override
    public CompletableFuture<List<UiInfo>> getUis() {
        return null;
    }

    @Override
    public CompletableFuture<List<Integer>> getProcessChildren() {
        return null;
    }

    @Override
    public CompletableFuture<Object> getProcess() {
        return null;
    }

    @Override
    public CompletableFuture<Map<String, Integer>> getNamespaces() {
        return null;
    }

    @Override
    public CompletableFuture<Integer> createNamespace(String name) {
        return null;
    }

    @Override
    public CompletableFuture<Void> resizeUiGrid(int width, int height) {
        return null;
    }

    @Override
    public CompletableFuture<Void> setPopupmenuHeight(int height) {
        return null;
    }

    @Override
    public CompletableFuture<Void> inputMouse(Mouse.Button button, Mouse.Action action, String modifier, int grid, int row, int col) {
        return null;
    }

    @Override
    public CompletableFuture<Void> setKeymap(String mode, String lhs, String rhs, Map<String, Boolean> options) {
        return null;
    }

    @Override
    public CompletableFuture<Void> deleteKeymap(String mode, String lhs) {
        return null;
    }

    @Override
    public CompletableFuture<Void> setVimVariable(String name, Object value) {
        return null;
    }

    @Override
    public CompletableFuture<NeovimBufferApi> createBuffer(boolean listed, boolean scratch) {
        return null;
    }

    @Override
    public CompletableFuture<NeovimWindowApi> openWindow(Buffer buffer, boolean enter, Map<String, Object> config) {
        return null;
    }

    @Override
    public CompletableFuture<Boolean> paste(String data, boolean crlf, int phase) {
        return null;
    }

    @Override
    public CompletableFuture<Void> put(List<String> lines, String type, boolean after, boolean follow) {
        return null;
    }

    @Override
    public CompletableFuture<Map<String, Object>> getContext(Map<String, Object> options) {
        return null;
    }

    @Override
    public CompletableFuture<Void> loadContext(Map<String, Object> contextMap) {
        return null;
    }

    @Override
    public CompletableFuture<Void> selectPopupmenuItem(int item, boolean insert, boolean finish, Map<String, Object> options) {
        return null;
    }
}
