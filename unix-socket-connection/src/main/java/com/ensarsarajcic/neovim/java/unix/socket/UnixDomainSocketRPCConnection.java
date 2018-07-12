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
import jnr.unixsocket.UnixSocketAddress;
import jnr.unixsocket.UnixSocketChannel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.util.Objects;

public final class UnixDomainSocketRPCConnection implements RPCConnection {

    private File path;
    private UnixSocketChannel unixSocketChannel;

    public UnixDomainSocketRPCConnection(File path) {
        Objects.requireNonNull(path, "path is required to make connection");
        this.path = path;
    }

    private UnixSocketChannel getChannel() {
        if (unixSocketChannel == null) {
            synchronized (this) {
                if (unixSocketChannel == null) {
                    try {
                        unixSocketChannel = UnixSocketChannel.open(new UnixSocketAddress(path));
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return unixSocketChannel;
    }

    @Override
    public InputStream getIncomingStream() {
        return Channels.newInputStream(getChannel());
    }

    @Override
    public OutputStream getOutgoingStream() {
        return Channels.newOutputStream(getChannel());
    }

    @Override
    public void close() throws IOException {
        if (unixSocketChannel != null) {
            unixSocketChannel.close();
        }
    }
}
