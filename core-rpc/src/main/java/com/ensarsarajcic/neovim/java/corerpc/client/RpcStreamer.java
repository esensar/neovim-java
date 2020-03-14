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
import com.ensarsarajcic.neovim.java.corerpc.message.RequestMessage;

import java.io.IOException;

/**
 * Interface defining a two way RPC communication stream
 * Implementations of this should be used for communication since it covers both input and output
 */
public interface RpcStreamer {
    /**
     * Attaches to given {@link RpcConnection}
     * Connects callbacks to its incoming stream and prepares for writing to outgoing stream
     * @param rpcConnection connection to attach to
     */
    void attach(RpcConnection rpcConnection);

    /**
     * Sends a message to attached {@link RpcConnection}
     *
     * @param message message to send
     * @throws IllegalStateException if current instance is not attached to a {@link RpcConnection}
     * @throws IOException if issues arise in communication or serialization
     */
    void send(Message message) throws IOException;

    /**
     * Specific version of {@link #send(Message)} method for requests
     * It takes a builder instead of a message, to ensure ID is added before sending
     * @param requestMessage {@link RequestMessage.Builder} of message to send
     * @throws IllegalStateException if current instance is not attached to a {@link RpcConnection}
     * @throws IOException if issues arise in communication or serialization
     */
    void send(RequestMessage.Builder requestMessage) throws IOException;

    /**
     * Specific version of {@link #send(Message)} method which can also
     * optionally take a {@link RpcListener.ResponseCallback} to be notified when response comes back
     * It takes a builder instead of a message, to ensure ID is added before sending
     * @param requestMessage {@link RequestMessage.Builder} of message to send
     * @param responseCallback {@link RpcListener.ResponseCallback} to be called when response arrives
     * @throws IllegalStateException if current instance is not attached to a {@link RpcConnection}
     * @throws IOException if issues arise in communication or serialization
     */
    void send(RequestMessage.Builder requestMessage, RpcListener.ResponseCallback responseCallback) throws IOException;

    /**
     * Adds a new {@link RpcListener.RequestCallback}, if it is not already added
     * It will stay attached and receive all requests until {@link #removeRequestCallback(RpcListener.RequestCallback)}
     * is called with exact same callback
     * @param requestCallback {@link RpcListener.RequestCallback} to add
     */
    void addRequestCallback(RpcListener.RequestCallback requestCallback);

    /**
     * Removes a {@link RpcListener.RequestCallback}, if it was added before
     * It does so by a reference, meaning it has to be exact same callback that was added before
     * using {@link #addRequestCallback(RpcListener.RequestCallback)}
     * @param requestCallback {@link RpcListener.RequestCallback} to remove
     */
    void removeRequestCallback(RpcListener.RequestCallback requestCallback);

    /**
     * Adds a new {@link RpcListener.NotificationCallback}, if it is not already added
     * It will stay attached and receive all notifications
     * until {@link #removeNotificationCallback(RpcListener.NotificationCallback)}
     * is called with exact same callback
     * @param notificationCallback {@link RpcListener.NotificationCallback} to add
     */
    void addNotificationCallback(RpcListener.NotificationCallback notificationCallback);

    /**
     * Removes a {@link RpcListener.NotificationCallback}, if it was added before
     * It does so by a reference, meaning it has to be exact same callback that was added before
     * using {@link #addNotificationCallback(RpcListener.NotificationCallback)}
     * @param notificationCallback {@link RpcListener.NotificationCallback} to remove
     */
    void removeNotificationCallback(RpcListener.NotificationCallback notificationCallback);

    /**
     * Stops the underlying {@link RpcListener}
     * It is not expected for implementation to be reusable after calling this method!
     */
    void stop();
}
