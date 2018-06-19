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

package com.ensarsarajcic.neovim.java.rxapi;

import com.ensarsarajcic.neovim.java.api.tabpage.NeovimTabpageApi;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Tabpage;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Window;
import com.ensarsarajcic.neovim.java.api.window.NeovimWindowApi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class NeovimTabpageRxWrapperTest {

    @Mock
    NeovimTabpageApi neovimTabpageApi;

    @InjectMocks
    NeovimTabpageRxWrapper neovimTabpageRxWrapper;

    @Test(expected = NullPointerException.class)
    public void cantBeCreatedWithNullApi() {
        new NeovimTabpageRxWrapper(null);
    }

    @Test
    public void delegatesGet() {
        Tabpage tabpage = new Tabpage(1);
        given(neovimTabpageApi.get()).willReturn(tabpage);
        assertEquals(neovimTabpageRxWrapper.get(), tabpage);
        verify(neovimTabpageApi).get();
    }

    @Test
    public void delegatesWindowOperations() {
        NeovimWindowApi neovimWindowApi = Mockito.mock(NeovimWindowApi.class);
        Window window = new Window(1);
        given(neovimWindowApi.get()).willReturn(window);
        given(neovimTabpageApi.getWindows()).willReturn(CompletableFuture.completedFuture(Collections.emptyList()));
        given(neovimTabpageApi.getWindow()).willReturn(CompletableFuture.completedFuture(neovimWindowApi));
        neovimTabpageRxWrapper.getWindows()
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(List::isEmpty);
        verify(neovimTabpageApi).getWindows();
        neovimTabpageRxWrapper.getWindow()
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(neovimWindowRxApi -> neovimWindowRxApi.get().equals(window));
        verify(neovimTabpageApi).getWindow();
    }

    @Test
    public void delegatesVarFunctions() {
        Object result = new Object();
        given(neovimTabpageApi.getVar("name")).willReturn(CompletableFuture.completedFuture(result));
        given(neovimTabpageApi.setVar("name", result)).willReturn(CompletableFuture.completedFuture(null));
        given(neovimTabpageApi.deleteVar("name")).willReturn(CompletableFuture.completedFuture(null));
        neovimTabpageRxWrapper.getVar("name")
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(result);
        verify(neovimTabpageApi).getVar("name");
        neovimTabpageRxWrapper.setVar("name", result)
                .test()
                .assertComplete()
                .assertNoErrors();
        verify(neovimTabpageApi).setVar("name", result);
        neovimTabpageRxWrapper.deleteVar("name")
                .test()
                .assertNoErrors()
                .assertComplete();
        verify(neovimTabpageApi).deleteVar("name");
    }

    @Test
    public void delegatesNumberCall() {
        given(neovimTabpageApi.getNumber()).willReturn(CompletableFuture.completedFuture(5));
        neovimTabpageRxWrapper.getNumber()
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(5);
        verify(neovimTabpageApi).getNumber();
    }

    @Test
    public void delegatesValidity() {
        given(neovimTabpageApi.isValid()).willReturn(CompletableFuture.completedFuture(true));
        neovimTabpageRxWrapper.isValid()
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(true);
        verify(neovimTabpageApi).isValid();
    }
}