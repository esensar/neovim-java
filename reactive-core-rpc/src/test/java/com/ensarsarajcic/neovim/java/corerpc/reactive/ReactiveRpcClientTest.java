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
import com.ensarsarajcic.neovim.java.corerpc.client.RpcStreamer;
import com.ensarsarajcic.neovim.java.corerpc.message.RequestMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ReactiveRpcClientTest {

    @Mock
    ReactiveRpcStreamer rpcStreamer;

    @InjectMocks
    ReactiveRpcClient rpcClient;

    @Test
    public void delegatesToUnderlyingRpcStreamer() throws IOException {
        // Given a rpc client depending on a rpc streamer
        // All operations should be delegated to rpc streamer
        validateDelegates(rpcClient, rpcStreamer);
    }

    @Test
    public void testDefaultFactories() {
        // Create should create new instances
        var rpc1 = ReactiveRpcClient.createDefaultInstance();
        var rpc2 = ReactiveRpcClient.createDefaultInstance();

        assertNotEquals(rpc1, rpc2);

        // Get should return same instances
        var rpc3 = ReactiveRpcClient.getDefaultInstance();
        var rpc4 = ReactiveRpcClient.getDefaultInstance();

        assertEquals(rpc3, rpc4);

        // And it is different from the created ones
        assertNotEquals(rpc1, rpc3);
        assertNotEquals(rpc2, rpc3);

        var rpc5 = ReactiveRpcClient.createDefaultInstance();
        assertNotEquals(rpc5, rpc4);
    }

    @Test
    public void testCustomFactories() throws IOException {
        // Create with custom reactive streamer should delegate to custom streamer
        var reactiveRpcStreamer = Mockito.mock(ReactiveRpcStreamer.class);
        var rpc1 = ReactiveRpcClient.createInstanceWithCustomReactiveStreamer(reactiveRpcStreamer);
        validateDelegates(rpc1, reactiveRpcStreamer);
        // And should not duplicate instances
        var rpc2 = ReactiveRpcClient.createInstanceWithCustomReactiveStreamer(reactiveRpcStreamer);

        assertNotEquals(rpc1, rpc2);

        // Create with custom streamer should use wrapper and delegate down to the streamer
        var rpcStreamer = Mockito.mock(RpcStreamer.class);
        var rpc3 = ReactiveRpcClient.createDefaultInstanceWithCustomStreamer(rpcStreamer);

        // And should not duplicate
        var rpc4 = ReactiveRpcClient.createDefaultInstanceWithCustomStreamer(rpcStreamer);

        assertNotEquals(rpc3, rpc4);
    }

    private void validateDelegates(ReactiveRpcClient rpcClient, ReactiveRpcStreamer rpcStreamer) throws IOException {
        var rpcConnection = Mockito.mock(RpcConnection.class);
        rpcClient.attach(rpcConnection);
        verify(rpcStreamer).attach(rpcConnection);

        var requestMessageBuilder = new RequestMessage.Builder("test");
        rpcClient.response(requestMessageBuilder);
        verify(rpcStreamer).response(requestMessageBuilder);

        rpcClient.requestsFlow();
        verify(rpcStreamer).requestsFlow();
        rpcClient.notificationsFlow();
        verify(rpcStreamer).notificationsFlow();
    }
}