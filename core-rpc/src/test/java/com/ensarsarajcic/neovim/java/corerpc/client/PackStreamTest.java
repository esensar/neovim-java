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

import com.ensarsarajcic.neovim.java.corerpc.message.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PackStreamTest {

    @Mock
    RPCListener rpcListener;

    @Mock
    RPCSender rpcSender;

    @Mock
    MessageIdGenerator messageIdGenerator;

    @InjectMocks
    PackStream packStream;

    @Mock
    InputStream inputStream;

    @Mock
    OutputStream outputStream;

    private RPCConnection connection;

    private ArgumentCaptor<RPCListener.RequestCallback> packStreamRequestCallback;
    private ArgumentCaptor<RPCListener.NotificationCallback> packStreamNotificationCallback;

    @Before
    public void setUp() throws Exception {
        prepareListeners();
        connection = new RPCConnection() {
            @Override
            public InputStream getIncomingStream() {
                return inputStream;
            }

            @Override
            public OutputStream getOutgoingStream() {
                return outputStream;
            }
        };
    }

    @Test
    public void testAttach() {
        // When attach is called
        packStream.attach(connection);

        // Rpc listener and sender should be prepared
        verify(rpcListener).listenForRequests(any());
        verify(rpcListener).listenForNotifications(any());
        verify(rpcListener).start(inputStream);
        verify(rpcSender).attach(outputStream);
    }

    @Test
    public void testSend() throws IOException {
        // When send is called
        Message message = () -> null;
        packStream.send(message);

        // Rpc sender should be used
        verify(rpcSender).send(message);
    }

    @Test
    public void testSendRequest() throws IOException {
        // Given a proper message id generator
        given(messageIdGenerator.nextId()).willReturn(25);
        // When send is called
        RequestMessage.Builder message = new RequestMessage.Builder("test");
        packStream.send(message);

        // Rpc sender should be used
        ArgumentCaptor<RequestMessage> argumentCaptor = ArgumentCaptor.forClass(RequestMessage.class);
        verify(rpcSender).send(argumentCaptor.capture());
        assertEquals("test", argumentCaptor.getValue().getMethod());
        // And id for the message should be generated
        verify(messageIdGenerator).nextId();
        // And put into the message
        assertEquals(25, argumentCaptor.getValue().getId());
    }

    @Test
    public void testSendRequestWithCallback() throws IOException {
        // Given a proper message id generator
        given(messageIdGenerator.nextId()).willReturn(25);
        // And callback
        RPCListener.ResponseCallback responseCallback = Mockito.mock(RPCListener.ResponseCallback.class);
        // When send is called
        RequestMessage.Builder message = new RequestMessage.Builder("test");
        packStream.send(message, responseCallback);

        // Rpc sender should be used
        ArgumentCaptor<RequestMessage> argumentCaptor = ArgumentCaptor.forClass(RequestMessage.class);
        verify(rpcSender).send(argumentCaptor.capture());
        assertEquals("test", argumentCaptor.getValue().getMethod());
        // And id for the message should be generated
        verify(messageIdGenerator).nextId();
        // And put into the message
        assertEquals(25, argumentCaptor.getValue().getId());

        // RPC Listener should be used too
        verify(rpcListener).listenForResponse(25, responseCallback);
    }

    @Test
    public void testSendRequestCompletable() throws IOException {
        // Given a proper message id generator and response
        given(messageIdGenerator.nextId()).willReturn(25);
        RequestMessage.Builder message = new RequestMessage.Builder("test");
        ResponseMessage preparedResponse = new ResponseMessage.Builder("test").withId(25).build();
        doAnswer(invocationOnMock -> {
            RPCListener.ResponseCallback responseCallback = (RPCListener.ResponseCallback) invocationOnMock.getArguments()[1];
            responseCallback.responseReceived(25, preparedResponse);
            return null;
        }).when(rpcListener).listenForResponse(anyInt(), any());

        CountDownLatch countDownLatch = new CountDownLatch(1);

        // When response is requested
        packStream.response(message).thenAccept(responseMessage -> {
            try {
                assertTrue(countDownLatch.await(100, TimeUnit.MILLISECONDS));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // Ensure it does not block
        countDownLatch.countDown();

        // Rpc sender should be used
        ArgumentCaptor<RequestMessage> argumentCaptor = ArgumentCaptor.forClass(RequestMessage.class);
        verify(rpcSender).send(argumentCaptor.capture());
        assertEquals("test", argumentCaptor.getValue().getMethod());
        // And id for the message should be generated
        verify(messageIdGenerator).nextId();
        // And put into the message
        assertEquals(25, argumentCaptor.getValue().getId());

        // RPC Listener should be used too
        verify(rpcListener).listenForResponse(eq(25), any());
    }

    @Test
    public void testRequestCallback() throws IOException {
        // Given a proper rpc listener and attached pack stream
        packStream.attach(connection);

        // When request callback is added
        RPCListener.RequestCallback firstCallback = Mockito.mock(RPCListener.RequestCallback.class);
        packStream.addRequestCallback(firstCallback);

        // It should receive requests
        RequestMessage msg1 = new RequestMessage.Builder("test").build();
        packStreamRequestCallback.getValue().requestReceived(msg1);
        verify(firstCallback).requestReceived(msg1);

        // Multiple callbacks should be supported too
        RPCListener.RequestCallback secondCallback = Mockito.mock(RPCListener.RequestCallback.class);
        packStream.addRequestCallback(secondCallback);

        // Both should receive messages
        RequestMessage msg2 = new RequestMessage.Builder("test2").build();
        packStreamRequestCallback.getValue().requestReceived(msg2);
        verify(firstCallback).requestReceived(msg2);
        verify(secondCallback).requestReceived(msg2);

        // Removing should be supported
        packStream.removeRequestCallback(firstCallback);

        // Only second should receive message now
        RequestMessage msg3 = new RequestMessage.Builder("test3").build();
        packStreamRequestCallback.getValue().requestReceived(msg3);
        verify(firstCallback, never()).requestReceived(msg3);
        verify(secondCallback).requestReceived(msg3);

        // Multiple removals
        packStream.removeRequestCallback(secondCallback);

        // None should receive message now
        RequestMessage msg4 = new RequestMessage.Builder("test4").build();
        packStreamRequestCallback.getValue().requestReceived(msg4);
        verify(firstCallback, never()).requestReceived(msg4);
        verify(secondCallback, never()).requestReceived(msg4);
    }

    @Test
    public void testNotificationCallback() throws IOException {
        // Given a proper rpc listener and attached pack stream
        packStream.attach(connection);

        // When notification callback is added
        RPCListener.NotificationCallback firstCallback = Mockito.mock(RPCListener.NotificationCallback.class);
        packStream.addNotificationCallback(firstCallback);

        // It should receive requests
        NotificationMessage msg1 = new NotificationMessage.Builder("test").build();
        packStreamNotificationCallback.getValue().notificationReceived(msg1);
        verify(firstCallback).notificationReceived(msg1);

        // Multiple callbacks should be supported too
        RPCListener.NotificationCallback secondCallback = Mockito.mock(RPCListener.NotificationCallback.class);
        packStream.addNotificationCallback(secondCallback);

        // Both should receive messages
        NotificationMessage msg2 = new NotificationMessage.Builder("test2").build();
        packStreamNotificationCallback.getValue().notificationReceived(msg2);
        verify(firstCallback).notificationReceived(msg2);
        verify(secondCallback).notificationReceived(msg2);

        // Removing should be supported
        packStream.removeNotificationCallback(firstCallback);

        // Only second should receive message now
        NotificationMessage msg3 = new NotificationMessage.Builder("test3").build();
        packStreamNotificationCallback.getValue().notificationReceived(msg3);
        verify(firstCallback, never()).notificationReceived(msg3);
        verify(secondCallback).notificationReceived(msg3);

        // Multiple removals
        packStream.removeNotificationCallback(secondCallback);

        // None should receive message now
        NotificationMessage msg4 = new NotificationMessage.Builder("test4").build();
        packStreamNotificationCallback.getValue().notificationReceived(msg4);
        verify(firstCallback, never()).notificationReceived(msg4);
        verify(secondCallback, never()).notificationReceived(msg4);
    }

    @Test(expected = NullPointerException.class)
    public void noNullRpcListener() {
        // When null rpc listener is passed, constructor should throw an exception
        try {
            new PackStream(rpcSender, null);
            fail("Constructor did not throw an exception");
        } catch (NullPointerException ex) {
            // pass
        }

        new PackStream(rpcSender, null, messageIdGenerator);
    }

    @Test(expected = NullPointerException.class)
    public void noNullRpcSender() {
        // When null rpc sender is passed, constructor should throw an exception
        try {
            new PackStream(null, rpcListener);
            fail("Constructor did not throw an exception");
        } catch (NullPointerException ex) {
            // pass
        }

        new PackStream(null, rpcListener, messageIdGenerator);
    }

    @Test(expected = NullPointerException.class)
    public void noNullMessageIdGenerator() {
        // When null message id generator is passed, constructor should throw an exception
        new PackStream(rpcSender, rpcListener, null);
    }

    private void prepareListeners() {
        packStreamNotificationCallback = ArgumentCaptor.forClass(RPCListener.NotificationCallback.class);
        doNothing().when(rpcListener).listenForNotifications(packStreamNotificationCallback.capture());

        packStreamRequestCallback = ArgumentCaptor.forClass(RPCListener.RequestCallback.class);
        doNothing().when(rpcListener).listenForRequests(packStreamRequestCallback.capture());
    }
}