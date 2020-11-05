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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Objects;

/**
 * Simple implementation of {@link RpcConnection} based on a TCP {@link Socket}
 * <p>
 * This allows connection and communication via TCP socket
 * It is a very simple implementation and it just passes down calls to underlying {@link Socket}
 * <p>
 * Example:
 * <pre>
 *     {@code
 *     Socket socket = new Socket("127.0.0.1", 1234);
 *
 *     RpcConnection localConnection = new TcpSocketRpcConnection(socket);
 *
 *     // It can now be used for communication
 *     rpcStreamer.attach(localConnection);
 *     rpcStreamer.sent(message); // send a message to local app on port 1234
 *     }
 * </pre>
 */
public final class TcpSocketRpcConnection implements RpcConnection {
    public static final Logger log = LoggerFactory.getLogger(TcpSocketRpcConnection.class);

    private Socket socket;

    /**
     * Creates a new {@link TcpSocketRpcConnection} based on passed {@link Socket}
     * It uses input and output streams of given {@link Socket} to communicate
     *
     * @param socket instance of {@link Socket} to use for communication
     * @throws NullPointerException if socket is null
     */
    public TcpSocketRpcConnection(Socket socket) {
        Objects.requireNonNull(socket, "socket is required to properly implement a RpcConnection");
        this.socket = socket;
    }

    /**
     * Gets the {@link InputStream} of the underlying {@link Socket}
     *
     * @return {@link InputStream} of the underlying {@link Socket}
     * @throws RuntimeException if underlying socket throws {@link IOException}
     */
    @Override
    public InputStream getIncomingStream() {
        try {
            return socket.getInputStream();
        } catch (IOException e) {
            log.error("Failed to get incoming stream", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the {@link OutputStream} of the underlying {@link Socket}
     *
     * @return {@link OutputStream} of the underlying {@link Socket}
     * @throws RuntimeException if underlying socket throws {@link IOException}
     */
    @Override
    public OutputStream getOutgoingStream() {
        try {
            return socket.getOutputStream();
        } catch (IOException e) {
            log.error("Failed to get outgoing stream", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Closes underlying {@link Socket}
     * Communication is no longer possible after this call
     *
     * @throws IOException when underlying socket throws {@link IOException}
     */
    @Override
    public void close() throws IOException {
        log.info("Closing socket: {}", socket);
        socket.close();
    }

    @Override
    public String toString() {
        return "TcpSocketRpcConnection{"
                + "socket=" + socket + '}';
    }
}
