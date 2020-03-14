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
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ProcessRpcConnectionTest {

    @Mock
    Process process;

    @Mock
    InputStream inputStream;
    @Mock
    OutputStream outputStream;

    ProcessRpcConnection processRpcConnection;

    @Before
    public void setUp() throws Exception {
        processRpcConnection = new ProcessRpcConnection(process);
        given(process.getInputStream()).willReturn(inputStream);
        given(process.getOutputStream()).willReturn(outputStream);
    }

    @Test
    public void testIncomingStream() {
        assertEquals(inputStream, processRpcConnection.getIncomingStream());
    }

    @Test
    public void testOugtoingStream() {
        assertEquals(outputStream, processRpcConnection.getOutgoingStream());
    }

    @Test
    public void testClose() throws IOException {
        var connection = new ProcessRpcConnection(process);
        connection.close();
        verify(process, never()).destroy();

        var closingConnection = new ProcessRpcConnection(process, true);
        closingConnection.close();
        verify(process).destroy();

        var newProcess = Mockito.mock(Process.class);
        try(var autoClosedConnection = new ProcessRpcConnection(newProcess, true)) {
            autoClosedConnection.getIncomingStream();
        }
        verify(newProcess).destroy();
    }
}