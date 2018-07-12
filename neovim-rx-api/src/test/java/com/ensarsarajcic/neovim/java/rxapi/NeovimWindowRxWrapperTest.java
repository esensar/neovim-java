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

import com.ensarsarajcic.neovim.java.api.buffer.NeovimBufferApi;
import com.ensarsarajcic.neovim.java.api.tabpage.NeovimTabpageApi;
import com.ensarsarajcic.neovim.java.api.types.api.VimCoords;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Buffer;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Tabpage;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Window;
import com.ensarsarajcic.neovim.java.api.window.NeovimWindowApi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class NeovimWindowRxWrapperTest {

    @Mock
    NeovimWindowApi neovimWindowApi;

    @InjectMocks
    NeovimWindowRxWrapper neovimWindowRxWrapper;

    @Test(expected = NullPointerException.class)
    public void cantBeCreatedWithNullApi() {
        new NeovimWindowRxWrapper(null);
    }

    @Test
    public void delegatesGet() {
        var window = new Window(1);
        given(neovimWindowApi.get()).willReturn(window);
        assertEquals(neovimWindowRxWrapper.get(), window);
        verify(neovimWindowApi).get();
    }

    @Test
    public void delegatesGetBuffer() {
        var neovimBufferApi = Mockito.mock(NeovimBufferApi.class);
        var buffer = new Buffer(1);
        given(neovimBufferApi.get()).willReturn(buffer);
        given(neovimWindowApi.getBuffer()).willReturn(CompletableFuture.completedFuture(neovimBufferApi));
        neovimWindowRxWrapper.getBuffer()
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(neovimBufferRxApi -> neovimBufferRxApi.get().equals(buffer));
        verify(neovimWindowApi).getBuffer();
    }

    @Test
    public void delegatesGetTabpage() {
        var neovimTabpageApi = Mockito.mock(NeovimTabpageApi.class);
        var tabpage = new Tabpage(1);
        given(neovimTabpageApi.get()).willReturn(tabpage);
        given(neovimWindowApi.getTabpage()).willReturn(CompletableFuture.completedFuture(neovimTabpageApi));
        neovimWindowRxWrapper.getTabpage()
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(neovimTabpageRxApi -> neovimTabpageRxApi.get().equals(tabpage));
        verify(neovimWindowApi).getTabpage();
    }

    @Test
    public void delegatesCursorOperations() {
        var vimCoords = new VimCoords(1, 2);
        given(neovimWindowApi.getCursor()).willReturn(CompletableFuture.completedFuture(vimCoords));
        neovimWindowRxWrapper.getCursor()
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(vimCoords);
        verify(neovimWindowApi).getCursor();
        given(neovimWindowApi.setCursor(vimCoords)).willReturn(CompletableFuture.completedFuture(null));
        neovimWindowRxWrapper.setCursor(vimCoords)
                .test()
                .assertNoErrors()
                .assertComplete();
        verify(neovimWindowApi).setCursor(vimCoords);
    }

    @Test
    public void delegatesDimensionOperations() {
        given(neovimWindowApi.getHeight()).willReturn(CompletableFuture.completedFuture(5));
        given(neovimWindowApi.getWidth()).willReturn(CompletableFuture.completedFuture(5));
        given(neovimWindowApi.setHeight(5)).willReturn(CompletableFuture.completedFuture(null));
        given(neovimWindowApi.setWidth(5)).willReturn(CompletableFuture.completedFuture(null));
        neovimWindowRxWrapper.getHeight()
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(5);
        neovimWindowRxWrapper.getWidth()
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(5);
        neovimWindowRxWrapper.setWidth(5)
                .test()
                .assertComplete()
                .assertNoErrors();
        neovimWindowRxWrapper.setHeight(5)
                .test()
                .assertComplete()
                .assertNoErrors();
        verify(neovimWindowApi).getHeight();
        verify(neovimWindowApi).getWidth();
        verify(neovimWindowApi).setHeight(5);
        verify(neovimWindowApi).setWidth(5);
    }

    @Test
    public void delegatesVarOperations() {
        var result = new Object();
        given(neovimWindowApi.getVar("name")).willReturn(CompletableFuture.completedFuture(result));
        given(neovimWindowApi.setVar("name", result)).willReturn(CompletableFuture.completedFuture(null));
        given(neovimWindowApi.deleteVar("name")).willReturn(CompletableFuture.completedFuture(null));
        neovimWindowRxWrapper.getVar("name")
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(result);
        verify(neovimWindowApi).getVar("name");
        neovimWindowRxWrapper.setVar("name", result)
                .test()
                .assertNoErrors()
                .assertComplete();
        verify(neovimWindowApi).setVar("name", result);
        neovimWindowRxWrapper.deleteVar("name")
                .test()
                .assertNoErrors()
                .assertComplete();
        verify(neovimWindowApi).deleteVar("name");
    }

    @Test
    public void delegatesOptionOperations() {
        var result = new Object();
        given(neovimWindowApi.getOption("name")).willReturn(CompletableFuture.completedFuture(result));
        given(neovimWindowApi.setOption("name", result)).willReturn(CompletableFuture.completedFuture(null));
        neovimWindowRxWrapper.getOption("name")
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(result);
        verify(neovimWindowApi).getOption("name");
        neovimWindowRxWrapper.setOption("name", result)
                .test()
                .assertComplete()
                .assertNoErrors();
        verify(neovimWindowApi).setOption("name", result);
    }


    @Test
    public void delegatesNumberCall() {
        given(neovimWindowApi.getNumber()).willReturn(CompletableFuture.completedFuture(5));
        neovimWindowRxWrapper.getNumber()
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(5);
        verify(neovimWindowApi).getNumber();
    }

    @Test
    public void delegatesValidity() {
        given(neovimWindowApi.isValid()).willReturn(CompletableFuture.completedFuture(true));
        neovimWindowRxWrapper.isValid()
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(true);
        verify(neovimWindowApi).isValid();
    }
}