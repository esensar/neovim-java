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

import java.io.IOException;
import java.io.OutputStream;

/**
 * Represents a RPC communication sender (writer)
 * It should provide interface for sending messages
 * Message sending should occur on a separate thread
 */
public interface RPCSender {
    /**
     * Sends a message to attached {@link OutputStream}
     * Implementations need to implement it according to interface (requiring attachment prior to communication)
     *
     * @param message message to send
     * @throws IllegalStateException if current instance is not attached to a {@link OutputStream}
     * @throws IOException if issues arise in communication or serialization
     */
    void send(Message message) throws IOException;

    /**
     * Attaches this {@link RPCSender} to a {@link OutputStream}
     * That {@link OutputStream} can (and should) then be used to communicate (for sending data)
     * @param outputStream {@link OutputStream} to write to
     */
    void attach(OutputStream outputStream);

    /**
     * Stops the sender
     */
    void stop();
}
