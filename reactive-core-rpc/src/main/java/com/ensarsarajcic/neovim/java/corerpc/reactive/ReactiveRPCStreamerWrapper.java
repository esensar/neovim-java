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

import com.ensarsarajcic.neovim.java.corerpc.client.RpcConnection;
import com.ensarsarajcic.neovim.java.corerpc.client.RpcStreamer;
import com.ensarsarajcic.neovim.java.corerpc.message.NotificationMessage;
import com.ensarsarajcic.neovim.java.corerpc.message.RequestMessage;
import com.ensarsarajcic.neovim.java.corerpc.message.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * Implementation of {@link ReactiveRPCStreamer} relying on a regular {@link RpcStreamer}
 * Provides reactive mappings by just wrapping regular calls in reactive streams
 * <p>
 * It is implemented by delegating all operations to a regular {@link RpcStreamer}, but doing it
 * in a wrapped {@link CompletableFuture}
 * If {@link Executor} is provided in the constructor, it is used for {@link CompletableFuture},
 * otherwise default provided by {@link CompletableFuture} is used
 * <p>
 * Notifications and requests are exposed as {@link Flow.Publisher} and are implemented by listening
 * to notifications and requests from the wrapped {@link RpcStreamer} and supplying them to the publishers
 * <p>
 * Example:
 * <pre>
 *     {@code
 *     ReactiveRPCStreamer reactiveRPCStreamer = new ReactiveRPCStreamerWrapper(rpcStreamer); // existing rpc streamer, default executor
 *
 *     reactiveRPCStreamer.response(request).thenAccept(System.out::println); // requesting
 *     reactiveRPCStreamer.notificationsFlow().subscribe(notificationsSubscriber); // notifications subscription
 *
 *     // Custom executor usage
 *     ReactiveRPCStreamer customExecutorStreamer = new ReactiveRPCStreamerWrapper(rpcStreamer, customExecutor);
 *     // All of the response calls on this streamer will now run CompletableFuture on the provided executor
 *     }
 * </pre>
 */
public final class ReactiveRPCStreamerWrapper implements ReactiveRPCStreamer {
    private static final Logger log = LoggerFactory.getLogger(ReactiveRPCStreamerWrapper.class);

    private RpcStreamer rpcStreamer;
    private Executor executor;

    private final SubmissionPublisher<RequestMessage> requestMessagePublisher = new SubmissionPublisher<>();
    private final SubmissionPublisher<NotificationMessage> notificationMessagePublisher = new SubmissionPublisher<>();

    /**
     * Constructs {@link ReactiveRPCStreamerWrapper} with provided {@link RpcStreamer} and default {@link Executor}
     * @param rpcStreamer {@link RpcStreamer} to use for making calls and listening for notifications/requests
     * @throws NullPointerException if {@link RpcStreamer} is null
     */
    public ReactiveRPCStreamerWrapper(RpcStreamer rpcStreamer) {
        this(rpcStreamer, null);
    }

    /**
     * Constructs {@link ReactiveRPCStreamerWrapper} with provided {@link RpcStreamer}
     * and with provided {@link Executor} - it is used only for requests
     * @param rpcStreamer {@link RpcStreamer} to use for making calls and listening for notifications/requests
     * @param executor {@link Executor} to use for creating {@link CompletableFuture} for requests
     * @throws NullPointerException if {@link RpcStreamer} is null
     */
    public ReactiveRPCStreamerWrapper(RpcStreamer rpcStreamer, Executor executor) {
        Objects.requireNonNull(rpcStreamer, "rpcStreamer may not be null");
        this.rpcStreamer = rpcStreamer;
        this.executor = executor;
    }

    /**
     * Implemented per {@link ReactiveRPCStreamer#attach(RpcConnection)} specification
     * Attaches underlying {@link RpcStreamer} and also attaches own callbacks for requests and notifications
     * to be able to provide them as {@link Flow} (for {@link #requestsFlow()} and {@link #notificationsFlow()}
     */
    @Override
    public void attach(RpcConnection rpcConnection) {
        rpcStreamer.attach(rpcConnection);
        rpcStreamer.addRequestCallback(requestMessagePublisher::submit);
        rpcStreamer.addNotificationCallback(notificationMessagePublisher::submit);
    }

    /**
     * Implemented per {@link ReactiveRPCStreamer#response(RequestMessage.Builder)} specification
     * Uses underlying {@link RpcStreamer} to send the message and awaits a response from it,
     * creating a {@link CompletableFuture} from it
     * <p>
     * If {@link Executor} is provided in the constructor, {@link CompletableFuture} will use it,
     * otherwise, default is used
     */
    @Override
    public CompletableFuture<ResponseMessage> response(RequestMessage.Builder requestMessage) {
        if (executor == null) {
            return CompletableFuture.supplyAsync(responseSupplier(requestMessage));
        } else {
            return CompletableFuture.supplyAsync(responseSupplier(requestMessage), executor);
        }
    }

    /**
     * Implemented per {@link ReactiveRPCStreamer#requestsFlow()} specification
     * Provides requests from underlying {@link RpcStreamer} in a {@link Flow}
     */
    @Override
    public Flow.Publisher<RequestMessage> requestsFlow() {
        return requestMessagePublisher;
    }

    /**
     * Implemented per {@link ReactiveRPCStreamer#notificationsFlow()} specification
     * Provides notifications from underlying {@link RpcStreamer} in a {@link Flow}
     */
    @Override
    public Flow.Publisher<NotificationMessage> notificationsFlow() {
        return notificationMessagePublisher;
    }

    private Supplier<ResponseMessage> responseSupplier(RequestMessage.Builder requestMessage) {
        return() -> {
            // Prepare for blocking until response comes
            var countDownLatch = new CountDownLatch(1);
            var responseMessage = new AtomicReference<ResponseMessage>();
            try {
                // Send request
                rpcStreamer.send(requestMessage, (forId, response) -> {
                    // Unblock and save response
                    responseMessage.set(response);
                    countDownLatch.countDown();
                });
                // Wait for response
                countDownLatch.await();
                if (responseMessage.get().getError() != null) {
                    log.info("Received an error response: {}", responseMessage);
                    throw new CompletionException(new RPCException(responseMessage.get().getError()));
                }
                return responseMessage.get();
            } catch (IOException | InterruptedException e) {
                log.error("Error while sending message!", e);
                e.printStackTrace();
                // Pass down exception on any failure
                throw new CompletionException(e);
            }
        };
    }
}
