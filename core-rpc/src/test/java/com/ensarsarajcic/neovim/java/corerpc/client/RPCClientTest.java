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
import com.ensarsarajcic.neovim.java.corerpc.message.RequestMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RPCClientTest {

    @Mock
    RPCStreamer rpcStreamer;

    @InjectMocks
    RPCClient rpcClient;

    @Test
    public void delegatesToUnderlyingRpcStreamer() throws IOException {
        // Given a rpc client depending on a rpc streamer
        // All operations should be delegated to rpc streamer
        validateDelegates(rpcClient, rpcStreamer);
    }

    @Test
    public void testDefaultFactories() {
        // Create should create new instances
        RPCClient rpc1 = RPCClient.createDefaultAsyncInstance();
        RPCClient rpc2 = RPCClient.createDefaultAsyncInstance();

        assertNotEquals(rpc1, rpc2);

        // Get should return same instances
        RPCClient rpc3 = RPCClient.getDefaultAsyncInstance();
        RPCClient rpc4 = RPCClient.getDefaultAsyncInstance();

        assertEquals(rpc3, rpc4);

        // And it is different from the created ones
        assertNotEquals(rpc1, rpc3);
        assertNotEquals(rpc2, rpc3);

        RPCClient rpc5 = RPCClient.createDefaultAsyncInstance();
        assertNotEquals(rpc5, rpc4);
    }

    @Test
    public void testDefaultBuilder() {
        // Just build straight away
        RPCClient rpc1 = new RPCClient.Builder().build();
    }

    @Test
    public void testRpcStreamerBuilder() throws IOException {
        // When custom streamer is passed, it should be used
        RPCStreamer rpc1Streamer = Mockito.mock(RPCStreamer.class);
        RPCClient rpc1 = new RPCClient.Builder()
                .withRPCStreamer(rpc1Streamer)
                .build();
        validateDelegates(rpc1, rpc1Streamer);
    }

    @Test
    public void testObjectMapperBuilder() {
        // Use custom mapper / executor service
        ObjectMapper customObjectMapper = Mockito.mock(ObjectMapper.class);
        ExecutorService executorService = Mockito.mock(ExecutorService.class);
        RPCClient rpc1 = new RPCClient.Builder()
                .withObjectMapper(customObjectMapper)
                .withExecutorService(executorService)
                .build();
    }

    @Test
    public void testCustomRpcComponentsBuilder() {
        // Use custom mapper / executor service
        ObjectMapper customObjectMapper = Mockito.mock(ObjectMapper.class);
        ExecutorService executorService = Mockito.mock(ExecutorService.class);
        RPCSender rpcSender = Mockito.mock(RPCSender.class);
        RPCListener rpcListener = Mockito.mock(RPCListener.class);

        // Building with just one custom component
        // RPC Sender
        // Sender can be changed later
        RPCClient rpc1 = new RPCClient.Builder()
                .withRPCSender(rpcSender)
                .withObjectMapper(customObjectMapper)
                .withExecutorService(executorService)
                .withRPCSender(rpcSender)
                .build();

        RPCClient rpc2 = new RPCClient.Builder()
                .withRPCSender(rpcSender)
                .build();

        // RPC Listener
        // Listener can be changed later
        RPCClient rpc3 = new RPCClient.Builder()
                .withRPCListener(rpcListener)
                .withObjectMapper(customObjectMapper)
                .withExecutorService(executorService)
                .withRPCListener(rpcListener)
                .build();

        RPCClient rpc4 = new RPCClient.Builder()
                .withRPCListener(rpcListener)
                .build();

        // With both
        RPCClient rpc5 = new RPCClient.Builder()
                .withRPCListener(rpcListener)
                .withRPCSender(rpcSender)
                .build();

        RPCClient rpc6 = new RPCClient.Builder()
                .withRPCSender(rpcSender)
                .withRPCListener(rpcListener)
                .build();

        // Components can be changed later
        RPCClient rpc7 = new RPCClient.Builder()
                .withRPCSenderAndListener(rpcSender, rpcListener)
                .withRPCSender(rpcSender)
                .withRPCListener(rpcListener)
                .build();
    }

    private void validateDelegates(RPCClient rpcClient, RPCStreamer rpcStreamer) throws IOException {
        RPCConnection rpcConnection = Mockito.mock(RPCConnection.class);
        rpcClient.attach(rpcConnection);
        verify(rpcStreamer).attach(rpcConnection);

        Message message = () -> null;
        rpcClient.send(message);
        verify(rpcStreamer).send(message);

        RequestMessage.Builder msgBuilder = new RequestMessage.Builder("test");
        rpcClient.send(msgBuilder);
        verify(rpcStreamer).send(msgBuilder);

        RPCListener.ResponseCallback responseCallback = Mockito.mock(RPCListener.ResponseCallback.class);
        rpcClient.send(msgBuilder, responseCallback);
        verify(rpcStreamer).send(msgBuilder, responseCallback);

        RPCListener.RequestCallback requestCallback = Mockito.mock(RPCListener.RequestCallback.class);
        RPCListener.NotificationCallback notificationCallback = Mockito.mock(RPCListener.NotificationCallback.class);
        rpcClient.addRequestCallback(requestCallback);
        rpcClient.removeRequestCallback(requestCallback);
        rpcClient.addNotificationCallback(notificationCallback);
        rpcClient.removeNotificationCallback(notificationCallback);
        verify(rpcStreamer).addRequestCallback(requestCallback);
        verify(rpcStreamer).removeRequestCallback(requestCallback);
        verify(rpcStreamer).addNotificationCallback(notificationCallback);
        verify(rpcStreamer).removeNotificationCallback(notificationCallback);
    }
}