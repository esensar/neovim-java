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

import com.ensarsarajcic.neovim.java.corerpc.message.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

/**
 * Implementation of {@link RPCSender} utilizing
 * {@link ExecutorService} for asynchronous work
 * <p>
 * Messages are sent using the {@link ExecutorService}, meaning order of execution
 * is not handled by this class
 * <p>
 * Messages are serialized using {@link ObjectMapper} passed in the constructor
 * <p>
 * Prior to using this class, {@link #attach(OutputStream)} must be called in order
 * to pick {@link OutputStream} to write data to
 * {@link #send(Message)} will throw an Exception otherwise
 * <p>
 * Example:
 * <pre>
 *     {@code
 *     ExecutorService executorService = Executors.newSingleThreadExecutor();
 *
 *     // Factory to support msgpack
 *     // Use other classes from the library to avoid having to manually create this
 *     MessagePackFactory factory = new MessagePackFactory();
 *     factory.disable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
 *     factory.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
 *     ObjectMapper objectMapper = new ObjectMapper(factory);
 *
 *     RPCSender sender = new AsyncRPCSender(executorService, objectMapper);
 *     sender.attach(outputStream); // an existing OutputStream
 *     sender.send(message); // fire and forget
 *     }
 * </pre>
 */
public final class AsyncRPCSender implements RPCSender {
    private static final Logger log = LoggerFactory.getLogger(AsyncRPCSender.class);

    private final ExecutorService executorService;
    private final ObjectMapper msgPacker;

    private OutputStream outgoingStream;

    /**
     * Creates a new {@link AsyncRPCSender} with given {@link ObjectMapper} for mapping requests
     * using {@link ExecutorService} for background work
     *
     * @param executorService service used for background work
     * @param msgPacker       {@link ObjectMapper} for mapping requests (outgoing)
     * @throws NullPointerException if any parameter is null
     */
    public AsyncRPCSender(ExecutorService executorService, ObjectMapper msgPacker) {
        Objects.requireNonNull(executorService, "executorService must be provided to enable background work");
        Objects.requireNonNull(msgPacker, "msgPacker must be provided for serialization of messages");
        this.executorService = executorService;
        this.msgPacker = msgPacker;
    }

    /**
     * Sends messages per {@link RPCSender#send(Message)} specification
     * Order of execution is handled by {@link ExecutorService},
     * this class just submits the task of actual serializing and writing to stream
     *
     * @throws IllegalStateException thrown if {@link #attach(OutputStream)} was not used - thrown by the submitted task
     */
    @Override
    public void send(Message message) {
        this.executorService.submit(() -> sendMessage(message));
    }

    /**
     * Attaches to {@link OutputStream}
     * Required for using {@link #send(Message)}
     *
     * @param outputStream {@link OutputStream} to write to
     */
    @Override
    public void attach(OutputStream outputStream) {
        Objects.requireNonNull(outputStream, "outputStream may not be null");
        log.info("Attached to output stream!");
        this.outgoingStream = outputStream;
    }

    /**
     * Stops the {@link ExecutorService}.
     */
    @Override
    public void stop() {
        this.executorService.shutdown();
    }

    private void sendMessage(Message message) {
        if (this.outgoingStream == null) {
            throw new IllegalStateException("Can't find a connection to send message to. Did you forget to call attach?");
        }

        try {
            log.info("Sending message: {}", message);
            msgPacker.writer().writeValue(outgoingStream, message);
        } catch (IOException e) {
            log.error("Failed sending message!", e);
            throw new RuntimeException(e);
        }
    }
}
