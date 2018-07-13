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

public final class ProcessRPCConnection implements RPCConnection {
    public static final Logger log = LoggerFactory.getLogger(ProcessRPCConnection.class);

    private Process process;
    private boolean killProcessOnClose;

    /**
     * Creates a new {@link ProcessRPCConnection} based on a {@link Process}'s input and output streams
     * By default does not kill process when connection is closed
     * @param process instance of {@link Process} to connect to
     */
    public ProcessRPCConnection(Process process) {
        this(process, false);
    }

    /**
     * Creates a new {@link ProcessRPCConnection} based on a {@link Process}'s input and output streams
     * @param process instance of {@link Process} to connect to
     * @param killProcessOnClose true if process should be destroyed when connection is closed
     */
    public ProcessRPCConnection(Process process, boolean killProcessOnClose) {
        Objects.requireNonNull(process, "process is required to properly implement a RPCConnection");
        this.process = process;
        this.killProcessOnClose = killProcessOnClose;
    }

    @Override
    public InputStream getIncomingStream() {
        return process.getInputStream();
    }

    @Override
    public OutputStream getOutgoingStream() {
        return process.getOutputStream();
    }

    @Override
    public void close() throws IOException {
        log.info("Closing process connection. Killing process = {}", killProcessOnClose);
        if (killProcessOnClose) {
            process.destroy();
        }
    }

    @Override
    public String toString() {
        return "ProcessRPCConnection{" +
                "process=" + process +
                ", killProcessOnClose=" + killProcessOnClose +
                '}';
    }
}
