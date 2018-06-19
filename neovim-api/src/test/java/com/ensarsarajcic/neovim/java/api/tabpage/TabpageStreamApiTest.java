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

package com.ensarsarajcic.neovim.java.api.tabpage;

import com.ensarsarajcic.neovim.java.api.BaseStreamApiTest;
import com.ensarsarajcic.neovim.java.api.NeovimApi;
import com.ensarsarajcic.neovim.java.api.types.msgpack.NeovimCustomType;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Tabpage;
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
public class TabpageStreamApiTest extends BaseStreamApiTest {

    private Tabpage tabpage;
    TabpageStreamApi tabpageStreamApi;

    @Before
    public void setUp() throws Exception {
        tabpage = new Tabpage(1);
        tabpageStreamApi = new TabpageStreamApi(
                reactiveRPCStreamer,
                tabpage
        );
    }

    @Test(expected = NullPointerException.class)
    public void cantConstructWithNullModel() {
        new TabpageStreamApi(reactiveRPCStreamer, null);
    }

    @Test(expected = NullPointerException.class)
    public void cantConstructWithNullStreamer() {
        new TabpageStreamApi(null, tabpage);
    }

    @Test
    public void toStringDoesntCrash() {
        tabpageStreamApi.toString();
    }

    @Test
    public void getVarTest() throws ExecutionException, InterruptedException {
        // Happy case
        Object varVal = "value";
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, varVal)),
                () -> tabpageStreamApi.getVar("name"),
                request -> assertMethodAndArguments(request, NeovimTabpageApi.GET_VAR, tabpage, "name"),
                result -> assertEquals(varVal, result)
        );

        // Error case
        assertErrorBehavior(
                () -> tabpageStreamApi.getVar("wrong name"),
                request -> assertMethodAndArguments(request, NeovimTabpageApi.GET_VAR, tabpage, "wrong name")
        );
    }

    @Test
    public void setVarTest() throws ExecutionException, InterruptedException {
        // Happy case
        Object varVal = "value";
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, null)),
                () -> tabpageStreamApi.setVar("name", varVal),
                request -> assertMethodAndArguments(request, NeovimTabpageApi.SET_VAR, tabpage, "name", varVal)
        );

        // Error case
        Object badVal = new Object();
        assertErrorBehavior(
                () -> tabpageStreamApi.setVar("wrong name", badVal),
                request -> assertMethodAndArguments(request, NeovimTabpageApi.SET_VAR, tabpage, "wrong name", badVal)
        );
    }

    @Test
    public void deleteVarTest() throws ExecutionException, InterruptedException {
        // Happy case
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, null)),
                () -> tabpageStreamApi.deleteVar("name"),
                request -> assertMethodAndArguments(request, NeovimTabpageApi.DEL_VAR, tabpage, "name")
        );

        // Error case
        assertErrorBehavior(
                () -> tabpageStreamApi.deleteVar("wrong name"),
                request -> assertMethodAndArguments(request, NeovimTabpageApi.DEL_VAR, tabpage, "wrong name")
        );
    }

    @Test
    public void getNumberTest() throws ExecutionException, InterruptedException {
        // Happy case
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, 1)),
                () -> tabpageStreamApi.getNumber(),
                request -> assertMethodAndArguments(request, NeovimTabpageApi.GET_NUMBER, tabpage),
                result -> assertEquals(1, result.intValue())
        );

        // Error case
        assertErrorBehavior(
                () -> tabpageStreamApi.getNumber(),
                request -> assertMethodAndArguments(request, NeovimTabpageApi.GET_NUMBER, tabpage)
        );
    }

    @Test
    public void isValidTest() throws ExecutionException, InterruptedException {
        // Happy case
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, false)),
                () -> tabpageStreamApi.isValid(),
                request -> assertMethodAndArguments(request, NeovimTabpageApi.IS_VALID, tabpage),
                Assert::assertFalse
        );

        // Error case
        assertErrorBehavior(
                () -> tabpageStreamApi.isValid(),
                request -> assertMethodAndArguments(request, NeovimTabpageApi.IS_VALID, tabpage)
        );
    }

    @Test
    public void getWindowsTest() throws InterruptedException, ExecutionException {
        // Happy case
        List<MessagePackExtensionType> windows = List.of(
                new MessagePackExtensionType((byte) NeovimCustomType.WINDOW.getTypeId(), new byte[]{1}),
                new MessagePackExtensionType((byte) NeovimCustomType.WINDOW.getTypeId(), new byte[]{2}),
                new MessagePackExtensionType((byte) NeovimCustomType.WINDOW.getTypeId(), new byte[]{3})
        );
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, windows)),
                () -> tabpageStreamApi.getWindows(),
                request -> assertMethodAndArguments(request, NeovimTabpageApi.LIST_WINDOWS),
                result -> {
                    assertEquals(1, result.get(0).get().getId());
                    assertEquals(2, result.get(1).get().getId());
                    assertEquals(3, result.get(2).get().getId());
                }
        );

        // Error case
        assertErrorBehavior(
                () -> tabpageStreamApi.getWindows(),
                request -> assertMethodAndArguments(request, NeovimTabpageApi.LIST_WINDOWS)
        );
    }

    @Test
    public void getWindowTest() throws InterruptedException, ExecutionException {
        // Happy case
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, new MessagePackExtensionType((byte) NeovimCustomType.WINDOW.getTypeId(), new byte[]{3}))),
                () -> tabpageStreamApi.getWindow(),
                request -> assertMethodAndArguments(request, NeovimTabpageApi.GET_WINDOW),
                result -> assertEquals(3, result.get().getId())
        );

        // Error case
        assertErrorBehavior(
                () -> tabpageStreamApi.getWindow(),
                request -> assertMethodAndArguments(request, NeovimTabpageApi.GET_WINDOW)
        );
    }
}