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

package com.ensarsarajcic.neovim.java.api.atomic;

import com.ensarsarajcic.neovim.java.api.NeovimApi;
import com.ensarsarajcic.neovim.java.api.buffer.NeovimBufferApi;
import com.ensarsarajcic.neovim.java.api.tabpage.NeovimTabpageApi;
import com.ensarsarajcic.neovim.java.api.types.api.*;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Buffer;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Tabpage;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Window;
import com.ensarsarajcic.neovim.java.api.types.apiinfo.ApiInfo;
import com.ensarsarajcic.neovim.java.api.window.NeovimWindowApi;
import com.ensarsarajcic.neovim.java.corerpc.message.RequestMessage;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Fake implementation of {@link NeovimApi} which stores calls into an array
 * instead of sending them directly to the neovim instance
 */
public final class AtomicCallBuilder implements NeovimApi {

    public AtomicCallBuilder() {
    }

    @Override
    public CompletableFuture<AtomicCallResponse> sendAtomic(AtomicCallBuilder atomicCallBuilder) {
        return null;
    }

    @Override
    public AtomicCallBuilder prepareAtomic() {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<Map> getHighlightById(int id, boolean rgb) {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<Map> getHighlightByName(String name, boolean rgb) {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<Void> attachUI(int width, int height, UiOptions options) {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<Void> detachUI() {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<Void> resizeUI(int width, int height) {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<Object> executeLua(String luaCode, List<String> args) {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<Void> executeCommand(String command) {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<Void> setCurrentDir(String directoryPath) {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<Void> subscribeToEvent(String event) {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<Void> unsubscribeFromEvent(String event) {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<Object> eval(String expression) {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<Object> callFunction(String name, List<String> args) {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<Void> feedKeys(String keys, String mode, boolean escape) {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<Integer> input(String keys) {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<Void> setCurrentLine(String line) {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<List<VimKeyMap>> getKeymap(String mode) {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<Void> setUiOption(String name, Object value) {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<Void> setVariable(String name, Object value) {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<Object> getVariable(String name) {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<Void> deleteVariable(String name) {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<Object> getVimVariable(String name) {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<Void> setOption(String name, Object value) {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<Object> getOption(String name) {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<Integer> getColorByName(String name) {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<String> replaceTermcodes(String strToReplace, boolean fromPart, boolean doLt, boolean special) {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<String> commandOutput(String command) {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<Void> writeToOutput(String text) {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<Void> writeToError(String text) {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<Void> writelnToError(String text) {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<Integer> stringWidth(String string) {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<List<String>> listRuntimePaths() {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<String> getCurrentLine() {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<Void> deleteCurrentLine() {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<List<NeovimBufferApi>> getBuffers() {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<NeovimBufferApi> getCurrentBuffer() {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<Void> setCurrentBuffer(Buffer buffer) {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<List<NeovimWindowApi>> getWindows() {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<NeovimWindowApi> getCurrentWindow() {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<Void> setCurrentWindow(Window window) {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<List<NeovimTabpageApi>> getTabpages() {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<NeovimTabpageApi> getCurrentTabpage() {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<Void> setCurrentTabpage(Tabpage tabpage) {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<VimColorMap> getColorMap() {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<VimMode> getMode() {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<ApiInfo> getApiInfo() {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<Object> callDictFunction(Map map, String function, List args) {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<Map<String, CommandInfo>> getCommands(GetCommandsOptions getCommandsOptions) {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<ChannelInfo> getChannelInfo(int channel) {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<Void> setClientInfo(String name, ClientVersionInfo clientVersionInfo, ClientType clientType, Map<String, MethodInfo> methods, ClientAttributes clientAttributes) {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<List<ChannelInfo>> getChannels() {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<Map> parseExpression(String expression, String flags, boolean highlight) {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<List<UiInfo>> getUis() {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<List<Integer>> getProcessChildren() {
        return null;
    }

    @Override
    public AtomicCallBuilderCompletableFuture<Object> getProcess() {
        return null;
    }

    public RequestMessage buildMessage() {
        throw new UnsupportedOperationException();
    }

    public AtomicCallBuilder copy() {
        throw new UnsupportedOperationException();
    }

    public CompletableFuture<AtomicCallResponse> send() {
        throw new UnsupportedOperationException();
    }
}
