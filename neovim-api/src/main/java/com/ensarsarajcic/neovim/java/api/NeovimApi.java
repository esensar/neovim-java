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
import com.ensarsarajcic.neovim.java.api.buffer.NeovimBufferApi;
import com.ensarsarajcic.neovim.java.api.tabpage.NeovimTabpageApi;
import com.ensarsarajcic.neovim.java.api.types.api.ChannelInfo;
import com.ensarsarajcic.neovim.java.api.types.api.ClientAttributes;
import com.ensarsarajcic.neovim.java.api.types.api.ClientType;
import com.ensarsarajcic.neovim.java.api.types.api.ClientVersionInfo;
import com.ensarsarajcic.neovim.java.api.types.api.CommandInfo;
import com.ensarsarajcic.neovim.java.api.types.api.EvalStatuslineOptions;
import com.ensarsarajcic.neovim.java.api.types.api.EvalStatuslineResult;
import com.ensarsarajcic.neovim.java.api.types.api.GetCommandsOptions;
import com.ensarsarajcic.neovim.java.api.types.api.HighlightedText;
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
import com.ensarsarajcic.neovim.java.corerpc.message.RequestMessage;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Represents full neovim API
 */
public interface NeovimApi {

    // region Supported functions names
    String CALL_ATOMIC = "nvim_call_atomic";
    String GET_HIGHLIGHT_BY_ID = "nvim_get_hl_by_id";
    String GET_HIGHLIGHT_BY_NAME = "nvim_get_hl_by_name";
    String ATTACH_UI = "nvim_ui_attach";
    String DETACH_UI = "nvim_ui_detach";
    String RESIZE_UI = "nvim_ui_try_resize";
    String RESIZE_UI_GRID = "nvim_ui_try_resize_grid";
    String SET_POPUPMENU_HEIGHT = "nvim_ui_pum_set_height";
    String INPUT_MOUSE = "nvim_input_mouse";
    String EXECUTE_LUA = "nvim_execute_lua";
    String EXECUTE_COMMAND = "nvim_command";
    String SET_CURRENT_DIR = "nvim_set_current_dir";
    String SUBSCRIBE_TO_EVENT = "nvim_subscribe";
    String UNSUBSCRIBE_FROM_EVENT = "nvim_unsubscribe";
    String EVAL = "nvim_eval";
    String CALL_FUNCTION = "nvim_call_function";
    String FEEDKEYS = "nvim_feedkeys";
    String INPUT = "nvim_input";
    String GET_KEYMAP = "nvim_get_keymap";
    String SET_KEYMAP = "nvim_set_keymap";
    String DEL_KEYMAP = "nvim_del_keymap";
    String SET_UI_OPTION = "nvim_ui_set_option";
    String SET_VAR = "nvim_set_var";
    String GET_VAR = "nvim_get_var";
    String DEL_VAR = "nvim_del_var";
    String SET_VIM_VARIABLE = "nvim_set_vvar";
    String GET_VIM_VARIABLE = "nvim_get_vvar";
    String SET_OPTION = "nvim_set_option";
    String GET_OPTION = "nvim_get_option";
    String GET_COLOR_BY_NAME = "nvim_get_color_by_name";
    String REPLACE_TERMCODES = "nvim_replace_termcodes";
    String COMMAND_OUTPUT = "nvim_command_output";
    String OUT_WRITE = "nvim_out_write";
    String ERR_WRITE = "nvim_err_write";
    String ERR_WRITELN = "nvim_err_writeln";
    String STRWIDTH = "nvim_strwidth";
    String LIST_RUNTIME_PATHS = "nvim_list_runtime_paths";
    String GET_CURRENT_LINE = "nvim_get_current_line";
    String SET_CURRENT_LINE = "nvim_set_current_line";
    String DEL_CURRENT_LINE = "nvim_del_current_line";
    String LIST_BUFS = "nvim_list_bufs";
    String CREATE_BUF = "nvim_create_buf";
    String GET_CURRENT_BUF = "nvim_get_current_buf";
    String SET_CURRENT_BUF = "nvim_set_current_buf";
    String LIST_WINS = "nvim_list_wins";
    String OPEN_WIN = "nvim_open_win";
    String GET_CURRENT_WIN = "nvim_get_current_win";
    String SET_CURRENT_WIN = "nvim_set_current_win";
    String LIST_TABPAGES = "nvim_list_tabpages";
    String GET_CURRENT_TABPAGE = "nvim_get_current_tabpage";
    String SET_CURRENT_TABPAGE = "nvim_set_current_tabpage";
    String GET_COLOR_MAP = "nvim_get_color_map";
    String GET_MODE = "nvim_get_mode";
    String GET_API_INFO = "nvim_get_api_info";
    String CALL_DICT_FUNCTION = "nvim_call_dict_function";
    String GET_COMMANDS = "nvim_get_commands";
    String SET_CLIENT_INFO = "nvim_set_client_info";
    String GET_CHANNEL_INFO = "nvim_get_chan_info";
    String LIST_CHANNELS = "nvim_list_chans";
    String PARSE_EXPRESSION = "nvim_parse_expression";
    String LIST_UIS = "nvim_list_uis";
    String GET_PROC_CHILDREN = "nvim_get_proc_children";
    String GET_PROC = "nvim_get_proc";
    String GET_NAMESPACES = "nvim_get_namespaces";
    String CREATE_NAMESPACES = "nvim_create_namespaces";
    String PASTE = "nvim_paste";
    String PUT = "nvim_put";
    String GET_CONTEXT = "nvim_get_context";
    String LOAD_CONTEXT = "nvim_load_context";
    String SELECT_POPUPMENU_ITEM = "nvim_select_popupmenu_item";

    String SET_DECORATION_PROVIDER = "nvim_set_decoration_provider";
    String UI_PUM_SET_BOUNDS = "nvim_ui_pum_set_bounds";
    String GET_HL_ID_BY_NAME = "nvim_get_hl_id_by_name";
    String SET_HL = "nvim_set_hl";
    String EXEC_LUA = "nvim_exec_lua";
    String NOTIFY = "nvim_notify";
    String GET_RUNTIME_FILE = "nvim_get_runtime_file";
    String GET_ALL_OPTIONS_INFO = "nvim_get_all_options_info";
    String GET_OPTION_INFO = "nvim_get_option_info";
    String ECHO = "nvim_echo";
    String OPEN_TERM = "nvim_open_term";
    String CHAN_SEND = "nvim_chan_send";
    String EXEC = "nvim_exec";
    String DEL_MARK = "nvim_del_mark";
    String GET_MARK = "nvim_get_mark";
    String EVAL_STATUSLINE = "nvim_eval_statusline";
    String GET_AUTOCMDS = "nvim_get_autocmds";
    String CREATE_AUTOCMD = "nvim_create_autocmd";
    String DEL_AUTOCMD = "nvim_del_autocmd";
    String CLEAR_AUTOCMDS = "nvim_clear_autocmds";
    String CREATE_AUGROUP = "nvim_create_augroup";
    String DEL_AUGROUP_BY_ID = "nvim_del_augroup_by_id";
    String DEL_AUGROUP_BY_NAME = "nvim_del_augroup_by_name";
    String EXEC_AUTOCMDS = "nvim_exec_autocmds";
    String GET_OPTION_VALUE = "nvim_get_option_value";
    String SET_OPTION_VALUE = "nvim_set_option_value";
    String CREATE_USER_COMMAND = "nvim_create_user_command";
    String DEL_USER_COMMAND = "nvim_del_user_command";
    // endregion

    @NeovimApiFunction(name = CALL_ATOMIC, since = 1)
    CompletableFuture<AtomicCallResponse> sendAtomic(List<RequestMessage> requestMessages);

    @NeovimApiFunction(name = GET_HIGHLIGHT_BY_ID, since = 3)
    CompletableFuture<Map> getHighlightById(int id, boolean rgb);

    @NeovimApiFunction(name = GET_HIGHLIGHT_BY_NAME, since = 3)
    CompletableFuture<Map> getHighlightByName(String name, boolean rgb);

    @NeovimApiFunction(name = ATTACH_UI, since = 1)
    CompletableFuture<Void> attachUi(int width, int height, UiOptions options);

    @NeovimApiFunction(name = DETACH_UI, since = 1)
    CompletableFuture<Void> detachUi();

    @NeovimApiFunction(name = RESIZE_UI, since = 1)
    CompletableFuture<Void> resizeUi(int width, int height);

    @NeovimApiFunction(name = RESIZE_UI_GRID, since = 6)
    CompletableFuture<Void> resizeUiGrid(int width, int height);

    @NeovimApiFunction(name = SET_POPUPMENU_HEIGHT, since = 6)
    CompletableFuture<Void> setPopupmenuHeight(int height);

    @NeovimApiFunction(name = INPUT_MOUSE, since = 6)
    CompletableFuture<Void> inputMouse(Mouse.Button button, Mouse.Action action, String modifier, int grid, int row, int col);

    @NeovimApiFunction(name = EXECUTE_LUA, since = 3)
    CompletableFuture<Object> executeLua(String luaCode, List<String> args);

    @NeovimApiFunction(name = EXECUTE_COMMAND, since = 1)
    CompletableFuture<Void> executeCommand(String command);

    @NeovimApiFunction(name = SET_CURRENT_DIR, since = 1)
    CompletableFuture<Void> setCurrentDir(String directoryPath);

    @NeovimApiFunction(name = SUBSCRIBE_TO_EVENT, since = 1)
    CompletableFuture<Void> subscribeToEvent(String event);

    @NeovimApiFunction(name = UNSUBSCRIBE_FROM_EVENT, since = 1)
    CompletableFuture<Void> unsubscribeFromEvent(String event);

    @NeovimApiFunction(name = EVAL, since = 1)
    CompletableFuture<Object> eval(String expression);

    @NeovimApiFunction(name = CALL_FUNCTION, since = 1)
    CompletableFuture<Object> callFunction(String name, List<Object> args);

    @NeovimApiFunction(name = FEEDKEYS, since = 1)
    CompletableFuture<Void> feedKeys(String keys, String mode, boolean escape);

    @NeovimApiFunction(name = INPUT, since = 1)
    CompletableFuture<Integer> input(String keys);

    @NeovimApiFunction(name = GET_KEYMAP, since = 3)
    CompletableFuture<List<VimKeyMap>> getKeymap(String mode);

    @NeovimApiFunction(name = SET_KEYMAP, since = 6)
    CompletableFuture<Void> setKeymap(String mode, String lhs, String rhs, Map<String, Boolean> options);

    @NeovimApiFunction(name = DEL_KEYMAP, since = 6)
    CompletableFuture<Void> deleteKeymap(String mode, String lhs);

    @NeovimApiFunction(name = SET_UI_OPTION, since = 1)
    CompletableFuture<Void> setUiOption(String name, Object value);

    @NeovimApiFunction(name = SET_VAR, since = 1)
    CompletableFuture<Void> setVariable(String name, Object value);

    @NeovimApiFunction(name = GET_VAR, since = 1)
    CompletableFuture<Object> getVariable(String name);

    @NeovimApiFunction(name = DEL_VAR, since = 1)
    CompletableFuture<Void> deleteVariable(String name);

    @NeovimApiFunction(name = GET_VIM_VARIABLE, since = 1)
    CompletableFuture<Object> getVimVariable(String name);

    @NeovimApiFunction(name = SET_VIM_VARIABLE, since = 6)
    CompletableFuture<Void> setVimVariable(String name, Object value);

    @NeovimApiFunction(name = SET_OPTION, since = 1)
    CompletableFuture<Void> setOption(String name, Object value);

    @NeovimApiFunction(name = GET_OPTION, since = 1)
    CompletableFuture<Object> getOption(String name);

    @NeovimApiFunction(name = GET_COLOR_BY_NAME, since = 1)
    CompletableFuture<Integer> getColorByName(String name);

    @NeovimApiFunction(name = REPLACE_TERMCODES, since = 1)
    CompletableFuture<String> replaceTermcodes(String strToReplace, boolean fromPart, boolean doLt, boolean special);

    @NeovimApiFunction(name = COMMAND_OUTPUT, since = 1)
    CompletableFuture<String> commandOutput(String command);

    @NeovimApiFunction(name = OUT_WRITE, since = 1)
    CompletableFuture<Void> writeToOutput(String text);

    @NeovimApiFunction(name = ERR_WRITE, since = 1)
    CompletableFuture<Void> writeToError(String text);

    @NeovimApiFunction(name = ERR_WRITELN, since = 1)
    CompletableFuture<Void> writelnToError(String text);

    @NeovimApiFunction(name = STRWIDTH, since = 1)
    CompletableFuture<Integer> stringWidth(String string);

    @NeovimApiFunction(name = LIST_RUNTIME_PATHS, since = 1)
    CompletableFuture<List<String>> listRuntimePaths();

    @NeovimApiFunction(name = GET_CURRENT_LINE, since = 1)
    CompletableFuture<String> getCurrentLine();

    @NeovimApiFunction(name = SET_CURRENT_LINE, since = 1)
    CompletableFuture<Void> setCurrentLine(String line);

    @NeovimApiFunction(name = DEL_CURRENT_LINE, since = 1)
    CompletableFuture<Void> deleteCurrentLine();

    @NeovimApiFunction(name = LIST_BUFS, since = 1)
    CompletableFuture<List<NeovimBufferApi>> getBuffers();

    @NeovimApiFunction(name = CREATE_BUF, since = 6)
    CompletableFuture<NeovimBufferApi> createBuffer(boolean listed, boolean scratch);

    @NeovimApiFunction(name = GET_CURRENT_BUF, since = 1)
    CompletableFuture<NeovimBufferApi> getCurrentBuffer();

    // Todo consider setAsCurrent method on buffer
    @NeovimApiFunction(name = SET_CURRENT_BUF, since = 1)
    CompletableFuture<Void> setCurrentBuffer(Buffer buffer);

    @NeovimApiFunction(name = LIST_WINS, since = 1)
    CompletableFuture<List<NeovimWindowApi>> getWindows();

    @NeovimApiFunction(name = OPEN_WIN, since = 6)
    CompletableFuture<NeovimWindowApi> openWindow(Buffer buffer, boolean enter, Map<String, Object> config);

    @NeovimApiFunction(name = GET_CURRENT_WIN, since = 1)
    CompletableFuture<NeovimWindowApi> getCurrentWindow();

    // Todo consider setAsCurrent method on window
    @NeovimApiFunction(name = SET_CURRENT_WIN, since = 1)
    CompletableFuture<Void> setCurrentWindow(Window window);

    @NeovimApiFunction(name = LIST_TABPAGES, since = 1)
    CompletableFuture<List<NeovimTabpageApi>> getTabpages();

    @NeovimApiFunction(name = GET_CURRENT_TABPAGE, since = 1)
    CompletableFuture<NeovimTabpageApi> getCurrentTabpage();

    // Todo consider setAsCurrent method on tabpage
    @NeovimApiFunction(name = SET_CURRENT_TABPAGE, since = 1)
    CompletableFuture<Void> setCurrentTabpage(Tabpage tabpage);

    @NeovimApiFunction(name = GET_COLOR_MAP, since = 1)
    CompletableFuture<VimColorMap> getColorMap();

    @NeovimApiFunction(name = GET_MODE, since = 2)
    CompletableFuture<VimMode> getMode();

    @NeovimApiFunction(name = GET_API_INFO, since = 1)
    CompletableFuture<ApiInfo> getApiInfo();

    @NeovimApiFunction(name = CALL_DICT_FUNCTION, since = 4)
    CompletableFuture<Object> callDictFunction(Map map, String function, List args);

    @NeovimApiFunction(name = GET_COMMANDS, since = 4)
    CompletableFuture<Map<String, CommandInfo>> getCommands(GetCommandsOptions getCommandsOptions);

    @NeovimApiFunction(name = SET_CLIENT_INFO, since = 4)
    CompletableFuture<Void> setClientInfo(String name, ClientVersionInfo clientVersionInfo, ClientType clientType, Map<String, MethodInfo> methods, ClientAttributes clientAttributes);

    @NeovimApiFunction(name = GET_CHANNEL_INFO, since = 4)
    CompletableFuture<ChannelInfo> getChannelInfo(int channel);

    @NeovimApiFunction(name = LIST_CHANNELS, since = 4)
    CompletableFuture<List<ChannelInfo>> getChannels();

    @NeovimApiFunction(name = PARSE_EXPRESSION, since = 4)
    CompletableFuture<Map> parseExpression(String expression, String flags, boolean highlight);

    @NeovimApiFunction(name = LIST_UIS, since = 4)
    CompletableFuture<List<UiInfo>> getUis();

    @NeovimApiFunction(name = GET_PROC_CHILDREN, since = 4)
    CompletableFuture<List<Integer>> getProcessChildren();

    @NeovimApiFunction(name = GET_PROC, since = 4)
    CompletableFuture<Object> getProcess();

    @NeovimApiFunction(name = GET_NAMESPACES, since = 5)
    CompletableFuture<Map<String, Integer>> getNamespaces();

    @NeovimApiFunction(name = CREATE_NAMESPACES, since = 5)
    CompletableFuture<Integer> createNamespace(String name);

    @NeovimApiFunction(name = PASTE, since = 6)
    CompletableFuture<Boolean> paste(String data, boolean crlf, int phase);

    @NeovimApiFunction(name = PUT, since = 6)
    CompletableFuture<Void> put(List<String> lines, String type, boolean after, boolean follow);

    @NeovimApiFunction(name = GET_CONTEXT, since = 6)
    CompletableFuture<Map<String, Object>> getContext(Map<String, Object> options);

    @NeovimApiFunction(name = LOAD_CONTEXT, since = 6)
    CompletableFuture<Void> loadContext(Map<String, Object> contextMap);

    @NeovimApiFunction(name = SELECT_POPUPMENU_ITEM, since = 6)
    CompletableFuture<Void> selectPopupmenuItem(int item, boolean insert, boolean finish, Map<String, Object> options);

    @NeovimApiFunction(name = SET_DECORATION_PROVIDER, since = 7)
    CompletableFuture<Void> setDecorationProvider(int hlId, Map<String, Object> options);

    @NeovimApiFunction(name = UI_PUM_SET_BOUNDS, since = 7)
    CompletableFuture<Void> uiPumSetBounds(int width, int height, int row, int col);

    @NeovimApiFunction(name = GET_HL_ID_BY_NAME, since = 7)
    CompletableFuture<Integer> getHlIdByName(String name);

    @NeovimApiFunction(name = SET_HL, since = 7)
    CompletableFuture<Void> setHl(int hlId, String name, Map hl);

    @NeovimApiFunction(name = EXEC_LUA, since = 7)
    CompletableFuture<Void> execLua(String code, List<String> arguments);

    @NeovimApiFunction(name = NOTIFY, since = 7)
    CompletableFuture<Void> notifyUser(String message, int logLevel, Map<String, Object> options);

    @NeovimApiFunction(name = GET_RUNTIME_FILE, since = 7)
    CompletableFuture<List<String>> getRuntimeFile(String name, boolean all);

    @NeovimApiFunction(name = GET_ALL_OPTIONS_INFO, since = 7)
    CompletableFuture<Map<String, OptionInfo>> getAllOptionsInfo();

    @NeovimApiFunction(name = GET_OPTION_INFO, since = 7)
    CompletableFuture<OptionInfo> getOptionInfo();

    @NeovimApiFunction(name = ECHO, since = 7)
    CompletableFuture<Void> echo(List<HighlightedText> chunks, boolean history, Map<String, Object> options);

    @NeovimApiFunction(name = OPEN_TERM, since = 7)
    CompletableFuture<Integer> openTerm(int buffer, Map<String, Object> options);

    @NeovimApiFunction(name = CHAN_SEND, since = 7)
    CompletableFuture<Void> chanSend(int channel, String data);

    @NeovimApiFunction(name = EXEC, since = 7)
    CompletableFuture<String> exec(String vimscriptCode, boolean captureOutput);

    @NeovimApiFunction(name = DEL_MARK, since = 8)
    CompletableFuture<Boolean> deleteMark(String name);

    @NeovimApiFunction(name = GET_MARK, since = 8)
    CompletableFuture<Boolean> getMark(String name, Map<String, Object> options);

    @NeovimApiFunction(name = EVAL_STATUSLINE, since = 8)
    CompletableFuture<EvalStatuslineResult> evalStatusline(String statuslineString, EvalStatuslineOptions options);

    @NeovimApiFunction(name = GET_AUTOCMDS, since = 9)
    CompletableFuture<List<Map>> getAutocommands(Map<String, Object> options);

    @NeovimApiFunction(name = CREATE_AUTOCMD, since = 9)
    CompletableFuture<Integer> createAutocommand(List<String> events, Map<String, Object> options);

    @NeovimApiFunction(name = DEL_AUTOCMD, since = 9)
    CompletableFuture<Void> deleteAutocommand(int autocommandId);

    @NeovimApiFunction(name = CLEAR_AUTOCMDS, since = 9)
    CompletableFuture<Void> clearAutocommands(Map<String, Object> options);

    @NeovimApiFunction(name = CREATE_AUGROUP, since = 9)
    CompletableFuture<Integer> createAugroup(String name, Map<String, Object> options);

    @NeovimApiFunction(name = DEL_AUGROUP_BY_ID, since = 9)
    CompletableFuture<Void> deleteAugroupById(int id);

    @NeovimApiFunction(name = DEL_AUGROUP_BY_NAME, since = 9)
    CompletableFuture<Void> deleteAugroupByName(String name);

    @NeovimApiFunction(name = EXEC_AUTOCMDS, since = 9)
    CompletableFuture<Void> executeAutocommands(List<String> event, Map<String, Object> options);

    @NeovimApiFunction(name = GET_OPTION_VALUE, since = 9)
    CompletableFuture<Object> getOptionValue(String name, Map<String, Object> options);

    @NeovimApiFunction(name = SET_OPTION_VALUE, since = 9)
    CompletableFuture<Void> setOptionValue(String name, Object value, Map<String, Object> options);

    @NeovimApiFunction(name = CREATE_USER_COMMAND, since = 9)
    CompletableFuture<Void> createUserCommand(String name, String command, Map<String, Object> options);

    @NeovimApiFunction(name = DEL_USER_COMMAND, since = 9)
    CompletableFuture<Void> deleteUserCommand(String name);
}
