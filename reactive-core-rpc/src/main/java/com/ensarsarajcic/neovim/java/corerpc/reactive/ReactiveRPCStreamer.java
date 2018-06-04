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

import com.ensarsarajcic.neovim.java.corerpc.client.RPCConnection;
import com.ensarsarajcic.neovim.java.corerpc.client.RPCListener;
import com.ensarsarajcic.neovim.java.corerpc.message.NotificationMessage;
import com.ensarsarajcic.neovim.java.corerpc.message.RequestMessage;
import com.ensarsarajcic.neovim.java.corerpc.message.ResponseMessage;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Flow;

/**
 * Interface defining rective variant of {@link com.ensarsarajcic.neovim.java.corerpc.client.RPCStreamer}
 */
public interface ReactiveRPCStreamer {
    /**
     * Attaches to given {@link RPCConnection}
     * Connects callbacks to its incoming stream and prepares for writing to outgoing stream
     * @param rpcConnection connection to attach to
     */
    void attach(RPCConnection rpcConnection);

    /**
     * Reactive variant of
     * {@link com.ensarsarajcic.neovim.java.corerpc.client.RPCStreamer#send(RequestMessage.Builder)}
     * which uses {@link CompletableFuture} instead of
     * {@link com.ensarsarajcic.neovim.java.corerpc.client.RPCListener.ResponseCallback}
     *
     * @param requestMessage {@link RequestMessage.Builder} of message to send
     * @see com.ensarsarajcic.neovim.java.corerpc.client.RPCStreamer#send(RequestMessage.Builder, RPCListener.ResponseCallback)
     */
    CompletableFuture<ResponseMessage> response(RequestMessage.Builder requestMessage);

    /**
     * Passes down a publisher of {@link RequestMessage} objects received
     * It will never complete
     * @return {@link Flow.Publisher} passing down requests as they come
     */
    Flow.Publisher<RequestMessage> requestsFlow();

    /**
     * Passes down a publisher of {@link NotificationMessage} objects received
     * It will never complete
     * @return {@link Flow.Publisher} passing down notifications as they come
     */
    Flow.Publisher<NotificationMessage> notificationsFlow();
}
