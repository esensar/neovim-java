package com.ensarsarajcic.neovim.java.pluginhost;

import com.ensarsarajcic.neovim.java.api.NeovimApi;
import com.ensarsarajcic.neovim.java.api.NeovimStreamApi;
import com.ensarsarajcic.neovim.java.api.types.apiinfo.ApiInfo;
import com.ensarsarajcic.neovim.java.corerpc.client.RpcClient;
import com.ensarsarajcic.neovim.java.corerpc.client.RpcConnection;
import com.ensarsarajcic.neovim.java.corerpc.client.StdIoRpcConnection;
import com.ensarsarajcic.neovim.java.corerpc.reactive.ReactiveRpcClient;
import com.ensarsarajcic.neovim.java.handler.NeovimHandlerManager;
import com.ensarsarajcic.neovim.java.handler.NeovimHandlerProxy;
import com.ensarsarajcic.neovim.java.notifications.NeovimStreamNotificationHandler;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

public final class NeovimJavaPluginHost {

    private final RpcConnection rpcConnection;
    private final NeovimHandlerManager neovimHandlerManager;
    private final NeovimHandlerProxy neovimHandlerProxy;
    private final NeovimApi api;
    private final NeovimStreamNotificationHandler neovimStreamNotificationHandler;
    private final RpcClient client;

    private ApiInfo apiInfo = null;
    private PluginApi pluginApi = null;

    public NeovimJavaPluginHost() {
        this(new NeovimHandlerProxy(Executors.newSingleThreadExecutor()));
    }

    public NeovimJavaPluginHost(NeovimHandlerProxy neovimHandlerProxy) {
        this.rpcConnection = new StdIoRpcConnection();
        this.neovimHandlerProxy = neovimHandlerProxy;
        this.neovimHandlerManager = new NeovimHandlerManager(neovimHandlerProxy);
        client = RpcClient.getDefaultAsyncInstance();
        var reactiveRpcClient = ReactiveRpcClient.createDefaultInstanceWithCustomStreamer(client);
        neovimStreamNotificationHandler = new NeovimStreamNotificationHandler(reactiveRpcClient);
        api = new NeovimStreamApi(reactiveRpcClient);
    }

    public CompletableFuture<Void> start(String[] args) {
        client.attach(rpcConnection);
        return api.getApiInfo().thenAccept(info -> {
            neovimHandlerManager.attachToStream(client);
            this.apiInfo = info;
            this.pluginApi = new PluginApi(api, apiInfo);
        });
    }

    public NeovimHandlerManager getNeovimHandlerManager() {
        return neovimHandlerManager;
    }

    public NeovimApi getApi() {
        return api;
    }

    public ApiInfo getApiInfo() {
        return apiInfo;
    }

    public NeovimStreamNotificationHandler getNeovimStreamNotificationHandler() {
        return neovimStreamNotificationHandler;
    }

    public PluginApi getPluginApi() {
        return pluginApi;
    }
}
