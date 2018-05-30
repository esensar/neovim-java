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
public interface RPCStreamer {
    /**
     * Attaches to given {@link RPCConnection}
     * Connects callbacks to its incoming stream and prepares for writing to outgoing stream
     * @param rpcConnection connection to attach to
     */
    void attach(RPCConnection rpcConnection);

    /**
     * Sends a message to attached {@link RPCConnection}
     *
     * @param message message to send
     * @throws IllegalStateException if current instance is not attached to a {@link RPCConnection}
     * @throws IOException if issues arise in communication or serialization
     */
    void send(Message message) throws IOException;

    /**
     * Specific version of {@link #send(Message)} method for requests
     * It takes a builder instead of a message, to ensure ID is added before sending
     * @param requestMessage {@link RequestMessage.Builder} of message to send
     * @throws IllegalStateException if current instance is not attached to a {@link RPCConnection}
     * @throws IOException if issues arise in communication or serialization
     */
    void send(RequestMessage.Builder requestMessage) throws IOException;

    /**
     * Specific version of {@link #send(Message)} method which can also
     * optionally take a {@link RPCListener.ResponseCallback} to be notified when response comes back
     * It takes a builder instead of a message, to ensure ID is added before sending
     * @param requestMessage {@link RequestMessage.Builder} of message to send
     * @param responseCallback {@link RPCListener.ResponseCallback} to be called when response arrives
     * @throws IllegalStateException if current instance is not attached to a {@link RPCConnection}
     * @throws IOException if issues arise in communication or serialization
     */
    void send(RequestMessage.Builder requestMessage, RPCListener.ResponseCallback responseCallback) throws IOException;

    /**
     * Adds a new {@link RPCListener.RequestCallback}, if it is not already added
     * It will stay attached and receive all requests until {@link #removeRequestCallback(RPCListener.RequestCallback)}
     * is called with exact same callback
     * @param requestCallback {@link RPCListener.RequestCallback} to add
     */
    void addRequestCallback(RPCListener.RequestCallback requestCallback);

    /**
     * Removes a {@link RPCListener.RequestCallback}, if it was added before
     * It does so by a reference, meaning it has to be exact same callback that was added before
     * using {@link #addRequestCallback(RPCListener.RequestCallback)}
     * @param requestCallback {@link RPCListener.RequestCallback} to remove
     */
    void removeRequestCallback(RPCListener.RequestCallback requestCallback);

    /**
     * Adds a new {@link RPCListener.NotificationCallback}, if it is not already added
     * It will stay attached and receive all notifications
     * until {@link #removeNotificationCallback(RPCListener.NotificationCallback)}
     * is called with exact same callback
     * @param notificationCallback {@link RPCListener.NotificationCallback} to add
     */
    void addNotificationCallback(RPCListener.NotificationCallback notificationCallback);

    /**
     * Removes a {@link RPCListener.NotificationCallback}, if it was added before
     * It does so by a reference, meaning it has to be exact same callback that was added before
     * using {@link #addNotificationCallback(RPCListener.NotificationCallback)}
     * @param notificationCallback {@link RPCListener.NotificationCallback} to remove
     */
    void removeNotificationCallback(RPCListener.NotificationCallback notificationCallback);
}
