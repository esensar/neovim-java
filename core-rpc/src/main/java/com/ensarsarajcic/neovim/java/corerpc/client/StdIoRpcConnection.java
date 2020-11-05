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

/**
 * Implementation of {@link RpcConnection} using stdio streams
 * <p>
 * This is mostly useful to communicate with nvim process that spawned this
 * process, usually as a remote plugin
 * <p>
 * Example:
 * <pre>
 *     {@code
 *     RpcConnection neovimConnection = new StdIoRpcConnection();
 *
 *     // This can now be used for communication
 *     rpcStreamer.attach(neovimConnection);
 *     rpcStreamer.send(message); // sends a message to the parent neovim instance
 *     }
 * </pre>
 */
public final class StdIoRpcConnection implements RpcConnection {
    public static final Logger log = LoggerFactory.getLogger(StdIoRpcConnection.class);

    /**
     * Creates a new {@link StdIoRpcConnection} based on system stdio
     */
    public StdIoRpcConnection() {
    }

    @Override
    public InputStream getIncomingStream() {
        return System.in;
    }

    @Override
    public OutputStream getOutgoingStream() {
        return System.out;
    }

    @Override
    public void close() throws IOException {
        log.info("Closing stdio connection.");
    }

    @Override
    public String toString() {
        return "StdIoRpcConnection{}";
    }
}
