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

package com.ensarsarajcic.neovim.java.corerpc.reactive;

import com.ensarsarajcic.neovim.java.corerpc.client.RpcConnection;
import com.ensarsarajcic.neovim.java.corerpc.client.RpcListener;
import com.ensarsarajcic.neovim.java.corerpc.client.RpcStreamer;
import com.ensarsarajcic.neovim.java.corerpc.message.NotificationMessage;
import com.ensarsarajcic.neovim.java.corerpc.message.RequestMessage;
import com.ensarsarajcic.neovim.java.corerpc.message.ResponseMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Flow;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ReactiveRpcStreamerWrapperTest {
    @Mock
    RpcStreamer rpcStreamer;

    @InjectMocks
    ReactiveRpcStreamerWrapper reactiveRpcStreamerWrapper;

    @Mock
    InputStream inputStream;

    @Mock
    OutputStream outputStream;

    private ArgumentCaptor<RpcListener.RequestCallback> requestCallbackArgumentCaptor = ArgumentCaptor.forClass(RpcListener.RequestCallback.class);
    private ArgumentCaptor<RpcListener.NotificationCallback> notificationCallbackArgumentCaptor = ArgumentCaptor.forClass(RpcListener.NotificationCallback.class);

    private RpcConnection connection;

    @Before
    public void setUp() throws Exception {
        connection = new RpcConnection() {
            @Override
            public InputStream getIncomingStream() {
                return inputStream;
            }

            @Override
            public OutputStream getOutgoingStream() {
                return outputStream;
            }

            @Override
            public void close() throws IOException {

            }
        };
    }

    @Test
    public void testDelegation() throws ExecutionException, InterruptedException, IOException {
        reactiveRpcStreamerWrapper.attach(connection);
        verify(rpcStreamer).attach(connection);
        verifyAndAttachCallbacks();

        doAnswer(invocationOnMock -> {
            ((RpcListener.ResponseCallback) invocationOnMock.getArguments()[1]).responseReceived(1, new ResponseMessage.Builder("test").build());
            return null;
        }).when(rpcStreamer).send(any(), any());
        reactiveRpcStreamerWrapper.response(new RequestMessage.Builder("test")).get();
        verify(rpcStreamer).send(any(), any());
    }

    @Test
    public void testSendRequestCompletable() throws IOException {
        // Given a proper message id generator and response
        var message = new RequestMessage.Builder("test");
        var preparedResponse = new ResponseMessage.Builder("test").withId(25).build();
        doAnswer(invocationOnMock -> {
            RpcListener.ResponseCallback responseCallback = (RpcListener.ResponseCallback) invocationOnMock.getArguments()[1];
            responseCallback.responseReceived(25, preparedResponse);
            return null;
        }).when(rpcStreamer).send(eq(message), any());

        var countDownLatch = new CountDownLatch(1);

        // When response is requested
        reactiveRpcStreamerWrapper.response(message).thenAccept(responseMessage -> {
            try {
                assertTrue(countDownLatch.await(100, TimeUnit.MILLISECONDS));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // Ensure it does not block
        countDownLatch.countDown();

        // Rpc streamer should be used
        var builderArgumentCaptor = ArgumentCaptor.forClass(RequestMessage.Builder.class);
        verify(rpcStreamer, timeout(100)).send(builderArgumentCaptor.capture(), any());
        assertEquals(message, builderArgumentCaptor.getValue());
    }

    @Test
    public void testRequestFlow() throws IOException {
        // Given a proper rpc listener and attached pack stream
        reactiveRpcStreamerWrapper.attach(connection);
        verifyAndAttachCallbacks();

        // When request flow is subscribed to
        Flow.Subscriber<RequestMessage> requestMessageSubscriber = Mockito.mock(Flow.Subscriber.class);
        doAnswer(invocationOnMock -> {
            Flow.Subscription subscription = (Flow.Subscription) invocationOnMock.getArguments()[0];
            subscription.request(Long.MAX_VALUE);
            return null;
        }).when(requestMessageSubscriber).onSubscribe(any());
        reactiveRpcStreamerWrapper.requestsFlow()
                .subscribe(requestMessageSubscriber);

        // It should receive events when requests arrive
        var msg1 = new RequestMessage.Builder("test").build();
        requestCallbackArgumentCaptor.getValue().requestReceived(msg1);
        verify(requestMessageSubscriber, timeout(100)).onNext(msg1);
    }

    @Test
    public void testNotificationFlow() throws IOException {
        // Given a proper rpc listener and attached pack stream
        reactiveRpcStreamerWrapper.attach(connection);
        verifyAndAttachCallbacks();

        // When request flow is subscribed to
        Flow.Subscriber<NotificationMessage> notificationMessageSubscriber = Mockito.mock(Flow.Subscriber.class);
        doAnswer(invocationOnMock -> {
            Flow.Subscription subscription = (Flow.Subscription) invocationOnMock.getArguments()[0];
            subscription.request(Long.MAX_VALUE);
            return null;
        }).when(notificationMessageSubscriber).onSubscribe(any());
        reactiveRpcStreamerWrapper.notificationsFlow()
                .subscribe(notificationMessageSubscriber);

        // It should receive events when notifications arrive
        var msg1 = new NotificationMessage.Builder("test").build();
        notificationCallbackArgumentCaptor.getValue().notificationReceived(msg1);
        verify(notificationMessageSubscriber, timeout(100)).onNext(msg1);
    }

    private void verifyAndAttachCallbacks() {
        requestCallbackArgumentCaptor = ArgumentCaptor.forClass(RpcListener.RequestCallback.class);
        notificationCallbackArgumentCaptor = ArgumentCaptor.forClass(RpcListener.NotificationCallback.class);
        verify(rpcStreamer).addRequestCallback(requestCallbackArgumentCaptor.capture());
        verify(rpcStreamer).addNotificationCallback(notificationCallbackArgumentCaptor.capture());
    }
}