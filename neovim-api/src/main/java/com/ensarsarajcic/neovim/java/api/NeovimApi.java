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

public interface NeovimApi {

    @NeovimApiFunction(name = "nvim_call_atomic", since = 1)
    CompletableFuture<List> sendAtomic(AtomicCallBuilder atomicCallBuilder);
    AtomicCallBuilder prepareAtomic();

    @NeovimApiFunction(name = "nvim_get_hl_by_id", since = 3)
    CompletableFuture<Map> getHighlightById(int id, boolean rgb);

    @NeovimApiFunction(name = "nvim_get_hl_by_name", since = 3)
    CompletableFuture<Map> getHighlightByName(String name, boolean rgb);

    @NeovimApiFunction(name = "nvim_ui_attach", since = 1)
    CompletableFuture<Void> attachUI(int width, int height, Map<String, String> options);

    @NeovimApiFunction(name = "nvim_ui_detach", since = 1)
    CompletableFuture<Void> detachUI();

    @NeovimApiFunction(name = "nvim_ui_try_resize", since = 1)
    CompletableFuture<Void> resizeUI(int width, int height);

    @NeovimApiFunction(name = "nvim_execute_lua", since = 3)
    CompletableFuture<Object> executeLua(String luaCode, List<String> args);

    @NeovimApiFunction(name = "nvim_command", since = 1)
    CompletableFuture<Void> executeCommand(String command);

    @NeovimApiFunction(name = "nvim_set_current_dir", since = 1)
    CompletableFuture<Void> setCurrentDir(String directoryPath);

    @NeovimApiFunction(name = "nvim_subscribe", since = 1)
    CompletableFuture<Void> subscribeToEvent(String event);

    @NeovimApiFunction(name = "nvim_unsubscribe", since = 1)
    CompletableFuture<Void> unsubscribeFromEvent(String event);

    @NeovimApiFunction(name = "nvim_eval", since = 1)
    CompletableFuture<Object> eval(String expression);

    @NeovimApiFunction(name = "nvim_call_function", since = 1)
    CompletableFuture<Object> callFunction(String name, List<String> args);

    @NeovimApiFunction(name = "nvim_feedkeys", since = 1)
    CompletableFuture<Void> feedKeys(String keys, String mode, Boolean escape);

    @NeovimApiFunction(name = "nvim_input", since = 1)
    CompletableFuture<Integer> input(String keys);

    @NeovimApiFunction(name = "nvim_set_current_line", since = 1)
    CompletableFuture<Void> setCurrentLine(String line);

    @NeovimApiFunction(name = "nvim_get_keymap", since = 3)
    CompletableFuture<List<Map>> getKeymap(String mode);

    @NeovimApiFunction(name = "nvim_ui_set_option", since = 1)
    CompletableFuture<Void> setUiOption(String name, Object value);

    @NeovimApiFunction(name = "nvim_set_var", since = 1)
    CompletableFuture<Void> setVariable(String name, Object value);

    @NeovimApiFunction(name = "nvim_get_var", since = 1)
    CompletableFuture<Object> getVariable(String name);

    @NeovimApiFunction(name = "nvim_del_var", since = 1)
    CompletableFuture<Void> deleteVariable(String name);

    @NeovimApiFunction(name = "nvim_get_vvar", since = 1)
    CompletableFuture<Object> getVimVariable(String name);

    @NeovimApiFunction(name = "nvim_set_option", since = 1)
    void setOption(String name, Object value);

    @NeovimApiFunction(name = "nvim_get_option", since = 1)
    CompletableFuture<Object> getOption(String name);

    @NeovimApiFunction(name = "nvim_get_color_by_name", since = 1)
    CompletableFuture<Integer> getColorByName(String name);

    @NeovimApiFunction(name = "nvim_replace_termcodes", since = 1)
    CompletableFuture<String> replaceTermcodes(String strToReplace, boolean fromPart, boolean doLt, boolean special);

    @NeovimApiFunction(name = "nvim_command_output", since = 1)
    CompletableFuture<String> commandOutput(String command);

    @NeovimApiFunction(name = "nvim_out_write", since = 1)
    CompletableFuture<Void> writeToOutput(String text);

    @NeovimApiFunction(name = "nvim_err_write", since = 1)
    CompletableFuture<Void> writeToError(String text);

    @NeovimApiFunction(name = "nvim_err_writeln", since = 1)
    CompletableFuture<Void> writelnToError(String text);

    @NeovimApiFunction(name = "nvim_strwidth", since = 1)
    CompletableFuture<String> stringWidth(String string);

    @NeovimApiFunction(name = "nvim_list_runtime_paths", since = 1)
    CompletableFuture<List<String>> listRuntimePaths();

    @NeovimApiFunction(name = "nvim_get_current_line", since = 1)
    CompletableFuture<String> getCurrentLine();

    @NeovimApiFunction(name = "nvim_del_current_line", since = 1)
    CompletableFuture<Void> deleteCurrentLine();

    @NeovimApiFunction(name = "nvim_list_bufs", since = 1)
    CompletableFuture<List<Buffer>> getBuffers();

    @NeovimApiFunction(name = "nvim_get_current_buf", since = 1)
    CompletableFuture<Buffer> getCurrentBuffer();

    // Todo consider setAsCurrent method on buffer
    @NeovimApiFunction(name = "nvim_set_current_buf", since = 1)
    CompletableFuture<Void> setCurrentBuffer(Buffer buffer);

    @NeovimApiFunction(name = "nvim_list_wins", since = 1)
    CompletableFuture<List<Window>> getWindows();

    @NeovimApiFunction(name = "nvim_get_current_win", since = 1)
    CompletableFuture<Window> getCurrentWindow();

    // Todo consider setAsCurrent method on window
    @NeovimApiFunction(name = "nvim_set_current_win", since = 1)
    CompletableFuture<Void> setCurrentWindow(Window window);

    @NeovimApiFunction(name = "nvim_list_tabpages", since = 1)
    CompletableFuture<List<Tabpage>> getTabpages();

    @NeovimApiFunction(name = "nvim_get_current_tabpage", since = 1)
    CompletableFuture<Tabpage> getCurrentTabpage();

    // Todo consider setAsCurrent method on tabpage
    @NeovimApiFunction(name = "nvim_set_current_tabpage", since = 1)
    CompletableFuture<Void> setCurrentTabpage(Tabpage tabpage);

    @NeovimApiFunction(name = "nvim_get_color_map", since = 1)
    CompletableFuture<Map> getColorMap();

    @NeovimApiFunction(name = "nvim_get_mode", since = 2)
    CompletableFuture<Map> getMode();

    @NeovimApiFunction(name = "nvim_get_api_info", since = 1)
    CompletableFuture<ApiInfo> getApiInfo();
}
