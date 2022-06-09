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
import com.ensarsarajcic.neovim.java.api.NeovimApis;
import com.ensarsarajcic.neovim.java.api.NeovimStreamApi;
import com.ensarsarajcic.neovim.java.api.types.apiinfo.ApiInfo;
import com.ensarsarajcic.neovim.java.api.util.ObjectMappers;
import com.ensarsarajcic.neovim.java.corerpc.client.RpcConnection;
import com.ensarsarajcic.neovim.java.corerpc.client.RpcStreamer;
import com.ensarsarajcic.neovim.java.corerpc.client.StdIoRpcConnection;
import com.ensarsarajcic.neovim.java.corerpc.reactive.ReactiveRpcClient;
import com.ensarsarajcic.neovim.java.corerpc.reactive.ReactiveRpcStreamer;
import com.ensarsarajcic.neovim.java.handler.NeovimHandlerManager;
import com.ensarsarajcic.neovim.java.handler.NeovimHandlerProxy;
import com.ensarsarajcic.neovim.java.notifications.NeovimStreamNotificationHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

public final class NeovimJavaPluginHost {

    private final RpcConnection rpcConnection;
    private final NeovimHandlerManager neovimHandlerManager;
    private final NeovimHandlerProxy neovimHandlerProxy;
    private final NeovimApi api;
    private final NeovimStreamNotificationHandler neovimStreamNotificationHandler;
    private final RpcStreamer client;
    private final ReactiveRpcStreamer reactiveClient;
    private final RemotePluginManager remotePluginManager;

    private ApiInfo apiInfo = null;
    private PluginApi pluginApi = null;

    public NeovimJavaPluginHost() {
        this(new NeovimHandlerProxy(Executors.newSingleThreadExecutor()));
    }

    public NeovimJavaPluginHost(NeovimHandlerProxy neovimHandlerProxy) {
        this.rpcConnection = new StdIoRpcConnection();
        this.neovimHandlerProxy = neovimHandlerProxy;
        this.neovimHandlerManager = new NeovimHandlerManager(neovimHandlerProxy, (type, o) -> {
            try {
                return ObjectMappers.defaultNeovimMapper().readerFor(type).readValue(
                        ObjectMappers.defaultNeovimMapper().writeValueAsBytes(o)
                );
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        });
        client = NeovimApis.getNeovimRpcStreamer();
        reactiveClient = ReactiveRpcClient.createDefaultInstanceWithCustomStreamer(client);
        neovimStreamNotificationHandler = new NeovimStreamNotificationHandler(reactiveClient);
        remotePluginManager = new RemotePluginManager(neovimHandlerManager, neovimHandlerProxy, client);
        api = new NeovimStreamApi(reactiveClient);
    }

    public CompletableFuture<Void> start(String[] args) {
        if (apiInfo != null) throw new RuntimeException("Plugin already started!");

        client.attach(rpcConnection);
        return api.getApiInfo()
                .thenAccept(info -> {
                    neovimHandlerManager.attachToStream(client);
                    this.apiInfo = info;
                    this.pluginApi = new PluginApi(api, apiInfo);
                })
                .thenCompose(v -> api.executeAutocommands(List.of("User"), Map.of("pattern", "NeovimJavaPrepare", "modeline", false)))
                .thenCompose(v -> remotePluginManager.setupRemotePlugins(this))
                .thenCompose(v -> api.executeAutocommands(List.of("User"), Map.of("pattern", "NeovimJavaReady", "modeline", false)))
                .thenCompose(v -> remotePluginManager.startRemotePlugins());
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

    public RpcStreamer getClient() {
        return client;
    }

    public ReactiveRpcStreamer getReactiveClient() {
        return reactiveClient;
    }
}
