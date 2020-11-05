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

package com.ensarsarajcic.neovim.java.corerpc.reactive;

import com.ensarsarajcic.neovim.java.corerpc.client.RpcClient;
import com.ensarsarajcic.neovim.java.corerpc.client.RpcConnection;
import com.ensarsarajcic.neovim.java.corerpc.client.RpcStreamer;
import com.ensarsarajcic.neovim.java.corerpc.message.NotificationMessage;
import com.ensarsarajcic.neovim.java.corerpc.message.RequestMessage;
import com.ensarsarajcic.neovim.java.corerpc.message.ResponseMessage;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Flow;

/**
 * Wrapper around {@link ReactiveRpcStreamer}
 * This class should be used for communication.
 * It provides convenience factory methods
 * <p>
 * All calls are passed down to underlying {@link ReactiveRpcStreamer}
 * <p>
 * Examples:
 * <pre>
 *     ReactiveRpcStreamer defaultSharedClient = ReactiveRpcClient.getDefaultInstance(); // shared singleton
 *
 *     ReactiveRpcStreamer defaultClient = ReactiveRpcClient.createDefaultInstance(); // new instance with same config as shared singleton
 * </pre>
 */
public final class ReactiveRpcClient implements ReactiveRpcStreamer {

    private static ReactiveRpcClient defaultSharedInstance;

    private ReactiveRpcStreamer reactiveRpcStreamer;

    private ReactiveRpcClient(ReactiveRpcStreamer reactiveRpcStreamer) {
        Objects.requireNonNull(reactiveRpcStreamer, "reactiveRpcStreamer is required for all operations");
        this.reactiveRpcStreamer = reactiveRpcStreamer;
    }

    /**
     * Creates a default instance of {@link ReactiveRpcClient}
     *
     * @return <b>New instance</b> of {@link ReactiveRpcClient}
     */
    public static ReactiveRpcClient createDefaultInstance() {
        return new ReactiveRpcClient(createDefaultReactiveRpcStreamer());
    }

    /**
     * Creates a new instance of {@link ReactiveRpcClient} based on custom {@link ReactiveRpcStreamer}
     *
     * @return <b>New instance</b> of {@link ReactiveRpcClient}
     */
    public static ReactiveRpcClient createInstanceWithCustomReactiveStreamer(ReactiveRpcStreamer reactiveRpcStreamer) {
        return new ReactiveRpcClient(reactiveRpcStreamer);
    }

    /**
     * Creates a new instance of {@link ReactiveRpcClient} based on {@link ReactiveRpcStreamerWrapper} with custom {@link RpcStreamer}
     *
     * @return <b>New instance</b> of {@link ReactiveRpcClient}
     */
    public static ReactiveRpcClient createDefaultInstanceWithCustomStreamer(RpcStreamer rpcStreamer) {
        return new ReactiveRpcClient(createDefaultReactiveRpcStreamer(rpcStreamer));
    }

    private static ReactiveRpcStreamer createDefaultReactiveRpcStreamer(RpcStreamer rpcStreamer) {
        return new ReactiveRpcStreamerWrapper(rpcStreamer);
    }

    private static ReactiveRpcStreamer createDefaultReactiveRpcStreamer() {
        return new ReactiveRpcStreamerWrapper(RpcClient.createDefaultAsyncInstance());
    }

    /**
     * Takes a default instance (shared - singleton) of {@link ReactiveRpcClient}
     *
     * @return <b>Default shared instance</b> of {@link ReactiveRpcClient}
     */
    public static ReactiveRpcClient getDefaultInstance() {
        if (defaultSharedInstance == null) {
            synchronized (ReactiveRpcClient.class) {
                if (defaultSharedInstance == null) {
                    defaultSharedInstance = createDefaultInstance();
                }
            }
        }

        return defaultSharedInstance;
    }

    /**
     * Calls underlying {@link ReactiveRpcStreamer}
     *
     * @param rpcConnection connection to attach to
     */
    @Override
    public void attach(RpcConnection rpcConnection) {
        reactiveRpcStreamer.attach(rpcConnection);
    }

    /**
     * Calls underlying {@link ReactiveRpcStreamer}
     *
     * @param requestMessage {@link RequestMessage.Builder} of message to send
     * @return {@link CompletableFuture} with response
     */
    @Override
    public CompletableFuture<ResponseMessage> response(RequestMessage.Builder requestMessage) {
        return reactiveRpcStreamer.response(requestMessage);
    }

    /**
     * Calls underlying {@link ReactiveRpcStreamer}
     *
     * @return {@link Flow.Publisher} of request messages
     */
    @Override
    public Flow.Publisher<RequestMessage> requestsFlow() {
        return reactiveRpcStreamer.requestsFlow();
    }

    /**
     * Calls underlying {@link ReactiveRpcStreamer}
     *
     * @return {@link Flow.Publisher} of notifications messages
     */
    @Override
    public Flow.Publisher<NotificationMessage> notificationsFlow() {
        return reactiveRpcStreamer.notificationsFlow();
    }
}
