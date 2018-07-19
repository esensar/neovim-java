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
 * Implementation of {@link RPCListener} utilizing {@link ExecutorService} for background work
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
     */
    public BackgroundRPCListener(ExecutorService executorService, ObjectMapper responseObjectMapper) {
        Objects.requireNonNull(executorService, "executorService must be provided to enable background work");
        Objects.requireNonNull(responseObjectMapper, "responseObjectMapper must be provided to deserialize");
        this.executorService = executorService;
        this.responseObjectMapper = responseObjectMapper;
    }

    /**
     * Starts listening on given input stream on background thread (using given executor service)
     * @param inputStream {@link InputStream} to listen to
     */
    @Override
    public void start(InputStream inputStream) {
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
     * Stops current listener by killing the task submitted to executor
     */
    @Override
    public void stop() {
        log.info("Stopped listening on stream");
        if (listener != null) {
            listener.cancel(true);
        }
    }

    @Override
    public void listenForResponse(int id, ResponseCallback callback) {
        log.debug("Added listener for id: {}", id);
        if (callback != null) {
            responseCallbacks.put(id, callback);
        }
    }

    @Override
    public void listenForNotifications(NotificationCallback callback) {
        log.debug("Added notification listener");
        this.notificationCallback = callback;
    }

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
