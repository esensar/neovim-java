package com.ensarsarajcic.neovim.java.pluginhost;

import com.ensarsarajcic.neovim.java.api.NeovimApi;
import com.ensarsarajcic.neovim.java.api.types.apiinfo.ApiInfo;
import com.ensarsarajcic.neovim.java.handler.NeovimHandlerProxy;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public final class PluginApi {

    private final NeovimApi api;
    private final ApiInfo apiInfo;

    public PluginApi(NeovimApi api, ApiInfo apiInfo) {
        this.api = api;
        this.apiInfo = apiInfo;
    }

    public CompletableFuture<Void> addRequestCommand(
            String commandName,
            String requestName,
            boolean echo,
            String description
    ) {
        var options = new HashMap<String, Object>();
        options.put("desc", description);
        var command = echo ? "echo " : "";
        return api.createUserCommand(
                commandName,
                String.format("%srpcrequest(%d, '%s')", command, apiInfo.getChannelId(), requestName),
                options
        );
    }

    public CompletableFuture<Void> addNotificationCommand(String commandName, String requestName, String description) {
        var options = new HashMap<String, Object>();
        options.put("desc", description);
        return api.createUserCommand(
                commandName,
                String.format("rpcnotify(%d, '%s')", apiInfo.getChannelId(), requestName),
                options
        );
    }
}
