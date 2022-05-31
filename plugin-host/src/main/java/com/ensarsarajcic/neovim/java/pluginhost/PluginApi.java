/*
 * MIT License
 *
 * Copyright (c) 2022 Ensar Sarajčić
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

package com.ensarsarajcic.neovim.java.pluginhost;

import com.ensarsarajcic.neovim.java.api.NeovimApi;
import com.ensarsarajcic.neovim.java.api.types.apiinfo.ApiInfo;
import com.ensarsarajcic.neovim.java.pluginhost.opts.AutocommandOpts;
import com.ensarsarajcic.neovim.java.pluginhost.opts.CommandOpts;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public final class PluginApi {

    private static final String COMMAND_ARGS =
            "{'lineStart': <line1>, 'lineEnd': <line2>, 'range': <range>, 'count': <count>, 'bang': '<bang>', 'mods': '<mods>', 'register': '<reg>', 'args': '<args>'}";
    private static final String AUTOCOMMAND_ARGS =
            "{'file': '<afile>', 'match': <amatch>, 'buffer': <abuf>}";

    public static class CommandAction {
        public static final String CALL = "call %s";
        public static final String ECHO = "echo %s";
        public static final String NOTIFY = "vim.notify(%s)";
    }

    private final NeovimApi api;
    private final ApiInfo apiInfo;

    public PluginApi(NeovimApi api, ApiInfo apiInfo) {
        this.api = api;
        this.apiInfo = apiInfo;
    }

    public CompletableFuture<Void> addRequestCommand(
            String commandName,
            String requestName,
            CommandOpts opts
    ) {
        return addRequestCommand(commandName, requestName, opts, null);
    }

    public CompletableFuture<Void> addRequestCommand(
            String commandName,
            String requestName,
            CommandOpts opts,
            String commandAction
    ) {
        var requestString = String.format(
                "rpcrequest(%d, '%s', %s)",
                apiInfo.getChannelId(),
                requestName,
                COMMAND_ARGS
        );
        String command = String.format(
                Objects.requireNonNullElse(
                        commandAction,
                        CommandAction.CALL
                ),
                requestString
        );
        return api.createUserCommand(
                commandName,
                command,
                opts.toNeovimOptions()
        );
    }

    public CompletableFuture<Void> addNotificationCommand(String commandName, String requestName, CommandOpts opts) {
        return api.createUserCommand(
                commandName,
                String.format(
                        "call rpcnotify(%d, '%s', %s)",
                        apiInfo.getChannelId(),
                        requestName,
                        COMMAND_ARGS
                ),
                opts.toNeovimOptions()
        );
    }

    public CompletableFuture<Integer> addAutocommand(
            List<String> events,
            String requestName,
            AutocommandOpts opts
    ) {
        String command;
        if (opts.sync()) {
            command = String.format(
                    "call rpcrequest(%d, '%s', %s)",
                    apiInfo.getChannelId(),
                    requestName,
                    AUTOCOMMAND_ARGS
            );
        } else {
            command = String.format(
                    "call rpcnotify(%d, '%s', %s)",
                    apiInfo.getChannelId(),
                    requestName,
                    AUTOCOMMAND_ARGS
            );
        }
        Map<String, Object> options = opts.toNeovimOptions();
        options.put("command", command);
        return api.createAutocommand(
                events,
                options
        );
    }
}
