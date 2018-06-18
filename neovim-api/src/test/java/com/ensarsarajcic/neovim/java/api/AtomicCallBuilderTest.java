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

import com.ensarsarajcic.neovim.java.api.types.api.GetCommandsOptions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class AtomicCallBuilderTest {

    @Test
    public void allMethodsReturnNullSinceUnimplemented() {
        AtomicCallBuilder atomicCallBuilder = new AtomicCallBuilder();
        assertNull(atomicCallBuilder.attachUI(1, 1, Map.of()));
        assertNull(atomicCallBuilder.callDictFunction(Map.of(), "", List.of()));
        assertNull(atomicCallBuilder.callFunction("", List.of()));
        assertNull(atomicCallBuilder.commandOutput(""));
        assertNull(atomicCallBuilder.deleteCurrentLine());
        assertNull(atomicCallBuilder.deleteVariable(""));
        assertNull(atomicCallBuilder.detachUI());
        assertNull(atomicCallBuilder.eval(""));
        assertNull(atomicCallBuilder.executeCommand(""));
        assertNull(atomicCallBuilder.executeLua("", List.of()));
        assertNull(atomicCallBuilder.feedKeys("", "", false));
        assertNull(atomicCallBuilder.getApiInfo());
        assertNull(atomicCallBuilder.getBuffers());
        assertNull(atomicCallBuilder.getChannelInfo(0));
        assertNull(atomicCallBuilder.getChannels());
        assertNull(atomicCallBuilder.getColorByName(""));
        assertNull(atomicCallBuilder.getColorMap());
        assertNull(atomicCallBuilder.getCommands(new GetCommandsOptions(false)));
        assertNull(atomicCallBuilder.getCurrentBuffer());
        assertNull(atomicCallBuilder.getCurrentLine());
        assertNull(atomicCallBuilder.getCurrentTabpage());
        assertNull(atomicCallBuilder.getCurrentWindow());
        assertNull(atomicCallBuilder.getHighlightById(1, true));
        assertNull(atomicCallBuilder.getHighlightByName("", true));
        assertNull(atomicCallBuilder.getKeymap(""));
        assertNull(atomicCallBuilder.getMode());
        assertNull(atomicCallBuilder.getOption(""));
        assertNull(atomicCallBuilder.getProcess());
        assertNull(atomicCallBuilder.getProcessChildren());
        assertNull(atomicCallBuilder.getTabpages());
        assertNull(atomicCallBuilder.getUis());
        assertNull(atomicCallBuilder.getVariable(""));
        assertNull(atomicCallBuilder.getVimVariable(""));
        assertNull(atomicCallBuilder.getWindows());
        assertNull(atomicCallBuilder.input(""));
        assertNull(atomicCallBuilder.listRuntimePaths());
        assertNull(atomicCallBuilder.parseExpression("", "", false));
        assertNull(atomicCallBuilder.prepareAtomic());
        assertNull(atomicCallBuilder.sendAtomic(null));
        assertNull(atomicCallBuilder.setClientInfo(null, null, null, null, null));
        assertNull(atomicCallBuilder.setCurrentBuffer(null));
        assertNull(atomicCallBuilder.setCurrentDir(null));
        assertNull(atomicCallBuilder.setCurrentLine(null));
        assertNull(atomicCallBuilder.setCurrentWindow(null));
        assertNull(atomicCallBuilder.setCurrentTabpage(null));
        assertNull(atomicCallBuilder.replaceTermcodes(null, false, false, false));
        assertNull(atomicCallBuilder.resizeUI(1, 1));
        assertNull(atomicCallBuilder.stringWidth(""));
        assertNull(atomicCallBuilder.subscribeToEvent(""));
        assertNull(atomicCallBuilder.unsubscribeFromEvent(""));
        assertNull(atomicCallBuilder.writelnToError(""));
        assertNull(atomicCallBuilder.writeToError(""));
        assertNull(atomicCallBuilder.writeToOutput(""));
        assertNull(atomicCallBuilder.setOption("", null));
        assertNull(atomicCallBuilder.setUiOption("", null));
        assertNull(atomicCallBuilder.setVariable("", null));
    }
}