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

package com.ensarsarajcic.neovim.java.corerpc.client;

import com.ensarsarajcic.neovim.java.corerpc.message.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Two-way msgpack stream that wraps reading/writing bytes and exposes
 * an interface for sending {@link Message}
 *
 * It is implemented by delegating reading to {@link RPCListener} and writing to {@link RPCSender},
 * since {@link RPCSender} supports both of these functionalities. A single interface makes it easier
 * to communicate in a request/response model of communication.
 *
 * Besides just passing down writing/reading, this class allows multiple {@link RPCListener.RequestCallback} and
 * multiple {@link RPCListener.NotificationCallback} by delegating to internally held callbacks
 * It also handles message id generation, meaning users of this class don't have to manually set id's for messages
 * Message id generation is handled by {@link MessageIdGenerator} which can optionally be overriden through constructor.
 * By default {@link SequentialMessageIdGenerator} is used.
 *
 * Sending messages supports any kind of {@link Message}
 * and for {@link RequestMessage}, {@link RPCListener.ResponseCallback} is supported, which will be called once
 * corresponding {@link ResponseMessage} arrives (message with same id as the request sent)
 *
 * Example:
 * <pre>
 *     {@code
 *     // Existing listener and sender
 *     RPCStreamer rpcStreamer = new PackStream(sender, listener);
 *     // Existing connection
 *     rpcStreamer.attach(connection);
 *
 *     rpcStreamer.send(message, (id, response) -> System.out.println(response)); // callback for request
 *
 *     rpcStreamer.send(responseMessage); // Sending a response -> no need for a callback
 *
 *     rpcStreamer.send(requestMessage); // Sending a request - fire and forget - no callback
 *     }
 * </pre>
 *
 * Example with custom id generator:
 * <pre>
 *     {@code
 *     // Create a generator
 *     MessageIdGenerator generator = createCustomMessageIdGenerator();
 *
 *     // Existing listener and sender
 *     RPCStreamer rpcStreamer = new PackStream(sender, listener, generator);
 *
 *     // ...
 *     }
 * </pre>
 */
public final class PackStream implements RPCStreamer {
    public static final Logger log = LoggerFactory.getLogger(PackStream.class);

    private final RPCListener rpcListener;
    private final RPCSender rpcSender;
    private final MessageIdGenerator messageIdGenerator;

    private List<RPCListener.RequestCallback> requestCallbacks = new ArrayList<>();
    private List<RPCListener.NotificationCallback> notificationCallbacks = new ArrayList<>();

    /**
     * Creates a new {@link PackStream} with given {@link RPCSender} for sending messages
     * and an {@link RPCListener} for listening for incoming requests, responses and notifications
     * Uses {@link SequentialMessageIdGenerator} for {@link MessageIdGenerator}
     * @param rpcSender {@link RPCSender} for sending data
     * @param rpcListener {@link RPCListener} for listening to incoming data
     * @throws NullPointerException if any parameter is null
     */
    public PackStream(RPCSender rpcSender, RPCListener rpcListener) {
        this(rpcSender, rpcListener, new SequentialMessageIdGenerator());
    }

    /**
     * Creates a new {@link PackStream} with given {@link RPCSender} for sending messages
     * and an {@link RPCListener} for listening for incoming requests, responses and notifications
     * @param rpcSender {@link RPCSender} for sending data
     * @param rpcListener {@link RPCListener} for listening to incoming data
     * @param messageIdGenerator {@link MessageIdGenerator} for generating request message ids
     * @throws NullPointerException if any parameter is null
     */
    public PackStream(RPCSender rpcSender, RPCListener rpcListener, MessageIdGenerator messageIdGenerator) {
        Objects.requireNonNull(rpcSender, "rpcSender must be provided for two way communication");
        Objects.requireNonNull(rpcListener, "rpcListener must be provided for two way communication");
        Objects.requireNonNull(messageIdGenerator, "messageIdGenerator must be provided for sending requests");
        this.rpcListener = rpcListener;
        this.rpcSender = rpcSender;
        this.messageIdGenerator = messageIdGenerator;
    }

    /**
     * Prepares for writing to outgoing stream and prepares for reading from input stream
     * Sets up listeners on the input stream, so that current and any new request/response/notification callbacks may
     * be called -> prepares the underlying {@link RPCListener}
     * Also prepares for writing messages -> prepares the underlying {@link RPCSender}
     * @throws NullPointerException if rpcConnection is null
     */
    @Override
    public void attach(RPCConnection rpcConnection) {
        Objects.requireNonNull(rpcConnection, "rpcConnection may not be null");
        log.info("Attaching PackStream to: {}", rpcConnection);
        startListening(rpcConnection.getIncomingStream());
        rpcSender.attach(rpcConnection.getOutgoingStream());
    }

    /**
     * Implemented per {@link RPCStreamer#send(Message)} specification
     * Passes the message down to underlying {@link RPCSender}, without callback
     */
    @Override
    public void send(Message message) throws IOException {
        log.debug("Sending message: {}", message);
        rpcSender.send(message);
    }

    /**
     * Implemented per {@link RPCStreamer#send(RequestMessage.Builder)} specification
     * Passes the message down to underlying {@link RPCSender}, without callback
     */
    @Override
    public void send(RequestMessage.Builder requestMessage) throws IOException {
        send(requestMessage, null);
    }

    /**
     * Implemented per {@link RPCStreamer#send(RequestMessage.Builder, RPCListener.ResponseCallback)} specification
     * Passes the message down to underlying {@link RPCSender}, with callback
     * First id for the message is generated using {@link MessageIdGenerator}, callback for that id is prepared on
     * {@link RPCListener} and then message is send using {@link RPCSender}
     */
    @Override
    public void send(RequestMessage.Builder requestMessage, RPCListener.ResponseCallback responseCallback) throws IOException {
        RequestMessage messageToSend = requestMessage.withId(messageIdGenerator.nextId()).build();
        rpcListener.listenForResponse(messageToSend.getId(), responseCallback);
        send(messageToSend);
    }

    /**
     * Adds a new {@link RPCListener.RequestCallback}
     * per {@link RPCStreamer#addRequestCallback(RPCListener.RequestCallback)} specification
     */
    @Override
    public void addRequestCallback(RPCListener.RequestCallback requestCallback) {
        log.info("Registered a new request callback: {}", requestCallback);
        if (!requestCallbacks.contains(requestCallback)) {
            this.requestCallbacks.add(requestCallback);
        }
    }

    /**
     * Removes a {@link RPCListener.RequestCallback}
     * per {@link RPCStreamer#removeRequestCallback(RPCListener.RequestCallback)} specification
     */
    @Override
    public void removeRequestCallback(RPCListener.RequestCallback requestCallback) {
        log.info("Removed a request callback: {}", requestCallback);
        if (requestCallbacks.contains(requestCallback)) {
            this.requestCallbacks.remove(requestCallback);
        }
    }

    /**
     * Adds a new {@link RPCListener.NotificationCallback}
     * per {@link RPCStreamer#addNotificationCallback(RPCListener.NotificationCallback)} specification
     */
    @Override
    public void addNotificationCallback(RPCListener.NotificationCallback notificationCallback) {
        log.info("Registered a new notification callback: {}", notificationCallback);
        if (!notificationCallbacks.contains(notificationCallback)) {
            this.notificationCallbacks.add(notificationCallback);
        }
    }

    /**
     * Removes a {@link RPCListener.NotificationCallback}
     * per {@link RPCStreamer#removeNotificationCallback(RPCListener.NotificationCallback)} specification
     */
    @Override
    public void removeNotificationCallback(RPCListener.NotificationCallback notificationCallback) {
        log.info("Removed a notification callback: {}", notificationCallback);
        if (notificationCallbacks.contains(notificationCallback)) {
            this.notificationCallbacks.remove(notificationCallback);
        }
    }

    private void requestReceived(RequestMessage requestMessage) {
        log.info("Request received: {}", requestMessage);
        for (RPCListener.RequestCallback requestCallback : requestCallbacks) {
            requestCallback.requestReceived(requestMessage);
        }
    }

    private void notificationReceived(NotificationMessage notificationMessage) {
        log.info("Notification received: {}", notificationMessage);
        for (RPCListener.NotificationCallback notificationCallback : notificationCallbacks) {
            notificationCallback.notificationReceived(notificationMessage);
        }
    }

    private void startListening(InputStream inputStream) {
        rpcListener.listenForNotifications(this::notificationReceived);
        rpcListener.listenForRequests(this::requestReceived);
        rpcListener.start(inputStream);
    }
}
