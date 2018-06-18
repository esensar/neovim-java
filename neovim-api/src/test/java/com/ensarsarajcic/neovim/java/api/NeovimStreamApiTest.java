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

package com.ensarsarajcic.neovim.java.api;

import com.ensarsarajcic.neovim.java.corerpc.message.RPCError;
import com.ensarsarajcic.neovim.java.corerpc.message.RequestMessage;
import com.ensarsarajcic.neovim.java.corerpc.message.ResponseMessage;
import com.ensarsarajcic.neovim.java.corerpc.reactive.RPCException;
import com.ensarsarajcic.neovim.java.corerpc.reactive.ReactiveRPCStreamer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class NeovimStreamApiTest {

    @Mock
    ReactiveRPCStreamer reactiveRPCStreamer;

    @InjectMocks
    NeovimStreamApi neovimStreamApi;

    @Test(expected = NullPointerException.class)
    public void cantConstructWithNull() {
        new NeovimStreamApi(null);
    }

    @Test
    public void atomicNotSupported() {
        try {
            neovimStreamApi.prepareAtomic();
            fail("Should have thrown unsupported operation exception");
        } catch(UnsupportedOperationException ex) {

        }
        try {
            neovimStreamApi.sendAtomic(null);
            fail("Should have thrown unsupported operation exception");
        } catch(UnsupportedOperationException ex) {

        }
    }

    @Test
    public void getHightlightByIdTest() throws ExecutionException, InterruptedException {
        // Happy case
        ArgumentCaptor<RequestMessage.Builder> argumentCaptor = prepareArgumentCaptor(
                CompletableFuture.completedFuture(new ResponseMessage(1, null, Map.of())));
        CompletableFuture<Map> result = neovimStreamApi.getHighlightById(1, true);
        RequestMessage requestMessage = argumentCaptor.getValue().build();
        assertMethodAndArguments(
                requestMessage,
                NeovimApi.GET_HIGHLIGHT_BY_ID,
                1,
                true
        );
        verify(reactiveRPCStreamer).response(any());
        assertTrue(result.get().isEmpty());

        // Error case
        assertErrorBehavior(
                () -> neovimStreamApi.getHighlightById(5, false),
                request -> assertMethodAndArguments(request, NeovimApi.GET_HIGHLIGHT_BY_ID, 5, false)
        );
    }

    @Test
    public void getHightlightByNameTest() throws ExecutionException, InterruptedException {
        // Happy case
        ArgumentCaptor<RequestMessage.Builder> argumentCaptor = prepareArgumentCaptor(
                CompletableFuture.completedFuture(new ResponseMessage(1, null, Map.of())));
        CompletableFuture<Map> result = neovimStreamApi.getHighlightByName("name", true);
        RequestMessage requestMessage = argumentCaptor.getValue().build();
        assertMethodAndArguments(
                requestMessage,
                NeovimApi.GET_HIGHLIGHT_BY_NAME,
                "name",
                true
        );
        verify(reactiveRPCStreamer).response(any());
        assertTrue(result.get().isEmpty());

        // Error case
        assertErrorBehavior(
                () -> neovimStreamApi.getHighlightByName("some other name", false),
                request -> assertMethodAndArguments(request, NeovimApi.GET_HIGHLIGHT_BY_NAME, "some other name", false)
        );
    }

    // region Testing helpers
    private void assertErrorBehavior(
            Supplier<CompletableFuture<?>> completableFutureSupplier,
            Consumer<RequestMessage> requestAsserter) throws InterruptedException {
        ArgumentCaptor<RequestMessage.Builder> errorArgumentCaptor = prepareArgumentCaptor(
                CompletableFuture.failedFuture(new RPCException(new RPCError(1, "error"))));
        CompletableFuture errorResult = completableFutureSupplier.get();
        RequestMessage errorResponse = errorArgumentCaptor.getValue().build();
        requestAsserter.accept(errorResponse);
        verify(reactiveRPCStreamer, atLeastOnce()).response(any());
        verifyError(errorResult);
    }

    private void assertMethodAndArguments(RequestMessage message, String method, Object... arguments) {
        assertEquals(message.getMethod(), method);
        int i = 0;
        for (Object arg : arguments) {
            assertEquals(message.getArguments().get(i), arg);
            i++;
        }
    }

    private void verifyError(CompletableFuture completableFuture) throws InterruptedException {
        try {
            completableFuture.get();
            fail("Should have thrown an error");
        } catch (ExecutionException ex) {
            if (!(ex.getCause() instanceof RPCException)) {
                fail("Should have been an RCP Exception");
            }
        }
    }

    private ArgumentCaptor<RequestMessage.Builder> prepareArgumentCaptor(CompletableFuture<ResponseMessage> responseMessageCompletableFuture) {
        ArgumentCaptor<RequestMessage.Builder> argumentCaptor = ArgumentCaptor.forClass(RequestMessage.Builder.class);
        given(reactiveRPCStreamer.response(argumentCaptor.capture())).willReturn(responseMessageCompletableFuture);
        return argumentCaptor;
    }
    // endregion
}