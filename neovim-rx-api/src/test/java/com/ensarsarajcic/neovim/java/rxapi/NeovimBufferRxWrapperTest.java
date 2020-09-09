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
import com.ensarsarajcic.neovim.java.api.types.api.GetCommandsOptions;
import com.ensarsarajcic.neovim.java.api.types.api.HighlightedText;
import com.ensarsarajcic.neovim.java.api.types.api.VimCoords;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Buffer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class NeovimBufferRxWrapperTest {

    @Mock
    NeovimBufferApi neovimBufferApi;

    @InjectMocks
    NeovimBufferRxWrapper neovimBufferRxWrapper;

    @Test(expected = NullPointerException.class)
    public void cantBeCreatedWithNullApi() {
        new NeovimBufferRxWrapper(null);
    }

    @Test
    public void delegatesGet() {
        var buffer = new Buffer(1);
        given(neovimBufferApi.get()).willReturn(buffer);
        assertEquals(neovimBufferRxWrapper.get(), buffer);
        verify(neovimBufferApi).get();
    }

    @Test
    public void delegatesLineOperations() {
        var replacement = List.<String>of();
        given(neovimBufferApi.getLines(1, 5, true)).willReturn(CompletableFuture.completedFuture(List.of()));
        given(neovimBufferApi.setLines(1, 5, true, replacement)).willReturn(CompletableFuture.completedFuture(null));
        given(neovimBufferApi.getLineCount()).willReturn(CompletableFuture.completedFuture(5));
        neovimBufferRxWrapper.getLines(1, 5, true)
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(List::isEmpty);
        verify(neovimBufferApi).getLines(1, 5, true);
        neovimBufferRxWrapper.setLines(1, 5, true, replacement)
                .test()
                .assertComplete()
                .assertNoErrors();
        verify(neovimBufferApi).setLines(1, 5, true, replacement);
        neovimBufferRxWrapper.getLineCount()
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(5);
        verify(neovimBufferApi).getLineCount();
    }

    @Test
    public void delegatesGetOffset() {
        given(neovimBufferApi.getOffset(1)).willReturn(CompletableFuture.completedFuture(5));
        neovimBufferRxWrapper.getOffset(1)
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(5);
        verify(neovimBufferApi).getOffset(1);
    }

    @Test
    public void delegatesVarOperations() {
        var result = new Object();
        given(neovimBufferApi.getVar("name")).willReturn(CompletableFuture.completedFuture(result));
        given(neovimBufferApi.setVar("name", result)).willReturn(CompletableFuture.completedFuture(null));
        given(neovimBufferApi.deleteVar("name")).willReturn(CompletableFuture.completedFuture(null));
        neovimBufferRxWrapper.getVar("name")
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(result);
        verify(neovimBufferApi).getVar("name");
        neovimBufferRxWrapper.setVar("name", result)
                .test()
                .assertNoErrors()
                .assertComplete();
        verify(neovimBufferApi).setVar("name", result);
        neovimBufferRxWrapper.deleteVar("name")
                .test()
                .assertNoErrors()
                .assertComplete();
        verify(neovimBufferApi).deleteVar("name");
    }

    @Test
    public void delegatesOptionOperations() {
        var result = new Object();
        given(neovimBufferApi.getOption("name")).willReturn(CompletableFuture.completedFuture(result));
        given(neovimBufferApi.setOption("name", result)).willReturn(CompletableFuture.completedFuture(null));
        neovimBufferRxWrapper.getOption("name")
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(result);
        verify(neovimBufferApi).getOption("name");
        neovimBufferRxWrapper.setOption("name", result)
                .test()
                .assertComplete()
                .assertNoErrors();
        verify(neovimBufferApi).setOption("name", result);
    }


    @Test
    public void delegatesNumberCall() {
        given(neovimBufferApi.getNumber()).willReturn(CompletableFuture.completedFuture(5));
        neovimBufferRxWrapper.getNumber()
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(5);
        verify(neovimBufferApi).getNumber();
    }

    @Test
    public void delegatesNameOperations() {
        given(neovimBufferApi.getName()).willReturn(CompletableFuture.completedFuture("name"));
        given(neovimBufferApi.setName("name")).willReturn(CompletableFuture.completedFuture(null));
        neovimBufferRxWrapper.getName()
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue("name");
        verify(neovimBufferApi).getName();
        neovimBufferRxWrapper.setName("name")
                .test()
                .assertComplete()
                .assertNoErrors();
        verify(neovimBufferApi).setName("name");
    }

    @Test
    public void delegatesStateChecks() {
        given(neovimBufferApi.isValid()).willReturn(CompletableFuture.completedFuture(true));
        given(neovimBufferApi.isLoaded()).willReturn(CompletableFuture.completedFuture(false));
        neovimBufferRxWrapper.isValid()
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(true);
        verify(neovimBufferApi).isValid();
        neovimBufferRxWrapper.isLoaded()
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(false);
        verify(neovimBufferApi).isLoaded();
    }

    @Test
    public void delegatesGetMark() {
        var vimCoords = new VimCoords(10, 10);
        given(neovimBufferApi.getMark("name")).willReturn(CompletableFuture.completedFuture(vimCoords));
        neovimBufferRxWrapper.getMark("name")
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(vimCoords);
        verify(neovimBufferApi).getMark("name");
    }

    @Test
    public void delegatesGetChangedTick() {
        var result = new Object();
        given(neovimBufferApi.getChangedTick()).willReturn(CompletableFuture.completedFuture(result));
        neovimBufferRxWrapper.getChangedTick()
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(result);
        verify(neovimBufferApi).getChangedTick();
    }

    @Test
    public void delegatesGetKeymap() {
        given(neovimBufferApi.getKeymap("n")).willReturn(CompletableFuture.completedFuture(List.of()));
        neovimBufferRxWrapper.getKeymap("n")
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(List::isEmpty);
        verify(neovimBufferApi).getKeymap("n");
    }

    @Test
    public void delegatesHighlightOperations() {
        given(neovimBufferApi.addHighlight(1, "hlg", 1, 2, 3)).willReturn(CompletableFuture.completedFuture(5));
        given(neovimBufferApi.clearHighlight(1, 1, 3)).willReturn(CompletableFuture.completedFuture(null));
        neovimBufferRxWrapper.addHighlight(1, "hlg", 1, 2, 3)
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(5);
        verify(neovimBufferApi).addHighlight(1, "hlg", 1, 2, 3);
        neovimBufferRxWrapper.clearHighlight(1, 1, 3)
                .test()
                .assertNoErrors()
                .assertComplete();
        verify(neovimBufferApi).clearHighlight(1, 1, 3);
    }

    @Test
    public void delegatesNamespaceOperations() {
        List<HighlightedText> chunks = List.of(new HighlightedText("justText"));
        given(neovimBufferApi.clearNamespace(1, 5, 10)).willReturn(CompletableFuture.completedFuture(null));
        given(neovimBufferApi.setVirtualText(1, 5, chunks, Map.of())).willReturn(CompletableFuture.completedFuture(1));
        neovimBufferRxWrapper.clearNamespace(1, 5, 10)
                .test()
                .assertNoErrors()
                .assertComplete();
        verify(neovimBufferApi).clearNamespace(1, 5, 10);
        neovimBufferRxWrapper.setVirtualText(1, 5, chunks, Map.of())
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(1);
        verify(neovimBufferApi).setVirtualText(1, 5, chunks, Map.of());
    }

    @Test
    public void delegatesAttach() {
        var opts = Map.of();
        given(neovimBufferApi.attach(false, opts)).willReturn(CompletableFuture.completedFuture(true));
        neovimBufferRxWrapper.attach(false, opts)
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(true);
        verify(neovimBufferApi).attach(false, opts);
    }

    @Test
    public void delegatesDetach() {
        given(neovimBufferApi.detach()).willReturn(CompletableFuture.completedFuture(true));
        neovimBufferRxWrapper.detach()
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(true);
        verify(neovimBufferApi).detach();
    }

    @Test
    public void delegatesGetCommands() {
        var getCommandsOptions = new GetCommandsOptions(false);
        given(neovimBufferApi.getCommands(getCommandsOptions)).willReturn(CompletableFuture.completedFuture(Map.of()));
        neovimBufferRxWrapper.getCommands(getCommandsOptions)
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(Map::isEmpty);
        verify(neovimBufferApi).getCommands(getCommandsOptions);
    }
}