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
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
@Ignore
public class BaseStreamApiTest {

    @Mock
    protected ReactiveRPCStreamer reactiveRPCStreamer;

    protected void assertNormalBehavior(
            Supplier<CompletableFuture<ResponseMessage>> preparedResponse,
            Supplier<CompletableFuture<Void>> callSupplier,
            Consumer<RequestMessage> requestAsserter
    ) throws ExecutionException, InterruptedException {
        assertNormalBehavior(preparedResponse, callSupplier, requestAsserter, Assert::assertNull);
    }

    protected <T> void assertNormalBehavior(
            Supplier<CompletableFuture<ResponseMessage>> preparedResponse,
            Supplier<CompletableFuture<T>> callSupplier,
            Consumer<RequestMessage> requestAsserter,
            Consumer<T> resultAsserter
    ) throws ExecutionException, InterruptedException {
        var argumentCaptor = prepareArgumentCaptor(preparedResponse.get());
        var result = callSupplier.get();
        var requestMessage = argumentCaptor.getValue().build();
        requestAsserter.accept(requestMessage);
        verify(reactiveRPCStreamer, atLeastOnce()).response(any());
        resultAsserter.accept(result.get());
    }

    protected void assertErrorBehavior(
            Supplier<CompletableFuture<?>> completableFutureSupplier,
            Consumer<RequestMessage> requestAsserter) throws InterruptedException {
        var errorArgumentCaptor = prepareArgumentCaptor(
                CompletableFuture.failedFuture(new RPCException(new RPCError(1, "error"))));
        var errorResult = completableFutureSupplier.get();
        var errorResponse = errorArgumentCaptor.getValue().build();
        requestAsserter.accept(errorResponse);
        verify(reactiveRPCStreamer, atLeastOnce()).response(any());
        verifyError(errorResult);
    }

    protected void assertMethodAndArguments(RequestMessage message, String method, Object... arguments) {
        assertEquals(message.getMethod(), method);
        int i = 0;
        for (var arg : arguments) {
            assertEquals(message.getArguments().get(i), arg);
            i++;
        }
    }

    protected void verifyError(CompletableFuture completableFuture) throws InterruptedException {
        try {
            completableFuture.get();
            fail("Should have thrown an error");
        } catch (ExecutionException ex) {
            if (!(ex.getCause() instanceof RPCException)) {
                fail("Should have been an RCP Exception");
            }
        }
    }

    protected ArgumentCaptor<RequestMessage.Builder> prepareArgumentCaptor(CompletableFuture<ResponseMessage> responseMessageCompletableFuture) {
        var argumentCaptor = ArgumentCaptor.forClass(RequestMessage.Builder.class);
        given(reactiveRPCStreamer.response(argumentCaptor.capture())).willReturn(responseMessageCompletableFuture);
        return argumentCaptor;
    }
}
