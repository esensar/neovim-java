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

import com.ensarsarajcic.neovim.java.api.types.Buffer;
import com.ensarsarajcic.neovim.java.api.types.Tabpage;
import com.ensarsarajcic.neovim.java.api.types.Window;
import com.ensarsarajcic.neovim.java.api.types.apiinfo.ApiInfo;

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
    public CompletableFuture<Void> attachUI(int width, int height, Map<String, String> options) {
        return null;
    }

    @Override
    public CompletableFuture<Void> detachUI() {
        return null;
    }

    @Override
    public CompletableFuture<Void> resizeUI(int width, int height) {
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
    public CompletableFuture<Object> callFunction(String name, List<String> args) {
        return null;
    }

    @Override
    public CompletableFuture<Void> feedKeys(String keys, String mode, Boolean escape) {
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
    public CompletableFuture<List<Map>> getKeymap(String mode) {
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
    public void setOption(String name, Object value) {

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
    public CompletableFuture<String> stringWidth(String string) {
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
    public CompletableFuture<List<Buffer>> getBuffers() {
        return null;
    }

    @Override
    public CompletableFuture<Buffer> getCurrentBuffer() {
        return null;
    }

    @Override
    public CompletableFuture<Void> setCurrentBuffer(Buffer buffer) {
        return null;
    }

    @Override
    public CompletableFuture<List<Window>> getWindows() {
        return null;
    }

    @Override
    public CompletableFuture<Window> getCurrentWindow() {
        return null;
    }

    @Override
    public CompletableFuture<Void> setCurrentWindow(Window window) {
        return null;
    }

    @Override
    public CompletableFuture<List<Tabpage>> getTabpages() {
        return null;
    }

    @Override
    public CompletableFuture<Tabpage> getCurrentTabpage() {
        return null;
    }

    @Override
    public CompletableFuture<Void> setCurrentTabpage(Tabpage tabpage) {
        return null;
    }

    @Override
    public CompletableFuture<Map> getColorMap() {
        return null;
    }

    @Override
    public CompletableFuture<Map> getMode() {
        return null;
    }

    @Override
    public CompletableFuture<ApiInfo> getApiInfo() {
        return null;
    }
}
