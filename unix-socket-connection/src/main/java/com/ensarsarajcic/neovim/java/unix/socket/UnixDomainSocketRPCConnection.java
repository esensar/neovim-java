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

package com.ensarsarajcic.neovim.java.unix.socket;

import com.ensarsarajcic.neovim.java.corerpc.client.RPCConnection;
import org.scalasbt.ipcsocket.UnixDomainSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Objects;

public final class UnixDomainSocketRPCConnection implements RPCConnection {
    private static final Logger log = LoggerFactory.getLogger(UnixDomainSocketRPCConnection.class);

    private UnixDomainSocket unixDomainSocket;

    public UnixDomainSocketRPCConnection(File path) {
        Objects.requireNonNull(path, "path is required to make connection");
        try {
            this.unixDomainSocket = new UnixDomainSocket(path.getPath());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public InputStream getIncomingStream() {
        return unixDomainSocket.getInputStream();
    }

    @Override
    public OutputStream getOutgoingStream() {
        return unixDomainSocket.getOutputStream();
    }

    @Override
    public void close() throws IOException {
        log.info("Closing unix domain socket {}", unixDomainSocket);
        unixDomainSocket.close();
    }

    @Override
    public String toString() {
        return "UnixDomainSocketRPCConnection{" +
                "unixDomainSocket=" + unixDomainSocket +
                '}';
    }
}
