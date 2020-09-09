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

import com.ensarsarajcic.neovim.java.api.NeovimApi;
import com.ensarsarajcic.neovim.java.api.buffer.NeovimBufferApi;
import com.ensarsarajcic.neovim.java.api.tabpage.NeovimTabpageApi;
import com.ensarsarajcic.neovim.java.api.types.api.ChannelInfo;
import com.ensarsarajcic.neovim.java.api.types.api.ClientAttributes;
import com.ensarsarajcic.neovim.java.api.types.api.ClientType;
import com.ensarsarajcic.neovim.java.api.types.api.ClientVersionInfo;
import com.ensarsarajcic.neovim.java.api.types.api.GetCommandsOptions;
import com.ensarsarajcic.neovim.java.api.types.api.MethodInfo;
import com.ensarsarajcic.neovim.java.api.types.api.UiOptions;
import com.ensarsarajcic.neovim.java.api.types.api.VimColorMap;
import com.ensarsarajcic.neovim.java.api.types.api.VimMode;
import com.ensarsarajcic.neovim.java.api.types.apiinfo.ApiInfo;
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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class NeovimRxWrapperTest {

    @Mock
    NeovimApi neovimApi;

    @InjectMocks
    NeovimRxWrapper neovimRxWrapper;

    @Test(expected = NullPointerException.class)
    public void cantBeCreatedWithNullApi() {
        new NeovimRxWrapper(null);
    }

    @Test
    public void delegatesAttachUi() {
        var options = UiOptions.TERMINAL;
        given(neovimApi.attachUi(1, 1, options)).willReturn(CompletableFuture.completedFuture(null));
        neovimRxWrapper.attachUi(1, 1, options)
                .test()
                .assertComplete()
                .assertNoErrors();
        verify(neovimApi).attachUi(1, 1, options);
    }

    @Test
    public void delegatesDetachUI() {
        given(neovimApi.detachUi()).willReturn(CompletableFuture.completedFuture(null));
        neovimRxWrapper.detachUi()
                .test()
                .assertComplete()
                .assertNoErrors();
        verify(neovimApi).detachUi();
    }

    @Test
    public void delegatesResizeUI() {
        given(neovimApi.resizeUi(1, 1)).willReturn(CompletableFuture.completedFuture(null));
        neovimRxWrapper.resizeUi(1, 1)
                .test()
                .assertComplete()
                .assertNoErrors();
        verify(neovimApi).resizeUi(1, 1);
    }

    @Test
    public void delegatesAtomicCall() {
        var atomicCallBuilder = neovimRxWrapper.prepareAtomic();
        verify(neovimApi).prepareAtomic();
        given(neovimApi.sendAtomic(atomicCallBuilder)).willReturn(CompletableFuture.completedFuture(Collections.emptyList()));
        neovimRxWrapper.sendAtomic(atomicCallBuilder);
        verify(neovimApi).sendAtomic(atomicCallBuilder);
    }

    @Test
    public void delegatesHighlightCalls() {
        given(neovimApi.getHighlightById(1, true)).willReturn(CompletableFuture.completedFuture(Map.of()));
        neovimRxWrapper.getHighlightById(1, true)
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(Map::isEmpty);
        verify(neovimApi).getHighlightById(1, true);

        given(neovimApi.getHighlightByName("name", true)).willReturn(CompletableFuture.completedFuture(Map.of()));
        neovimRxWrapper.getHighlightByName("name", true)
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(Map::isEmpty);
        verify(neovimApi).getHighlightByName("name", true);
    }

    @Test
    public void delegatesExecuteLua() {
        var args = Collections.<String>emptyList();
        Object result = new Object();
        given(neovimApi.executeLua("lua", args)).willReturn(CompletableFuture.completedFuture(result));
        neovimRxWrapper.executeLua("lua", args)
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(result);
        verify(neovimApi).executeLua("lua", args);
    }

    @Test
    public void delegatesExecuteCommand() {
        given(neovimApi.executeCommand("Command")).willReturn(CompletableFuture.completedFuture(null));
        neovimRxWrapper.executeCommand("Command")
                .test()
                .assertNoErrors()
                .assertComplete();
        verify(neovimApi).executeCommand("Command");
    }

    @Test
    public void delegatesSetCurrentDir() {
        given(neovimApi.setCurrentDir("path")).willReturn(CompletableFuture.completedFuture(null));
        neovimRxWrapper.setCurrentDir("path")
                .test()
                .assertNoErrors()
                .assertComplete();
        verify(neovimApi).setCurrentDir("path");
    }

    @Test
    public void delegatesEventSubscriptions() {
        given(neovimApi.subscribeToEvent("event")).willReturn(CompletableFuture.completedFuture(null));
        given(neovimApi.unsubscribeFromEvent("event")).willReturn(CompletableFuture.completedFuture(null));
        neovimRxWrapper.subscribeToEvent("event")
                .test()
                .assertComplete()
                .assertNoErrors();
        verify(neovimApi).subscribeToEvent("event");
        neovimRxWrapper.unsubscribeFromEvent("event")
                .test()
                .assertComplete()
                .assertNoErrors();
        verify(neovimApi).unsubscribeFromEvent("event");
    }

    @Test
    public void delegatesEval() {
        var result = new Object();
        given(neovimApi.eval("expr")).willReturn(CompletableFuture.completedFuture(result));
        neovimRxWrapper.eval("expr")
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(result);
        verify(neovimApi).eval("expr");
    }

    @Test
    public void delegatesCallFunction() {
        var result = new Object();
        List<Object> args = Collections.emptyList();
        given(neovimApi.callFunction("func", args)).willReturn(CompletableFuture.completedFuture(result));
        neovimRxWrapper.callFunction("func", args)
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(result);
        verify(neovimApi).callFunction("func", args);
    }

    @Test
    public void delegatesFeedKeys() {
        given(neovimApi.feedKeys("keys", "n", false)).willReturn(CompletableFuture.completedFuture(null));
        neovimRxWrapper.feedKeys("keys", "n", false)
                .test()
                .assertNoErrors()
                .assertComplete();
        verify(neovimApi).feedKeys("keys", "n", false);
    }

    @Test
    public void delegatesInput() {
        given(neovimApi.input("keys")).willReturn(CompletableFuture.completedFuture(5));
        neovimRxWrapper.input("keys")
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(5);
        verify(neovimApi).input("keys");
    }

    @Test
    public void delegatesGetKeymap() {
        given(neovimApi.getKeymap("n")).willReturn(CompletableFuture.completedFuture(Collections.emptyList()));
        neovimRxWrapper.getKeymap("n")
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(List::isEmpty);
        verify(neovimApi).getKeymap("n");
    }

    @Test
    public void delegatesOptions() {
        var option = new Object();
        given(neovimApi.setUiOption("name", option)).willReturn(CompletableFuture.completedFuture(null));
        given(neovimApi.setOption("name", option)).willReturn(CompletableFuture.completedFuture(null));
        given(neovimApi.getOption("name")).willReturn(CompletableFuture.completedFuture(option));
        neovimRxWrapper.setUiOption("name", option)
                .test()
                .assertNoErrors()
                .assertComplete();
        verify(neovimApi).setUiOption("name", option);
        neovimRxWrapper.setOption("name", option)
                .test()
                .assertNoErrors()
                .assertComplete();
        verify(neovimApi).setOption("name", option);
        neovimRxWrapper.getOption("name")
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(option);
        verify(neovimApi).getOption("name");
    }

    @Test
    public void delegatesVariableOperations() {
        var var = new Object();
        given(neovimApi.setVariable("name", var)).willReturn(CompletableFuture.completedFuture(null));
        given(neovimApi.getVariable("name")).willReturn(CompletableFuture.completedFuture(var));
        given(neovimApi.getVimVariable("name")).willReturn(CompletableFuture.completedFuture(var));
        given(neovimApi.deleteVariable("name")).willReturn(CompletableFuture.completedFuture(null));
        neovimRxWrapper.setVariable("name", var)
                .test()
                .assertNoErrors()
                .assertComplete();
        verify(neovimApi).setVariable("name", var);
        neovimRxWrapper.getVariable("name")
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(var);
        verify(neovimApi).getVariable("name");
        neovimRxWrapper.getVimVariable("name")
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(var);
        verify(neovimApi).getVimVariable("name");
        neovimRxWrapper.deleteVariable("name")
                .test()
                .assertNoErrors()
                .assertComplete();
        verify(neovimApi).deleteVariable("name");
    }

    @Test
    public void delegatesGettingColor() {
        given(neovimApi.getColorByName("name")).willReturn(CompletableFuture.completedFuture(5));
        neovimRxWrapper.getColorByName("name")
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(5);
        verify(neovimApi).getColorByName("name");
    }

    @Test
    public void delegatesReplaceTermcodes() {
        given(neovimApi.replaceTermcodes("str", true, true, true)).willReturn(CompletableFuture.completedFuture("string"));
        neovimRxWrapper.replaceTermcodes("str", true, true, true)
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue("string");
        verify(neovimApi).replaceTermcodes("str", true, true, true);
    }

    @Test
    public void delegatesCommandOutput() {
        given(neovimApi.commandOutput("str")).willReturn(CompletableFuture.completedFuture("string"));
        neovimRxWrapper.commandOutput("str")
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue("string");
        verify(neovimApi).commandOutput("str");
    }

    @Test
    public void delegatesWrites() {
        given(neovimApi.writeToOutput("text")).willReturn(CompletableFuture.completedFuture(null));
        given(neovimApi.writelnToError("text")).willReturn(CompletableFuture.completedFuture(null));
        given(neovimApi.writeToError("text")).willReturn(CompletableFuture.completedFuture(null));
        neovimRxWrapper.writeToOutput("text")
                .test()
                .assertNoErrors()
                .assertComplete();
        verify(neovimApi).writeToOutput("text");
        neovimRxWrapper.writelnToError("text")
                .test()
                .assertNoErrors()
                .assertComplete();
        verify(neovimApi).writelnToError("text");
        neovimRxWrapper.writeToError("text")
                .test()
                .assertNoErrors()
                .assertComplete();
        verify(neovimApi).writeToError("text");
    }

    @Test
    public void delegatesStringWidth() {
        given(neovimApi.stringWidth("str")).willReturn(CompletableFuture.completedFuture(5));
        neovimRxWrapper.stringWidth("str")
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(5);
        verify(neovimApi).stringWidth("str");
    }

    @Test
    public void delegatesListRuntimePaths() {
        var result = Collections.<String>emptyList();
        given(neovimApi.listRuntimePaths()).willReturn(CompletableFuture.completedFuture(result));
        neovimRxWrapper.listRuntimePaths()
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(result);
        verify(neovimApi).listRuntimePaths();
    }

    @Test
    public void delegatesLineOperations() {
        given(neovimApi.getCurrentLine()).willReturn(CompletableFuture.completedFuture("line"));
        given(neovimApi.setCurrentLine("line")).willReturn(CompletableFuture.completedFuture(null));
        given(neovimApi.deleteCurrentLine()).willReturn(CompletableFuture.completedFuture(null));
        neovimRxWrapper.getCurrentLine()
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue("line");
        verify(neovimApi).getCurrentLine();
        neovimRxWrapper.setCurrentLine("line")
                .test()
                .assertComplete()
                .assertNoErrors();
        verify(neovimApi).setCurrentLine("line");
        neovimRxWrapper.deleteCurrentLine()
                .test()
                .assertNoErrors()
                .assertComplete();
        verify(neovimApi).deleteCurrentLine();
    }

    @Test
    public void delegatesBufferOperations() {
        var neovimBufferApi = Mockito.mock(NeovimBufferApi.class);
        var buffer = new Buffer(1);
        given(neovimBufferApi.get()).willReturn(buffer);
        given(neovimApi.getBuffers()).willReturn(CompletableFuture.completedFuture(Collections.emptyList()));
        given(neovimApi.getCurrentBuffer()).willReturn(CompletableFuture.completedFuture(neovimBufferApi));
        given(neovimApi.setCurrentBuffer(buffer)).willReturn(CompletableFuture.completedFuture(null));
        neovimRxWrapper.getBuffers()
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(List::isEmpty);
        verify(neovimApi).getBuffers();
        neovimRxWrapper.getCurrentBuffer()
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(neovimBufferRxApi -> neovimBufferRxApi.get().equals(buffer));
        verify(neovimApi).getCurrentBuffer();
        neovimRxWrapper.setCurrentBuffer(buffer)
                .test()
                .assertNoErrors()
                .assertComplete();
        verify(neovimApi).setCurrentBuffer(buffer);
    }

    @Test
    public void delegatesWindowOperations() {
        var neovimWindowApi = Mockito.mock(NeovimWindowApi.class);
        var window = new Window(1);
        given(neovimWindowApi.get()).willReturn(window);
        given(neovimApi.getWindows()).willReturn(CompletableFuture.completedFuture(Collections.emptyList()));
        given(neovimApi.getCurrentWindow()).willReturn(CompletableFuture.completedFuture(neovimWindowApi));
        given(neovimApi.setCurrentWindow(window)).willReturn(CompletableFuture.completedFuture(null));
        neovimRxWrapper.getWindows()
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(List::isEmpty);
        verify(neovimApi).getWindows();
        neovimRxWrapper.getCurrentWindow()
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(neovimWindowRxApi -> neovimWindowRxApi.get().equals(window));
        verify(neovimApi).getCurrentWindow();
        neovimRxWrapper.setCurrentWindow(window)
                .test()
                .assertNoErrors()
                .assertComplete();
        verify(neovimApi).setCurrentWindow(window);
    }

    @Test
    public void delegatesTabpageOperations() {
        var neovimTabpageApi = Mockito.mock(NeovimTabpageApi.class);
        var tabpage = new Tabpage(1);
        given(neovimTabpageApi.get()).willReturn(tabpage);
        given(neovimApi.getTabpages()).willReturn(CompletableFuture.completedFuture(Collections.emptyList()));
        given(neovimApi.getCurrentTabpage()).willReturn(CompletableFuture.completedFuture(neovimTabpageApi));
        given(neovimApi.setCurrentTabpage(tabpage)).willReturn(CompletableFuture.completedFuture(null));
        neovimRxWrapper.getTabpages()
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(List::isEmpty);
        verify(neovimApi).getTabpages();
        neovimRxWrapper.getCurrentTabpage()
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(neovimTabpageRxApi -> neovimTabpageRxApi.get().equals(tabpage));
        verify(neovimApi).getCurrentTabpage();
        neovimRxWrapper.setCurrentTabpage(tabpage)
                .test()
                .assertNoErrors()
                .assertComplete();
        verify(neovimApi).setCurrentTabpage(tabpage);
    }

    @Test
    public void delegatesGetColorMap() {
        var vimColorMap = new VimColorMap(Map.of());
        given(neovimApi.getColorMap()).willReturn(CompletableFuture.completedFuture(vimColorMap));
        neovimRxWrapper.getColorMap()
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(vimColorMap);
        verify(neovimApi).getColorMap();
    }

    @Test
    public void delegatesGetMode() {
        var vimMode = new VimMode("n", true);
        given(neovimApi.getMode()).willReturn(CompletableFuture.completedFuture(vimMode));
        neovimRxWrapper.getMode()
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(vimMode);
        verify(neovimApi).getMode();
    }

    @Test
    public void delegatesGetApiInfo() {
        var apiInfo = new ApiInfo();
        given(neovimApi.getApiInfo()).willReturn(CompletableFuture.completedFuture(apiInfo));
        neovimRxWrapper.getApiInfo()
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(apiInfo);
        verify(neovimApi).getApiInfo();
    }

    @Test
    public void delegatesCallDictFunction() {
        var map = Map.of();
        var args = List.of();
        var result = new Object();
        given(neovimApi.callDictFunction(map, "name", args)).willReturn(CompletableFuture.completedFuture(result));
        neovimRxWrapper.callDictFunction(map, "name", args)
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(result);
        verify(neovimApi).callDictFunction(map, "name", args);
    }

    @Test
    public void delegatesGetCommands() {
        var opts = new GetCommandsOptions(true);
        given(neovimApi.getCommands(opts)).willReturn(CompletableFuture.completedFuture(Map.of()));
        neovimRxWrapper.getCommands(opts)
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(Map::isEmpty);
        verify(neovimApi).getCommands(opts);
    }

    @Test
    public void delegatesSetClientInfo() {
        var versionInfo = new ClientVersionInfo(0, 0, 0, "dev");
        var methodInfoMap = Map.<String, MethodInfo>of();
        var clientAttributes = new ClientAttributes("web", "license", "logo");
        given(neovimApi.setClientInfo("name", versionInfo, ClientType.REMOTE, methodInfoMap, clientAttributes))
                .willReturn(CompletableFuture.completedFuture(null));

        neovimRxWrapper.setClientInfo("name", versionInfo, ClientType.REMOTE, methodInfoMap, clientAttributes)
                .test()
                .assertNoErrors()
                .assertComplete();
        verify(neovimApi).setClientInfo("name", versionInfo, ClientType.REMOTE, methodInfoMap, clientAttributes);
    }

    @Test
    public void delegatesChannelOperations() {
        var channelInfo = new ChannelInfo(1, ChannelInfo.Stream.STANDARD_ERROR, ChannelInfo.Mode.BYTES, null);
        given(neovimApi.getChannels()).willReturn(CompletableFuture.completedFuture(List.of()));
        given(neovimApi.getChannelInfo(1)).willReturn(CompletableFuture.completedFuture(channelInfo));
        neovimRxWrapper.getChannels()
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(List::isEmpty);
        verify(neovimApi).getChannels();
        neovimRxWrapper.getChannelInfo(1)
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(channelInfo);
        verify(neovimApi).getChannelInfo(1);
    }

    @Test
    public void delegatesParseExpression() {
        given(neovimApi.parseExpression("expr", "flags", true)).willReturn(CompletableFuture.completedFuture(Map.of()));
        neovimRxWrapper.parseExpression("expr", "flags", true)
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(Map::isEmpty);
        verify(neovimApi).parseExpression("expr", "flags", true);
    }

    @Test
    public void delegatesListUis() {
        given(neovimApi.getUis()).willReturn(CompletableFuture.completedFuture(List.of()));
        neovimRxWrapper.getUis()
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(List::isEmpty);
        verify(neovimApi).getUis();
    }

    @Test
    public void delegatesProcessOperations() {
        var result = new Object();
        given(neovimApi.getProcessChildren()).willReturn(CompletableFuture.completedFuture(List.of()));
        given(neovimApi.getProcess()).willReturn(CompletableFuture.completedFuture(result));
        neovimRxWrapper.getProcessChildren()
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(List::isEmpty);
        verify(neovimApi).getProcessChildren();
        neovimRxWrapper.getProcess()
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(result);
        verify(neovimApi).getProcess();
    }

    @Test
    public void delegatesNamespaceOperations() {
        var result = Map.of("test", 1);
        given(neovimApi.getNamespaces()).willReturn(CompletableFuture.completedFuture(result));
        given(neovimApi.createNamespace("test")).willReturn(CompletableFuture.completedFuture(1));
        neovimRxWrapper.getNamespaces()
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(result);
        verify(neovimApi).getNamespaces();
        neovimRxWrapper.createNamespace("test")
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(1);
        verify(neovimApi).createNamespace("test");
    }
}