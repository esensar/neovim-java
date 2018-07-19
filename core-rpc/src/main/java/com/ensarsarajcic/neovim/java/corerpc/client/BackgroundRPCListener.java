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

import com.ensarsarajcic.neovim.java.corerpc.message.MessageType;
import com.ensarsarajcic.neovim.java.corerpc.message.NotificationMessage;
import com.ensarsarajcic.neovim.java.corerpc.message.RequestMessage;
import com.ensarsarajcic.neovim.java.corerpc.message.ResponseMessage;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Implementation of {@link RPCListener}
 * utilizing {@link ExecutorService} for background work
 *
 * Messages are read using {@link ExecutorService}
 * An infinite task is submitted to the service which constantly waits for and reads messages from the attached stream
 * It may be stopped using {@link #stop()}
 *
 * Messages are deserialized using {@link ObjectMapper} passed in the constructor
 *
 * This class supports 3 types of callbacks required by {@link RPCListener}:
 *  - {@link RPCListener.RequestCallback}
 *  - {@link RPCListener.ResponseCallback} with a specific id to respond to
 *  - {@link RPCListener.NotificationCallback}
 *
 *  Every {@link RPCListener.RequestCallback} is notified when request comes
 *  Request callback is run on the same thread as the one used for reading, meaning long running callbacks will
 *  block reading. For longer tasks async request callback should be implemented.
 *
 *  Same holds true for {@link RPCListener.NotificationCallback} and {@link RPCListener.RequestCallback}, with the
 *  exception that {@link RPCListener.RequestCallback} will only be called for messages with matching id
 *
 *  Example:
 *  <pre>
 *      {@code
 *     ExecutorService executorService = Executors.newSingleThreadExecutor();
 *
 *     // Factory to support msgpack
 *     // Use other classes from the library to avoid having to manually create this
 *     MessagePackFactory factory = new MessagePackFactory();
 *     factory.disable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
 *     factory.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
 *     ObjectMapper objectMapper = new ObjectMapper(factory);
 *
 *     RPCListener rpcListener = new BackgroundRPCListener(executorService, objectMapper);
 *     // Registering callbacks
 *     rpcListener.listenForRequests(request -> System.out.println(request));
 *     rpcListener.listenForNotifications(notification -> System.out.println(notification));
 *     rpcListener.listenForResponse(1, response -> System.out.println(response)); // used together with sender
 *
 *     // Starting
 *     rpcListener.start(inputStream); // an existing InputStream
 *
 *     // ...
 *
 *     // End
 *     rpcListener.stop(); // cancels listener and interrupts any running task
 *      }
 *  </pre>
 */
public final class BackgroundRPCListener implements RPCListener {
    public static final Logger log = LoggerFactory.getLogger(BackgroundRPCListener.class);

    private final ExecutorService executorService;
    private final ObjectMapper responseObjectMapper;

    private NotificationCallback notificationCallback;
    private RequestCallback requestCallback;
    private Map<Integer, ResponseCallback> responseCallbacks = new ConcurrentHashMap<>();

    private Future listener;

    /**
     * Creates a new {@link BackgroundRPCListener} using {@link ExecutorService} for background work
     * and given {@link ObjectMapper} for mapping responses
     * @param executorService service used for background work
     * @param responseObjectMapper mapper used for mapping responses
     * @throws NullPointerException if any parameter is null
     */
    public BackgroundRPCListener(ExecutorService executorService, ObjectMapper responseObjectMapper) {
        Objects.requireNonNull(executorService, "executorService must be provided to enable background work");
        Objects.requireNonNull(responseObjectMapper, "responseObjectMapper must be provided to deserialize");
        this.executorService = executorService;
        this.responseObjectMapper = responseObjectMapper;
    }

    /**
     * Starts listening on given input stream on
     * background thread (using given executor service)
     *
     * The listening never stops, unless {@link #stop()} is used
     * Listener may be restarted after stopping
     *
     * Calling start multiple times has no effect - only first one is considered
     * It can be called again only after calling {@link #stop()}
     * @param inputStream {@link InputStream} to listen to
     * @throws NullPointerException if {@link InputStream} is null
     */
    @Override
    public void start(InputStream inputStream) {
        Objects.requireNonNull(inputStream, "inputStream may not be null");
        if (listener != null) {
            log.info("Already listening, ignoring");
            return;
        }

        listener = executorService.submit(() -> {
            try {
                log.info("Started listening on stream");
                listenForMessages(inputStream);
            } catch (IOException e) {
                log.error("Listening to messages failed!", e);
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Stops current listener by
     * killing the task submitted to executor
     * If listener is currently not started, this method has no effect
     */
    @Override
    public void stop() {
        if (listener != null) {
            listener.cancel(true);
            listener = null;
            log.info("Stopped listening on stream");
        }
    }

    /**
     * Prepares a {@link RPCListener.ResponseCallback}
     * Once a response with message id equal to the id passed to this method comes,
     * the {@link RPCListener.ResponseCallback} will be notified. It will then be removed, meaning it will not
     * be called multiple times.
     *
     * Only single {@link RPCListener.ResponseCallback} is supported per message id. If multiple callbacks are
     * required, consider delegating to them through a single callback.
     * @param id ID of the response to listen to (it should match request id)
     * @param callback {@link ResponseCallback} that should be notified once response arrives
     */
    @Override
    public void listenForResponse(int id, ResponseCallback callback) {
        log.debug("Added listener for id: {}", id);
        if (callback != null) {
            responseCallbacks.put(id, callback);
        }
    }

    /**
     * Prepares a {@link RPCListener.NotificationCallback}
     * It will be notified for any notification that comes through to this listener
     *
     * Since {@link RPCListener.NotificationCallback} will be notified whenever any notification comes,
     * on the same thread that is used for listening, these callbacks should not do heavy work.
     * If heavy work is required, it should be run on a different thread (that is not handled by this class).
     *
     * <b>null</b> is supported and can be used to effectively remove current callback
     *
     * @param callback {@link NotificationCallback} that should be notified when notifications arrive
     */
    @Override
    public void listenForNotifications(NotificationCallback callback) {
        log.debug("Added notification listener");
        this.notificationCallback = callback;
    }

    /**
     * Prepares a {@link RPCListener.RequestCallback}
     * It will be notified for any request that comes through to this listener
     *
     * Neovim requires that requests are handled immediately, but this class does not offer such functionality.
     * Other classes should be used to send a message.
     *
     * Since {@link RPCListener.RequestCallback} will be notified whenever any request comes,
     * on the same thread that is used for listening, these callbacks should not do heavy work.
     * If heavy work is required, it should be run on a different thread (that is not handled by this class).
     *
     * <b>null</b> is supported and can be used to effectively remove current callback
     *
     * @param callback {@link RequestCallback} that should be notified when requests arrive
     */
    @Override
    public void listenForRequests(RequestCallback callback) {
        log.debug("Added request listener");
        this.requestCallback = callback;
    }

    // executes on background thread
    private void listenForMessages(InputStream inputStream) throws IOException {
        ObjectReader objectReader = responseObjectMapper.reader();
        JsonNode readNode;
        while ((readNode = objectReader.readTree(inputStream)) != null) {
            log.debug("Received message: {}", readNode);

            if (!readNode.isArray()
                    || !(readNode instanceof ArrayNode)
                    || readNode.size() < 3
                    || readNode.size() > 4) {
                log.warn("Received a bad message: {}", readNode);
                continue;
            }

            ArrayNode arrayNode = (ArrayNode) readNode;

            MessageType messageType = MessageType.fromInt(arrayNode.get(0).asInt());
            // Pop off the type
            arrayNode.remove(0);

            switch (messageType) {
                case REQUEST:
                    RequestMessage requestMessage = responseObjectMapper.treeToValue(arrayNode, RequestMessage.class);
                    if (requestCallback != null) {
                        log.debug("Notifying request callback with: {}", requestMessage);
                        requestCallback.requestReceived(requestMessage);
                    }
                    break;
                case RESPONSE:
                    ResponseMessage responseMessage = responseObjectMapper.treeToValue(arrayNode, ResponseMessage.class);
                    if (responseCallbacks.containsKey(responseMessage.getId())) {
                        log.debug("Notifying response callback for id({}) with: {}", responseMessage.getId(), responseMessage);
                        responseCallbacks.get(responseMessage.getId()).responseReceived(responseMessage.getId(), responseMessage);
                        responseCallbacks.remove(responseMessage.getId());
                    }
                    break;
                case NOTIFICATION:
                    NotificationMessage notificationMessage = responseObjectMapper.treeToValue(arrayNode, NotificationMessage.class);
                    if (notificationCallback != null) {
                        log.debug("Notifying notification callback with: {}", notificationMessage);
                        notificationCallback.notificationReceived(notificationMessage);
                    }
                    break;
            }
        }
    }
}
