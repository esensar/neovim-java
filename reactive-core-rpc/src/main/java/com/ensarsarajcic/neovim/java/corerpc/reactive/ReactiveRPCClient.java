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

import com.ensarsarajcic.neovim.java.corerpc.client.*;
import com.ensarsarajcic.neovim.java.corerpc.message.NotificationMessage;
import com.ensarsarajcic.neovim.java.corerpc.message.RequestMessage;
import com.ensarsarajcic.neovim.java.corerpc.message.ResponseMessage;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Flow;

/**
 * Wrapper around {@link ReactiveRPCStreamer}
 * This class should be used for communication.
 * It provides convenience factory methods
 * <p>
 * All calls are passed down to underlying {@link ReactiveRPCStreamer}
 * <p>
 * Examples:
 * <pre>
 *     ReactiveRPCStreamer defaultSharedClient = ReactiveRPCClient.getDefaultInstance(); // shared singleton
 *
 *     ReactiveRPCStreamer defaultClient = ReactiveRPCClient.createDefaultInstance(); // new instance with same config as shared singleton
 * </pre>
 */
public final class ReactiveRPCClient implements ReactiveRPCStreamer {

    private static ReactiveRPCClient defaultSharedInstance;

    private ReactiveRPCStreamer reactiveRPCStreamer;

    private ReactiveRPCClient(ReactiveRPCStreamer reactiveRPCStreamer) {
        Objects.requireNonNull(reactiveRPCStreamer, "reactiveRPCStreamer is required for all operations");
        this.reactiveRPCStreamer = reactiveRPCStreamer;
    }

    /**
     * Creates a default instance of {@link ReactiveRPCClient}
     *
     * @return <b>New instance</b> of {@link ReactiveRPCClient}
     */
    public static ReactiveRPCClient createDefaultInstance() {
        return new ReactiveRPCClient(createDefaultReactiveRPCStreamer());
    }

    /**
     * Creates a new instance of {@link ReactiveRPCClient} based on custom {@link ReactiveRPCStreamer}
     *
     * @return <b>New instance</b> of {@link ReactiveRPCClient}
     */
    public static ReactiveRPCClient createInstanceWithCustomReactiveStreamer(ReactiveRPCStreamer reactiveRPCStreamer) {
        return new ReactiveRPCClient(reactiveRPCStreamer);
    }

    /**
     * Creates a new instance of {@link ReactiveRPCClient} based on {@link ReactiveRPCStreamerWrapper} with custom {@link RpcStreamer}
     *
     * @return <b>New instance</b> of {@link ReactiveRPCClient}
     */
    public static ReactiveRPCClient createDefaultInstanceWithCustomStreamer(RpcStreamer rpcStreamer) {
        return new ReactiveRPCClient(createDefaultReactiveRPCStreamer(rpcStreamer));
    }

    private static ReactiveRPCStreamer createDefaultReactiveRPCStreamer(RpcStreamer rpcStreamer) {
        return new ReactiveRPCStreamerWrapper(rpcStreamer);
    }

    private static ReactiveRPCStreamer createDefaultReactiveRPCStreamer() {
        return new ReactiveRPCStreamerWrapper(RpcClient.createDefaultAsyncInstance());
    }

    /**
     * Takes a default instance (shared - singleton) of {@link ReactiveRPCClient}
     *
     * @return <b>Default shared instance</b> of {@link ReactiveRPCClient}
     */
    public static ReactiveRPCClient getDefaultInstance() {
        if (defaultSharedInstance == null) {
            synchronized (ReactiveRPCClient.class) {
                if (defaultSharedInstance == null) {
                    defaultSharedInstance = createDefaultInstance();
                }
            }
        }

        return defaultSharedInstance;
    }

    /**
     * Calls underlying {@link ReactiveRPCStreamer}
     *
     * @param rpcConnection connection to attach to
     */
    @Override
    public void attach(RpcConnection rpcConnection) {
        reactiveRPCStreamer.attach(rpcConnection);
    }

    /**
     * Calls underlying {@link ReactiveRPCStreamer}
     *
     * @param requestMessage {@link RequestMessage.Builder} of message to send
     * @return {@link CompletableFuture} with response
     */
    @Override
    public CompletableFuture<ResponseMessage> response(RequestMessage.Builder requestMessage) {
        return reactiveRPCStreamer.response(requestMessage);
    }

    /**
     * Calls underlying {@link ReactiveRPCStreamer}
     *
     * @return {@link Flow.Publisher} of request messages
     */
    @Override
    public Flow.Publisher<RequestMessage> requestsFlow() {
        return reactiveRPCStreamer.requestsFlow();
    }

    /**
     * Calls underlying {@link ReactiveRPCStreamer}
     *
     * @return {@link Flow.Publisher} of notifications messages
     */
    @Override
    public Flow.Publisher<NotificationMessage> notificationsFlow() {
        return reactiveRPCStreamer.notificationsFlow();
    }
}
