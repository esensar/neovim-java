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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class TcpSocketRPCConnectionTest {

    @Mock
    Socket socket;

    @Mock
    InputStream inputStream;
    @Mock
    OutputStream outputStream;

    @InjectMocks
    TcpSocketRPCConnection tcpSocketRPCConnection;

    @Before
    public void setUp() throws Exception {
        given(socket.getInputStream()).willReturn(inputStream);
        given(socket.getOutputStream()).willReturn(outputStream);
    }

    @Test
    public void testIncomingStream() {
        assertEquals(inputStream, tcpSocketRPCConnection.getIncomingStream());
    }

    @Test
    public void testOugtoingStream() {
        assertEquals(outputStream, tcpSocketRPCConnection.getOutgoingStream());
    }

    @Test(expected = RuntimeException.class)
    public void testExceptionInOpeningIncomingStream() throws IOException {
        given(socket.getInputStream()).willThrow(new IOException());

        // When incoming stream is requested, crash the app
        tcpSocketRPCConnection.getIncomingStream();
    }

    @Test(expected = RuntimeException.class)
    public void testExceptionInOpeningOutgoingStream() throws IOException {
        given(socket.getOutputStream()).willThrow(new IOException());

        // When incoming stream is requested, crash the app
        tcpSocketRPCConnection.getOutgoingStream();
    }
}