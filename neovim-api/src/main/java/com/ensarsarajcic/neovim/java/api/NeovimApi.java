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

import java.util.List;
import java.util.Map;

@NeovimApiClient(target = 3)
public interface NeovimApi {
    // TODO add proper return types

    // TODO call atomic will need a prettier interface
    @NeovimApiFunction(name = "nvim_call_atomic", since = 1)
    void callAtomic();

    @NeovimApiFunction(name = "nvim_get_hl_by_id", since = 3)
    void getHighlightById(int id, boolean rgb);

    @NeovimApiFunction(name = "nvim_get_hl_by_name", since = 3)
    void getHighlightByName(String name, boolean rgb);

    @NeovimApiFunction(name = "nvim_ui_attach", since = 1)
    void attachUI(int width, int height, Map<String, String> options);

    @NeovimApiFunction(name = "nvim_ui_detach", since = 1)
    void detachUI();

    @NeovimApiFunction(name = "nvim_ui_try_resize", since = 1)
    void resizeUI(int width, int height);

    @NeovimApiFunction(name = "nvim_execute_lua", since = 3)
    void executeLua(String luaCode, List<String> args);

    @NeovimApiFunction(name = "nvim_command", since = 1)
    void executeCommand(String command);

    @NeovimApiFunction(name = "nvim_set_current_dir", since = 1)
    void setCurrentDir(String directoryPath);

    @NeovimApiFunction(name = "nvim_subscribe", since = 1)
    void subscribeToEvent(String event);

    @NeovimApiFunction(name = "nvim_unsubscribe", since = 1)
    void unsubscribeFromEvent(String event);

    @NeovimApiFunction(name = "nvim_eval", since = 1)
    void eval(String expression);

    @NeovimApiFunction(name = "nvim_call_function", since = 1)
    void callFunction(String name, List<String> args);

    @NeovimApiFunction(name = "nvim_feedkeys", since = 1)
    void feedKeys(String keys, String mode, Boolean escape);

    @NeovimApiFunction(name = "nvim_input", since = 1)
    void input(String keys);

    @NeovimApiFunction(name = "nvim_set_current_line", since = 1)
    void setCurrentLine(String line);

    @NeovimApiFunction(name = "nvim_get_keymap", since = 3)
    void getKeymap(String mode);

    @NeovimApiFunction(name = "nvim_ui_set_option", since = 1)
    void setUiOption(String name, Object value);

    @NeovimApiFunction(name = "nvim_set_var", since = 1)
    void setVariable(String name, Object value);

    @NeovimApiFunction(name = "nvim_get_var", since = 1)
    void getVariable(String name);

    @NeovimApiFunction(name = "nvim_del_var", since = 1)
    void deleteVariable(String name);

    @NeovimApiFunction(name = "nvim_get_vvar", since = 1)
    void getVimVariable(String name);

    @NeovimApiFunction(name = "nvim_set_option", since = 1)
    void setOption(String name, Object value);

    @NeovimApiFunction(name = "nvim_get_option", since = 1)
    void getOption(String name);

    @NeovimApiFunction(name = "nvim_get_color_by_name", since = 1)
    void getColorByName(String name);

    @NeovimApiFunction(name = "nvim_replace_termcodes", since = 1)
    void replaceTermcodes(String strToReplace, boolean fromPart, boolean doLt, boolean special);

    @NeovimApiFunction(name = "nvim_command_output", since = 1)
    void commandOutput(String command);

    @NeovimApiFunction(name = "nvim_out_write", since = 1)
    void writeToOutput(String text);

    @NeovimApiFunction(name = "nvim_err_write", since = 1)
    void writeToError(String text);

    @NeovimApiFunction(name = "nvim_err_writeln", since = 1)
    void writelnToError(String text);

    @NeovimApiFunction(name = "nvim_strwidth", since = 1)
    void stringWidth(String string);

    @NeovimApiFunction(name = "nvim_list_runtime_paths", since = 1)
    void listRuntimePaths();

    @NeovimApiFunction(name = "nvim_get_current_line", since = 1)
    void getCurrentLine();

    @NeovimApiFunction(name = "nvim_del_current_line", since = 1)
    void deleteCurrentLine();

    @NeovimApiFunction(name = "nvim_list_bufs", since = 1)
    void getBuffers();

    @NeovimApiFunction(name = "nvim_get_current_buf", since = 1)
    void getCurrentBuffer();

    // TODO add param when Buffer type is defined
    @NeovimApiFunction(name = "nvim_set_current_buf", since = 1)
    void setCurrentBuffer();

    @NeovimApiFunction(name = "nvim_list_wins", since = 1)
    void getWindows();

    @NeovimApiFunction(name = "nvim_get_current_win", since = 1)
    void getCurrentWindow();

    // TODO add param when Window object is defined
    @NeovimApiFunction(name = "nvim_set_current_win", since = 1)
    void setCurrentWindow();

    @NeovimApiFunction(name = "nvim_list_tabpages", since = 1)
    void getTabpages();

    @NeovimApiFunction(name = "nvim_get_current_tabpage", since = 1)
    void getCurrentTabpage();

    // TODO add param when Tabpage object is defined
    @NeovimApiFunction(name = "nvim_set_current_tabpage", since = 1)
    void setCurrentTabpage();

    @NeovimApiFunction(name = "nvim_get_color_map", since = 1)
    void getColorMap();

    @NeovimApiFunction(name = "nvim_get_mode", since = 2)
    void getMode();

    @NeovimApiFunction(name = "nvim_get_api_info", since = 1)
    void getApiInfo();
}
