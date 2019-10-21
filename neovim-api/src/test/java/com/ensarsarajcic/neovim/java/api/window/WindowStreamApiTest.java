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

package com.ensarsarajcic.neovim.java.api.window;

import com.ensarsarajcic.neovim.java.api.BaseStreamApiTest;
import com.ensarsarajcic.neovim.java.api.types.api.VimCoords;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Buffer;
import com.ensarsarajcic.neovim.java.api.types.msgpack.NeovimCustomType;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Window;
import com.ensarsarajcic.neovim.java.corerpc.message.ResponseMessage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.msgpack.jackson.dataformat.MessagePackExtensionType;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class WindowStreamApiTest extends BaseStreamApiTest {

    private Window window;
    WindowStreamApi windowStreamApi;

    @Before
    public void setUp() throws Exception {
        window = new Window(1);
        windowStreamApi = new WindowStreamApi(
                reactiveRPCStreamer,
                window
        );
    }

    @Test(expected = NullPointerException.class)
    public void cantConstructWithNullModel() {
        new WindowStreamApi(reactiveRPCStreamer, null);
    }

    @Test(expected = NullPointerException.class)
    public void cantConstructWithNullStreamer() {
        new WindowStreamApi(null, window);
    }

    @Test
    public void toStringDoesntCrash() {
        windowStreamApi.toString();
    }

    @Test
    public void getVarTest() throws ExecutionException, InterruptedException {
        // Happy case
        var varVal = "value";
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, varVal)),
                () -> windowStreamApi.getVar("name"),
                request -> assertMethodAndArguments(request, NeovimWindowApi.GET_VAR, window, "name"),
                result -> assertEquals(varVal, result)
        );

        // Error case
        assertErrorBehavior(
                () -> windowStreamApi.getVar("wrong name"),
                request -> assertMethodAndArguments(request, NeovimWindowApi.GET_VAR, window, "wrong name")
        );
    }

    @Test
    public void setVarTest() throws ExecutionException, InterruptedException {
        // Happy case
        var varVal = "value";
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, null)),
                () -> windowStreamApi.setVar("name", varVal),
                request -> assertMethodAndArguments(request, NeovimWindowApi.SET_VAR, window, "name", varVal)
        );

        // Error case
        var badVal = new Object();
        assertErrorBehavior(
                () -> windowStreamApi.setVar("wrong name", badVal),
                request -> assertMethodAndArguments(request, NeovimWindowApi.SET_VAR, window, "wrong name", badVal)
        );
    }

    @Test
    public void deleteVarTest() throws ExecutionException, InterruptedException {
        // Happy case
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, null)),
                () -> windowStreamApi.deleteVar("name"),
                request -> assertMethodAndArguments(request, NeovimWindowApi.DEL_VAR, window, "name")
        );

        // Error case
        assertErrorBehavior(
                () -> windowStreamApi.deleteVar("wrong name"),
                request -> assertMethodAndArguments(request, NeovimWindowApi.DEL_VAR, window, "wrong name")
        );
    }

    @Test
    public void getNumberTest() throws ExecutionException, InterruptedException {
        // Happy case
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, 1)),
                () -> windowStreamApi.getNumber(),
                request -> assertMethodAndArguments(request, NeovimWindowApi.GET_NUMBER, window),
                result -> assertEquals(1, result.intValue())
        );

        // Error case
        assertErrorBehavior(
                () -> windowStreamApi.getNumber(),
                request -> assertMethodAndArguments(request, NeovimWindowApi.GET_NUMBER, window)
        );
    }

    @Test
    public void isValidTest() throws ExecutionException, InterruptedException {
        // Happy case
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, false)),
                () -> windowStreamApi.isValid(),
                request -> assertMethodAndArguments(request, NeovimWindowApi.IS_VALID, window),
                Assert::assertFalse
        );

        // Error case
        assertErrorBehavior(
                () -> windowStreamApi.isValid(),
                request -> assertMethodAndArguments(request, NeovimWindowApi.IS_VALID, window)
        );
    }

    @Test
    public void getBufferTest() throws InterruptedException, ExecutionException {
        // Happy case
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, new MessagePackExtensionType((byte) NeovimCustomType.BUFFER.getTypeId(), new byte[]{3}))),
                () -> windowStreamApi.getBuffer(),
                request -> assertMethodAndArguments(request, NeovimWindowApi.GET_BUFFER),
                result -> assertEquals(3, result.get().getId())
        );

        // Error case
        assertErrorBehavior(
                () -> windowStreamApi.getBuffer(),
                request -> assertMethodAndArguments(request, NeovimWindowApi.GET_BUFFER)
        );
    }

    @Test
    public void setBufferTest() throws InterruptedException, ExecutionException {
        // Happy case
        var buffer = new Buffer(1);
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, null)),
                () -> windowStreamApi.setBuffer(buffer),
                request -> assertMethodAndArguments(request, NeovimWindowApi.SET_BUFFER, window, buffer)
        );

        // Error case
        assertErrorBehavior(
                () -> windowStreamApi.setBuffer(buffer),
                request -> assertMethodAndArguments(request, NeovimWindowApi.SET_BUFFER, window, buffer)
        );
    }

    @Test
    public void getTabpageTest() throws InterruptedException, ExecutionException {
        // Happy case
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, new MessagePackExtensionType((byte) NeovimCustomType.TABPAGE.getTypeId(), new byte[]{4}))),
                () -> windowStreamApi.getTabpage(),
                request -> assertMethodAndArguments(request, NeovimWindowApi.GET_TABPAGE, window),
                result -> assertEquals(4, result.get().getId())
        );

        // Error case
        assertErrorBehavior(
                () -> windowStreamApi.getTabpage(),
                request -> assertMethodAndArguments(request, NeovimWindowApi.GET_TABPAGE, window)
        );
    }

    @Test
    public void getCursorTest() throws ExecutionException, InterruptedException {
        // Happy case
        var coords = List.of(100, 200);
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, coords)),
                () -> windowStreamApi.getCursor(),
                request -> assertMethodAndArguments(request, NeovimWindowApi.GET_CURSOR, window),
                result -> {
                    assertEquals(100, result.getRow());
                    assertEquals(200, result.getCol());

                    // Ensure to string doesn't crash
                    result.toString();
                }
        );

        // Error case
        assertErrorBehavior(
                () -> windowStreamApi.getCursor(),
                request -> assertMethodAndArguments(request, NeovimWindowApi.GET_CURSOR, window)
        );
    }

    @Test
    public void setCursorTest() throws ExecutionException, InterruptedException {
        // Happy case
        var vimCoords = new VimCoords(500, 600);
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, null)),
                () -> windowStreamApi.setCursor(vimCoords),
                request -> assertMethodAndArguments(request, NeovimWindowApi.SET_CURSOR, window, vimCoords)
        );

        // Error case
        var badCoords = new VimCoords(1, 2);
        assertErrorBehavior(
                () -> windowStreamApi.setCursor(badCoords),
                request -> assertMethodAndArguments(request, NeovimWindowApi.SET_CURSOR, window, badCoords)
        );
    }

    @Test
    public void getHeightTest() throws ExecutionException, InterruptedException {
        // Happy case
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, 200)),
                () -> windowStreamApi.getHeight(),
                request -> assertMethodAndArguments(request, NeovimWindowApi.GET_HEIGHT, window),
                result -> assertEquals(200, result.intValue())
        );

        // Error case
        assertErrorBehavior(
                () -> windowStreamApi.getHeight(),
                request -> assertMethodAndArguments(request, NeovimWindowApi.GET_HEIGHT, window)
        );
    }

    @Test
    public void setHeightTest() throws ExecutionException, InterruptedException {
        // Happy case
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, null)),
                () -> windowStreamApi.setHeight(500),
                request -> assertMethodAndArguments(request, NeovimWindowApi.SET_HEIGHT, window, 500)
        );

        // Error case
        assertErrorBehavior(
                () -> windowStreamApi.setHeight(-100),
                request -> assertMethodAndArguments(request, NeovimWindowApi.SET_HEIGHT, window, -100)
        );
    }

    @Test
    public void getWidthTest() throws ExecutionException, InterruptedException {
        // Happy case
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, 600)),
                () -> windowStreamApi.getWidth(),
                request -> assertMethodAndArguments(request, NeovimWindowApi.GET_WIDTH, window),
                result -> assertEquals(600, result.intValue())
        );

        // Error case
        assertErrorBehavior(
                () -> windowStreamApi.getWidth(),
                request -> assertMethodAndArguments(request, NeovimWindowApi.GET_WIDTH, window)
        );
    }

    @Test
    public void setWidthTest() throws ExecutionException, InterruptedException {
        // Happy case
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, null)),
                () -> windowStreamApi.setWidth(300),
                request -> assertMethodAndArguments(request, NeovimWindowApi.SET_WIDTH, window, 300)
        );

        // Error case
        assertErrorBehavior(
                () -> windowStreamApi.setWidth(-200),
                request -> assertMethodAndArguments(request, NeovimWindowApi.SET_WIDTH, window, -200)
        );
    }
}