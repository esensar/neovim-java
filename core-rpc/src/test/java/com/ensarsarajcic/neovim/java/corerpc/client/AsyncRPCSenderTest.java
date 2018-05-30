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
import com.ensarsarajcic.neovim.java.corerpc.message.MessageType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AsyncRPCSenderTest {

    @Mock
    ExecutorService executorService;

    @Mock
    ObjectMapper objectMapper;

    @Mock
    ObjectWriter objectWriter;

    @Mock
    OutputStream outputStream;

    @InjectMocks
    AsyncRPCSender asyncRPCSender;

    @Test(expected = IllegalStateException.class)
    public void cantSendWithoutAttaching() {
        // given no stream attached and proper executor service
        prepareSequentialExecutorService();
        // when send is called
        // exception is thrown
        asyncRPCSender.send(() -> MessageType.REQUEST);
    }

    @Test
    public void testAttach() {
        // when attaching app should not crash
        asyncRPCSender.attach(outputStream);
    }

    @Test
    public void testSend() throws IOException {
        // given an attached stream and proper executor service
        asyncRPCSender.attach(outputStream);
        prepareSequentialExecutorService();
        Message message = () -> MessageType.REQUEST;
        given(objectMapper.writer()).willReturn(objectWriter);
        doNothing().when(objectWriter).writeValue(outputStream, message);
        // when send is called
        asyncRPCSender.send(message);

        // message is passed down to stream
        // writer was requested
        verify(objectMapper).writer();
        // and used
        verify(objectWriter).writeValue(outputStream, message);
    }

    @Test(expected = RuntimeException.class)
    public void testSendError() throws IOException {
        // and a failing write
        asyncRPCSender.attach(outputStream);
        prepareSequentialExecutorService();
        Message message = () -> MessageType.REQUEST;
        given(objectMapper.writer()).willReturn(objectWriter);
        // given an attached stream and proper executor service
        doThrow(new IOException()).when(objectWriter).writeValue(outputStream, message);
        // when send fails
        asyncRPCSender.send(message);

        // message is passed down to stream
        // writer was requested
        verify(objectMapper).writer();
        // and used
        verify(objectWriter).writeValue(outputStream, message);
    }

    @Test(expected = NullPointerException.class)
    public void noNullExecutorService() {
        // when null executor service is passed to constructor, it throws exception
        new AsyncRPCSender(null, objectMapper);
    }

    @Test(expected = NullPointerException.class)
    public void noNullObjectMapper() {
        // when null object mapper is passed to constructor, it throws exception
        new AsyncRPCSender(executorService, null);
    }

    private void prepareSequentialExecutorService() {
        doAnswer(invocationOnMock -> {
            ((Runnable) invocationOnMock.getArguments()[0]).run();
            return null;
        }).when(executorService).submit(any(Runnable.class));
    }
}