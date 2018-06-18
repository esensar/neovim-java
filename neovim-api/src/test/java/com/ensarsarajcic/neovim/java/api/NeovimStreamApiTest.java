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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
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
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, Map.of())),
                () -> neovimStreamApi.getHighlightById(1, true),
                request -> assertMethodAndArguments(request, NeovimApi.GET_HIGHLIGHT_BY_ID, 1, true),
                result -> assertTrue(result.isEmpty())
        );

        // Error case
        assertErrorBehavior(
                () -> neovimStreamApi.getHighlightById(5, false),
                request -> assertMethodAndArguments(request, NeovimApi.GET_HIGHLIGHT_BY_ID, 5, false)
        );
    }

    @Test
    public void getHightlightByNameTest() throws ExecutionException, InterruptedException {
        // Happy case
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, Map.of())),
                () -> neovimStreamApi.getHighlightByName("name", true),
                request -> assertMethodAndArguments(request, NeovimApi.GET_HIGHLIGHT_BY_NAME, "name", true),
                result -> assertTrue(result.isEmpty())
        );

        // Error case
        assertErrorBehavior(
                () -> neovimStreamApi.getHighlightByName("some other name", false),
                request -> assertMethodAndArguments(request, NeovimApi.GET_HIGHLIGHT_BY_NAME, "some other name", false)
        );
    }

    @Test
    public void attachUITest() throws InterruptedException, ExecutionException {
        // Happy case
        Map<String, String> opts = Map.of("Test", "Value");
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, null)),
                () -> neovimStreamApi.attachUI(500, 500, opts),
                request -> assertMethodAndArguments(request, NeovimApi.ATTACH_UI, 500, 500, opts),
                Assert::assertNull
        );

        // Error case
        assertErrorBehavior(
                () -> neovimStreamApi.detachUI(),
                request -> assertMethodAndArguments(request, NeovimApi.DETACH_UI)
        );
    }

    @Test
    public void resizeUITest() throws InterruptedException, ExecutionException {
        // Happy case
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, null)),
                () -> neovimStreamApi.resizeUI(500, 500),
                request -> assertMethodAndArguments(request, NeovimApi.RESIZE_UI, 500, 500),
                Assert::assertNull
        );

        // Error case
        assertErrorBehavior(
                () -> neovimStreamApi.resizeUI(150, 450),
                request -> assertMethodAndArguments(request, NeovimApi.RESIZE_UI, 150, 450)
        );
    }

    @Test
    public void executeLuaTest() throws InterruptedException, ExecutionException {
        // Happy case
        String realLuaResult = "lua exec result";
        List<String> args = List.of("lua arg1");
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, realLuaResult)),
                () -> neovimStreamApi.executeLua("real lua code", args),
                request -> assertMethodAndArguments(request, NeovimApi.EXECUTE_LUA, "real lua code", args),
                result -> assertEquals(result, realLuaResult)
        );

        // Error case
        List<String> badArgs = List.of();
        assertErrorBehavior(
                () -> neovimStreamApi.executeLua("bad lua code", badArgs),
                request -> assertMethodAndArguments(request, NeovimApi.EXECUTE_LUA, "bad lua code", badArgs)
        );
    }

    @Test
    public void executeCommandTest() throws InterruptedException, ExecutionException {
        // Happy case
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, null)),
                () -> neovimStreamApi.executeCommand("real vim command"),
                request -> assertMethodAndArguments(request, NeovimApi.EXECUTE_COMMAND, "real vim command"),
                Assert::assertNull
        );

        // Error case
        assertErrorBehavior(
                () -> neovimStreamApi.executeCommand("bad vim command"),
                request -> assertMethodAndArguments(request, NeovimApi.EXECUTE_COMMAND, "bad vim command")
        );
    }

    @Test
    public void setCurrentDirTest() throws InterruptedException, ExecutionException {
        // Happy case
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, null)),
                () -> neovimStreamApi.setCurrentDir("/home"),
                request -> assertMethodAndArguments(request, NeovimApi.SET_CURRENT_DIR, "/home"),
                Assert::assertNull
        );

        // Error case
        assertErrorBehavior(
                () -> neovimStreamApi.setCurrentDir("badDir"),
                request -> assertMethodAndArguments(request, NeovimApi.SET_CURRENT_DIR, "badDir")
        );
    }

    @Test
    public void subscribeToEventTest() throws InterruptedException, ExecutionException {
        // Happy case
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, null)),
                () -> neovimStreamApi.subscribeToEvent("real event"),
                request -> assertMethodAndArguments(request, NeovimApi.SUBSCRIBE_TO_EVENT, "real event"),
                Assert::assertNull
        );

        // Error case
        assertErrorBehavior(
                () -> neovimStreamApi.subscribeToEvent("bad event"),
                request -> assertMethodAndArguments(request, NeovimApi.SUBSCRIBE_TO_EVENT, "bad event")
        );
    }
    // region Testing helpers
    private <T> void assertNormalBehavior(
            Supplier<CompletableFuture<ResponseMessage>> preparedResponse,
            Supplier<CompletableFuture<T>> callSupplier,
            Consumer<RequestMessage> requestAsserter,
            Consumer<T> resultAsserter
    ) throws ExecutionException, InterruptedException {
        ArgumentCaptor<RequestMessage.Builder> argumentCaptor = prepareArgumentCaptor(preparedResponse.get());
        CompletableFuture<T> result = callSupplier.get();
        RequestMessage requestMessage = argumentCaptor.getValue().build();
        requestAsserter.accept(requestMessage);
        verify(reactiveRPCStreamer, atLeastOnce()).response(any());
        resultAsserter.accept(result.get());
    }

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