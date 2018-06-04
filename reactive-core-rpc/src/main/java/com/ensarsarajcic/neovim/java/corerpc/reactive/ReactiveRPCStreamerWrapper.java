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
import com.ensarsarajcic.neovim.java.corerpc.client.RPCStreamer;
import com.ensarsarajcic.neovim.java.corerpc.message.NotificationMessage;
import com.ensarsarajcic.neovim.java.corerpc.message.RequestMessage;
import com.ensarsarajcic.neovim.java.corerpc.message.ResponseMessage;

import java.io.IOException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Implementation of {@link ReactiveRPCStreamer} relying on a regular {@link RPCStreamer}
 * Provides reactive mappings by just wrapping regular calls in reactive streams
 */
public final class ReactiveRPCStreamerWrapper implements ReactiveRPCStreamer {

    private RPCStreamer rpcStreamer;

    private final SubmissionPublisher<RequestMessage> requestMessagePublisher = new SubmissionPublisher<>();
    private final SubmissionPublisher<NotificationMessage> notificationMessagePublisher = new SubmissionPublisher<>();

    public ReactiveRPCStreamerWrapper(RPCStreamer rpcStreamer) {
        this.rpcStreamer = rpcStreamer;
    }

    /**
     * Implemented per {@link ReactiveRPCStreamer#attach(RPCConnection)} specification
     * Attaches underlying {@link RPCStreamer} and also attaches own callbacks for requests and notifications
     * to be able to provide them as {@link Flow} (for {@link #requestsFlow()} and {@link #notificationsFlow()}
     */
    @Override
    public void attach(RPCConnection rpcConnection) {
        rpcStreamer.attach(rpcConnection);
        rpcStreamer.addRequestCallback(requestMessagePublisher::submit);
        rpcStreamer.addNotificationCallback(notificationMessagePublisher::submit);
    }

    /**
     * Implemented per {@link ReactiveRPCStreamer#response(RequestMessage.Builder)} specification
     * Uses underlying {@link RPCStreamer} to send the message and awaits a response from it,
     * creating a {@link CompletableFuture} from it
     */
    @Override
    public CompletableFuture<ResponseMessage> response(RequestMessage.Builder requestMessage) {
        return CompletableFuture.supplyAsync(() -> {
            // Prepare for blocking until response comes
            CountDownLatch countDownLatch = new CountDownLatch(1);
            AtomicReference<ResponseMessage> responseMessage = new AtomicReference<>();
            try {
                // Send request
                rpcStreamer.send(requestMessage, (forId, response) -> {
                    // Unblock and save response
                    responseMessage.set(response);
                    countDownLatch.countDown();
                });
                // Wait for response
                countDownLatch.await();
                return responseMessage.get();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                // Pass down exception on any failure
                throw new CompletionException(e);
            }
        });
    }

    /**
     * Implemented per {@link ReactiveRPCStreamer#requestsFlow()} specification
     * Provides requests from underlying {@link RPCStreamer} in a {@link Flow}
     */
    @Override
    public Flow.Publisher<RequestMessage> requestsFlow() {
        return requestMessagePublisher;
    }

    /**
     * Implemented per {@link ReactiveRPCStreamer#notificationsFlow()} specification
     * Provides notifications from underlying {@link RPCStreamer} in a {@link Flow}
     */
    @Override
    public Flow.Publisher<NotificationMessage> notificationsFlow() {
        return notificationMessagePublisher;
    }
}
