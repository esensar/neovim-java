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
import java.util.Objects;

/**
 * Implementation of {@link RpcConnection} providing streams of a {@link Process}
 * <p>
 * This allows a connection to another process and communication with it via Rpc
 * It is a very simple implementation which can optionally kill the process once connection is closed
 * <p>
 * Example:
 * <pre>
 *     {@code
 *     ProcessBuilder pb = new ProcessBuilder("nvim", "--embed");
 *     Process neovim = pb.start();
 *
 *     RpcConnection neovimConnection = new ProcessRpcConnection(neovim, true); // true to kill neovim once connection is closed
 *
 *     // This can now be used for communication
 *     rpcStreamer.attach(neovimConnection);
 *     rpcStreamer.send(message); // sends a message to the embedded neovim instance
 *     }
 * </pre>
 */
public final class ProcessRpcConnection implements RpcConnection {
    public static final Logger log = LoggerFactory.getLogger(ProcessRpcConnection.class);

    private Process process;
    private boolean killProcessOnClose;

    /**
     * Creates a new {@link ProcessRpcConnection} based on a {@link Process}'s input and output streams
     * By default does not kill process when connection is closed
     *
     * @param process instance of {@link Process} to connect to
     * @throws NullPointerException if process is null
     */
    public ProcessRpcConnection(Process process) {
        this(process, false);
    }

    /**
     * Creates a new {@link ProcessRpcConnection} based on a {@link Process}'s input and output streams
     *
     * @param process            instance of {@link Process} to connect to
     * @param killProcessOnClose true if process should be destroyed when connection is closed
     * @throws NullPointerException if process is null
     */
    public ProcessRpcConnection(Process process, boolean killProcessOnClose) {
        Objects.requireNonNull(process, "process is required to properly implement a RpcConnection");
        this.process = process;
        this.killProcessOnClose = killProcessOnClose;
    }

    /**
     * Provides input stream of underlying process
     *
     * @return {@link InputStream} of the underlying process
     */
    @Override
    public InputStream getIncomingStream() {
        return process.getInputStream();
    }

    /**
     * Provides ouput stream of underlying process
     *
     * @return {@link OutputStream} of the underlying process
     */
    @Override
    public OutputStream getOutgoingStream() {
        return process.getOutputStream();
    }

    /**
     * Closes connection and optionally kills the underlying process if {@link #killProcessOnClose} is true
     * If {@link #killProcessOnClose} is true, communication is no longer possible
     * Otherwise, communication may proceed, because it is a no-op in that case
     *
     * @throws IOException - never
     */
    @Override
    public void close() throws IOException {
        log.info("Closing process connection. Killing process = {}", killProcessOnClose);
        if (killProcessOnClose) {
            process.destroy();
        }
    }

    @Override
    public String toString() {
        return "ProcessRpcConnection{" +
                "process=" + process +
                ", killProcessOnClose=" + killProcessOnClose +
                '}';
    }
}
