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
import com.ensarsarajcic.neovim.java.api.types.msgpack.NeovimJacksonModule;
import com.ensarsarajcic.neovim.java.corerpc.client.RpcClient;
import com.ensarsarajcic.neovim.java.corerpc.client.RpcConnection;
import com.ensarsarajcic.neovim.java.corerpc.reactive.ReactiveRPCClient;
import com.ensarsarajcic.neovim.java.corerpc.reactive.ReactiveRPCStreamer;

public final class ConnectionHolder {

    private ConnectionHolder() {
        throw new AssertionError("No instances");
    }

    private static RpcConnection connection;
    private static NeovimApi neovimApi;
    private static ReactiveRPCStreamer reactiveRPCStreamer;
    private static String connectedIpPort;

    public static void setConnection(RpcConnection rpcConnection) {
        connection = rpcConnection;
    }

    public static ReactiveRPCStreamer getReactiveRPCStreamer() {
        if (reactiveRPCStreamer == null) {
            synchronized (ConnectionHolder.class) {
                if (reactiveRPCStreamer == null) {
                    var rpcClient = new RpcClient.Builder()
                            .withObjectMapper(NeovimJacksonModule.createNeovimObjectMapper()).build();
                    rpcClient.attach(connection);

                    reactiveRPCStreamer = ReactiveRPCClient.createDefaultInstanceWithCustomStreamer(rpcClient);
                }
            }
        }

        return reactiveRPCStreamer;
    }

    public static NeovimApi getApi() {
        if (neovimApi == null) {
            synchronized (ConnectionHolder.class) {
                if (neovimApi == null) {
                    neovimApi = new NeovimStreamApi(getReactiveRPCStreamer());
                }
            }
        }

        return neovimApi;
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
