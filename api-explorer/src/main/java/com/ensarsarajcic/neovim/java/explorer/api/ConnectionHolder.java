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

package com.ensarsarajcic.neovim.java.explorer.api;

import com.ensarsarajcic.neovim.java.api.NeovimApi;
import com.ensarsarajcic.neovim.java.api.NeovimStreamApi;
import com.ensarsarajcic.neovim.java.api.util.ObjectMappers;
import com.ensarsarajcic.neovim.java.corerpc.client.RpcClient;
import com.ensarsarajcic.neovim.java.corerpc.client.RpcConnection;
import com.ensarsarajcic.neovim.java.corerpc.reactive.ReactiveRpcClient;
import com.ensarsarajcic.neovim.java.corerpc.reactive.ReactiveRpcStreamer;

public final class ConnectionHolder {

    private ConnectionHolder() {
        throw new AssertionError("No instances");
    }

    private static RpcConnection connection;
    private static NeovimApi neovimApi;
    private static ReactiveRpcStreamer reactiveRpcStreamer;
    private static String connectedIpPort;

    private static String executable = "nvim";

    public static void setConnection(RpcConnection rpcConnection) {
        connection = rpcConnection;
    }

    public static ReactiveRpcStreamer getReactiveRpcStreamer() {
        if (reactiveRpcStreamer == null) {
            synchronized (ConnectionHolder.class) {
                if (reactiveRpcStreamer == null) {
                    var rpcClient = new RpcClient.Builder()
                            .withObjectMapper(ObjectMappers.defaultNeovimMapper()).build();
                    rpcClient.attach(connection);

                    reactiveRpcStreamer = ReactiveRpcClient.createDefaultInstanceWithCustomStreamer(rpcClient);
                }
            }
        }

        return reactiveRpcStreamer;
    }

    public static NeovimApi getApi() {
        if (neovimApi == null) {
            synchronized (ConnectionHolder.class) {
                if (neovimApi == null) {
                    neovimApi = new NeovimStreamApi(getReactiveRpcStreamer());
                }
            }
        }

        return neovimApi;
    }

    public static String getExecutable() {
        return executable;
    }

    public static void setExecutable(String executable) {
        ConnectionHolder.executable = executable;
    }

    public static RpcConnection getConnection() {
        return connection;
    }

    public static String getConnectedIpPort() {
        return connectedIpPort;
    }

    public static void setConnectedIpPort(String connectedIpPort) {
        ConnectionHolder.connectedIpPort = connectedIpPort;
    }
}
