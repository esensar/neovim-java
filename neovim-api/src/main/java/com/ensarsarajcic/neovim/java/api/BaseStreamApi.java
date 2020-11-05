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

package com.ensarsarajcic.neovim.java.api;

import com.ensarsarajcic.neovim.java.api.types.msgpack.BaseCustomIdType;
import com.ensarsarajcic.neovim.java.api.types.msgpack.NeovimTypeDeserializer;
import com.ensarsarajcic.neovim.java.api.util.ObjectMappers;
import com.ensarsarajcic.neovim.java.corerpc.message.RequestMessage;
import com.ensarsarajcic.neovim.java.corerpc.message.ResponseMessage;
import com.ensarsarajcic.neovim.java.corerpc.reactive.ReactiveRpcStreamer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

/**
 * Base class for implementation of stream based {@link NeovimApiClient} classes
 * Provides convenience methods for sending and parsing messages
 */
public abstract class BaseStreamApi {
    private static final Logger log = LoggerFactory.getLogger(NeovimTypeDeserializer.class);

    protected ReactiveRpcStreamer reactiveRpcStreamer;
    protected ObjectMapper objectMapper;

    public BaseStreamApi(ReactiveRpcStreamer reactiveRpcStreamer) {
        Objects.requireNonNull(reactiveRpcStreamer, "reactiveRpcStreamer is required for stream API");
        this.reactiveRpcStreamer = reactiveRpcStreamer;
        this.objectMapper = ObjectMappers.defaultNeovimMapper();
    }

    protected <T> CompletableFuture<T> sendWithResponseOfType(RequestMessage.Builder request, Class<T> type) {
        return reactiveRpcStreamer.response(request)
                .thenApply(ResponseMessage::getResult)
                .thenApply(o -> objectMapper.convertValue(o, type));
    }

    protected CompletableFuture<byte[]> sendWithBytesResponse(RequestMessage.Builder request) {
        return reactiveRpcStreamer.response(request)
                .thenApply(ResponseMessage::getResult)
                .thenApply(o -> {
                    try {
                        return objectMapper.writeValueAsBytes(o);
                    } catch (JsonProcessingException e) {
                        log.error("Failed to convert response to bytes!", e);
                        e.printStackTrace();
                        throw new CompletionException(e);
                    }
                });
    }

    protected <T extends BaseCustomIdType> CompletableFuture<T> sendWithResponseOfMsgPackType(RequestMessage.Builder request, Class<T> type) {
        return sendWithBytesResponse(request).thenApply(bytes -> {
            try {
                return objectMapper.readerFor(type).readValue(bytes);
            } catch (IOException e) {
                log.error("Failed to read bytes as " + type, e);
                e.printStackTrace();
                throw new CompletionException(e);
            }
        });
    }

    protected <T extends BaseCustomIdType> CompletableFuture<List<T>> sendWithResponseOfListOfMsgPackType(RequestMessage.Builder request, Class<T> type) {
        return sendWithBytesResponse(request).thenApply(bytes -> {
            try {
                return objectMapper.readerFor(
                        objectMapper.getTypeFactory().constructCollectionType(List.class, type)
                ).readValue(bytes);
            } catch (IOException e) {
                log.error("Failed to construct a list of " + type, e);
                e.printStackTrace();
                throw new CompletionException(e);
            }
        });
    }

    protected <T> CompletableFuture<List<T>> sendWithResponseOfListType(RequestMessage.Builder request, Class<T> type) {
        return sendWithGenericResponse(
                request)
                .thenApply(o -> objectMapper.convertValue(
                        o,
                        objectMapper.getTypeFactory()
                                .constructCollectionType(List.class, type)
                ));
    }

    protected <K, V> CompletableFuture<Map<K, V>> sendWithResponseOfMapType(RequestMessage.Builder request, Class<K> keyType, Class<V> valueType) {
        return sendWithGenericResponse(
                request)
                .thenApply(o -> objectMapper.convertValue(
                        o,
                        objectMapper.getTypeFactory()
                                .constructMapType(Map.class, keyType, valueType)
                ));
    }

    protected CompletableFuture<Object> sendWithGenericResponse(RequestMessage.Builder request) {
        return sendWithResponseOfType(request, Object.class);
    }

    protected CompletableFuture<Void> sendWithNoResponse(RequestMessage.Builder request) {
        return reactiveRpcStreamer.response(request).thenApply(responseMessage -> null);
    }
}
