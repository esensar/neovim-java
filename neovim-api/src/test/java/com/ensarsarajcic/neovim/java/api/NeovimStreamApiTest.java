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

import com.ensarsarajcic.neovim.java.api.types.api.*;
import com.ensarsarajcic.neovim.java.api.types.apiinfo.FunctionInfo;
import com.ensarsarajcic.neovim.java.api.types.apiinfo.ParamInfo;
import com.ensarsarajcic.neovim.java.api.types.apiinfo.TypeInfo;
import com.ensarsarajcic.neovim.java.api.types.apiinfo.UiEventInfo;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Buffer;
import com.ensarsarajcic.neovim.java.api.types.msgpack.NeovimCustomType;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Tabpage;
import com.ensarsarajcic.neovim.java.api.types.msgpack.Window;
import com.ensarsarajcic.neovim.java.corerpc.message.RPCError;
import com.ensarsarajcic.neovim.java.corerpc.message.RequestMessage;
import com.ensarsarajcic.neovim.java.corerpc.message.ResponseMessage;
import com.ensarsarajcic.neovim.java.corerpc.reactive.RPCException;
import com.ensarsarajcic.neovim.java.corerpc.reactive.ReactiveRPCStreamer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.msgpack.jackson.dataformat.MessagePackExtensionType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
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
        } catch (UnsupportedOperationException ex) {

        }
        try {
            neovimStreamApi.sendAtomic(null);
            fail("Should have thrown unsupported operation exception");
        } catch (UnsupportedOperationException ex) {

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
                request -> assertMethodAndArguments(request, NeovimApi.ATTACH_UI, 500, 500, opts)
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
                request -> assertMethodAndArguments(request, NeovimApi.RESIZE_UI, 500, 500)
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
                request -> assertMethodAndArguments(request, NeovimApi.EXECUTE_COMMAND, "real vim command")
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
                request -> assertMethodAndArguments(request, NeovimApi.SET_CURRENT_DIR, "/home")
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
                request -> assertMethodAndArguments(request, NeovimApi.SUBSCRIBE_TO_EVENT, "real event")
        );

        // Error case
        assertErrorBehavior(
                () -> neovimStreamApi.subscribeToEvent("bad event"),
                request -> assertMethodAndArguments(request, NeovimApi.SUBSCRIBE_TO_EVENT, "bad event")
        );
    }

    @Test
    public void unsubscribeFromEventTest() throws InterruptedException, ExecutionException {
        // Happy case
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, null)),
                () -> neovimStreamApi.unsubscribeFromEvent("real event"),
                request -> assertMethodAndArguments(request, NeovimApi.UNSUBSCRIBE_FROM_EVENT, "real event")
        );

        // Error case
        assertErrorBehavior(
                () -> neovimStreamApi.unsubscribeFromEvent("bad event"),
                request -> assertMethodAndArguments(request, NeovimApi.UNSUBSCRIBE_FROM_EVENT, "bad event")
        );
    }

    @Test
    public void evalTest() throws InterruptedException, ExecutionException {
        // Happy case
        Object expectedResult = "expression result";
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, expectedResult)),
                () -> neovimStreamApi.eval("real expression"),
                request -> assertMethodAndArguments(request, NeovimApi.EVAL, "real expression"),
                result -> assertEquals(expectedResult, result)
        );

        // Error case
        assertErrorBehavior(
                () -> neovimStreamApi.eval("bad expression"),
                request -> assertMethodAndArguments(request, NeovimApi.EVAL, "bad expression")
        );
    }

    @Test
    public void callFunctionTest() throws InterruptedException, ExecutionException {
        // Happy case
        Object expectedResult = "function result";
        List<String> args = List.of("arg1", "arg2");
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, expectedResult)),
                () -> neovimStreamApi.callFunction("the function", args),
                request -> assertMethodAndArguments(request, NeovimApi.CALL_FUNCTION, "the function", args),
                result -> assertEquals(expectedResult, result)
        );

        // Error case
        List<String> badArgs = List.of("ont bad arg");
        assertErrorBehavior(
                () -> neovimStreamApi.callFunction("bad function", badArgs),
                request -> assertMethodAndArguments(request, NeovimApi.CALL_FUNCTION, "bad function", badArgs)
        );
    }

    @Test
    public void feedKeysTest() throws InterruptedException, ExecutionException {
        // Happy case
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, null)),
                () -> neovimStreamApi.feedKeys("abcd", "n", true),
                request -> assertMethodAndArguments(request, NeovimApi.FEEDKEYS, "abcd", "n", true)
        );

        // Error case
        assertErrorBehavior(
                () -> neovimStreamApi.feedKeys("keys", "o", false),
                request -> assertMethodAndArguments(request, NeovimApi.FEEDKEYS, "keys", "o", false)
        );
    }

    @Test
    public void inputTest() throws InterruptedException, ExecutionException {
        // Happy case
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, 5)),
                () -> neovimStreamApi.input("abcd"),
                request -> assertMethodAndArguments(request, NeovimApi.INPUT, "abcd"),
                result -> assertEquals(5, (int) result)
        );

        // Error case
        assertErrorBehavior(
                () -> neovimStreamApi.input("keys"),
                request -> assertMethodAndArguments(request, NeovimApi.INPUT, "keys")
        );
    }

    @Test
    public void getKeymapTest() throws InterruptedException, ExecutionException {
        // Happy case
        List<Map> vimKeyMaps = List.of(
                Map.of(
                        "silent", 1,
                        "noremap", 1,
                        "lhs", "keys",
                        "rhs", "action",
                        "mode", "n",
                        "nowait", 1,
                        "expr", 1,
                        "sid", 1,
                        "buffer", 1
                ));
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, vimKeyMaps)),
                () -> neovimStreamApi.getKeymap("n"),
                request -> assertMethodAndArguments(request, NeovimApi.GET_KEYMAP, "n"),
                result -> {
                    VimKeyMap vimKeyMap = result.get(0);
                    assertTrue(vimKeyMap.isSilent());
                    assertTrue(vimKeyMap.isNoRemap());
                    assertTrue(vimKeyMap.isBuffer());
                    assertTrue(vimKeyMap.isExpr());
                    assertTrue(vimKeyMap.isNoWait());
                    assertEquals(vimKeyMap.getKeyStroke(), "keys");
                    assertEquals(vimKeyMap.getActionExpression(), "action");
                    assertEquals(vimKeyMap.getMode(), "n");
                    // Assert to string does not crash
                    vimKeyMap.toString();
                }
        );

        // Error case
        assertErrorBehavior(
                () -> neovimStreamApi.getKeymap("o"),
                request -> assertMethodAndArguments(request, NeovimApi.GET_KEYMAP, "o")
        );
    }

    @Test
    public void setUiOptionTest() throws InterruptedException, ExecutionException {
        // Happy case
        String optionName = "name";
        Object optionValue = "VALUE";
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, null)),
                () -> neovimStreamApi.setUiOption(optionName, optionValue),
                request -> assertMethodAndArguments(request, NeovimApi.SET_UI_OPTION, optionName, optionValue)
        );

        // Error case
        String badName = "badName";
        Object badValue = "badValue";
        assertErrorBehavior(
                () -> neovimStreamApi.setUiOption(badName, badValue),
                request -> assertMethodAndArguments(request, NeovimApi.SET_UI_OPTION, badName, badValue)
        );
    }

    @Test
    public void setVariableTest() throws InterruptedException, ExecutionException {
        // Happy case
        String varName = "name";
        Object varValue = "VALUE";
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, null)),
                () -> neovimStreamApi.setVariable(varName, varValue),
                request -> assertMethodAndArguments(request, NeovimApi.SET_VAR, varName, varValue)
        );

        // Error case
        String badVarName = "badName";
        Object badValue = "badValue";
        assertErrorBehavior(
                () -> neovimStreamApi.setVariable(badVarName, badValue),
                request -> assertMethodAndArguments(request, NeovimApi.SET_VAR, badVarName, badValue)
        );
    }

    @Test
    public void deleteVariableTest() throws InterruptedException, ExecutionException {
        // Happy case
        String varName = "name";
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, null)),
                () -> neovimStreamApi.deleteVariable(varName),
                request -> assertMethodAndArguments(request, NeovimApi.DEL_VAR, varName)
        );

        // Error case
        String badVarName = "badName";
        assertErrorBehavior(
                () -> neovimStreamApi.deleteVariable(badVarName),
                request -> assertMethodAndArguments(request, NeovimApi.DEL_VAR, badVarName)
        );
    }

    @Test
    public void getVariableTest() throws InterruptedException, ExecutionException {
        // Happy case
        String varName = "name";
        Object varValue = "value";
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, varValue)),
                () -> neovimStreamApi.getVariable(varName),
                request -> assertMethodAndArguments(request, NeovimApi.GET_VAR, varName),
                result -> assertEquals(varValue, result)
        );

        // Error case
        String badVarName = "badName";
        assertErrorBehavior(
                () -> neovimStreamApi.getVariable(badVarName),
                request -> assertMethodAndArguments(request, NeovimApi.GET_VAR, badVarName)
        );
    }

    @Test
    public void getVimVariableTest() throws InterruptedException, ExecutionException {
        // Happy case
        String varName = "name";
        Object varValue = "value";
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, varValue)),
                () -> neovimStreamApi.getVimVariable(varName),
                request -> assertMethodAndArguments(request, NeovimApi.GET_VIM_VARIABLE, varName),
                result -> assertEquals(varValue, result)
        );

        // Error case
        String badVarName = "badName";
        assertErrorBehavior(
                () -> neovimStreamApi.getVimVariable(badVarName),
                request -> assertMethodAndArguments(request, NeovimApi.GET_VIM_VARIABLE, badVarName)
        );
    }

    @Test
    public void setOptionTest() throws InterruptedException, ExecutionException {
        // Happy case
        String optName = "name";
        Object optValue = "VALUE";
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, null)),
                () -> neovimStreamApi.setOption(optName, optValue),
                request -> assertMethodAndArguments(request, NeovimApi.SET_OPTION, optName, optValue)
        );

        // Error case
        String badOptName = "badName";
        Object badOptValue = "badValue";
        assertErrorBehavior(
                () -> neovimStreamApi.setOption(badOptName, badOptValue),
                request -> assertMethodAndArguments(request, NeovimApi.SET_OPTION, badOptName, badOptValue)
        );
    }

    @Test
    public void getOptionTest() throws InterruptedException, ExecutionException {
        // Happy case
        String optName = "name";
        Object optValue = "value";
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, optValue)),
                () -> neovimStreamApi.getOption(optName),
                request -> assertMethodAndArguments(request, NeovimApi.GET_OPTION, optName),
                result -> assertEquals(optValue, result)
        );

        // Error case
        String badOptName = "badName";
        assertErrorBehavior(
                () -> neovimStreamApi.getOption(badOptName),
                request -> assertMethodAndArguments(request, NeovimApi.GET_OPTION, badOptName)
        );
    }

    @Test
    public void getColorByNameTest() throws InterruptedException, ExecutionException {
        // Happy case
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, 123)),
                () -> neovimStreamApi.getColorByName("name"),
                request -> assertMethodAndArguments(request, NeovimApi.GET_COLOR_BY_NAME, "name"),
                result -> assertEquals(123, (int) result)
        );

        // Error case
        assertErrorBehavior(
                () -> neovimStreamApi.getColorByName("bad name"),
                request -> assertMethodAndArguments(request, NeovimApi.GET_COLOR_BY_NAME, "bad name")
        );
    }

    @Test
    public void replaceTermcodesTest() throws InterruptedException, ExecutionException {
        // Happy case
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, "replaced")),
                () -> neovimStreamApi.replaceTermcodes("str", true, true, true),
                request -> assertMethodAndArguments(request, NeovimApi.REPLACE_TERMCODES, "str", true, true, true),
                result -> assertEquals("replaced", result)
        );

        // Error case
        assertErrorBehavior(
                () -> neovimStreamApi.replaceTermcodes("bad str", true, true, true),
                request -> assertMethodAndArguments(request, NeovimApi.REPLACE_TERMCODES, "bad str")
        );
    }

    @Test
    public void commandOutputTest() throws InterruptedException, ExecutionException {
        // Happy case
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, "output")),
                () -> neovimStreamApi.commandOutput("command"),
                request -> assertMethodAndArguments(request, NeovimApi.COMMAND_OUTPUT, "command"),
                result -> assertEquals("output", result)
        );

        // Error case
        assertErrorBehavior(
                () -> neovimStreamApi.commandOutput("bad command"),
                request -> assertMethodAndArguments(request, NeovimApi.COMMAND_OUTPUT, "bad command")
        );
    }


    @Test
    public void writeToOutputTest() throws InterruptedException, ExecutionException {
        // Happy case
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, null)),
                () -> neovimStreamApi.writeToOutput("some text"),
                request -> assertMethodAndArguments(request, NeovimApi.OUT_WRITE, "some text")
        );

        // Error case
        assertErrorBehavior(
                () -> neovimStreamApi.writeToOutput("bad text"),
                request -> assertMethodAndArguments(request, NeovimApi.OUT_WRITE, "bad text")
        );
    }

    @Test
    public void writeToErrorTest() throws InterruptedException, ExecutionException {
        // Happy case
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, null)),
                () -> neovimStreamApi.writeToError("some text"),
                request -> assertMethodAndArguments(request, NeovimApi.ERR_WRITE, "some text")
        );

        // Error case
        assertErrorBehavior(
                () -> neovimStreamApi.writeToError("bad text"),
                request -> assertMethodAndArguments(request, NeovimApi.ERR_WRITE, "bad text")
        );
    }

    @Test
    public void writeLnToErrorTest() throws InterruptedException, ExecutionException {
        // Happy case
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, null)),
                () -> neovimStreamApi.writelnToError("some text"),
                request -> assertMethodAndArguments(request, NeovimApi.ERR_WRITELN, "some text")
        );

        // Error case
        assertErrorBehavior(
                () -> neovimStreamApi.writelnToError("bad text"),
                request -> assertMethodAndArguments(request, NeovimApi.ERR_WRITELN, "bad text")
        );
    }

    @Test
    public void stringWidthTest() throws InterruptedException, ExecutionException {
        // Happy case
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, 9)),
                () -> neovimStreamApi.stringWidth("some text"),
                request -> assertMethodAndArguments(request, NeovimApi.STRWIDTH, "some text"),
                result -> assertEquals(9, (int) result)
        );

        // Error case
        assertErrorBehavior(
                () -> neovimStreamApi.stringWidth("bad text"),
                request -> assertMethodAndArguments(request, NeovimApi.STRWIDTH, "bad text")
        );
    }

    @Test
    public void listRuntimePathsTest() throws InterruptedException, ExecutionException {
        // Happy case
        List<String> list = List.of("runtime path");
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, list)),
                () -> neovimStreamApi.listRuntimePaths(),
                request -> assertMethodAndArguments(request, NeovimApi.LIST_RUNTIME_PATHS),
                result -> assertEquals(list, result)
        );

        // Error case
        assertErrorBehavior(
                () -> neovimStreamApi.listRuntimePaths(),
                request -> assertMethodAndArguments(request, NeovimApi.LIST_RUNTIME_PATHS)
        );
    }

    @Test
    public void getCurrentLineTest() throws InterruptedException, ExecutionException {
        // Happy case
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, "Current line")),
                () -> neovimStreamApi.getCurrentLine(),
                request -> assertMethodAndArguments(request, NeovimApi.GET_CURRENT_LINE),
                result -> assertEquals("Current line", result)
        );

        // Error case
        assertErrorBehavior(
                () -> neovimStreamApi.getCurrentLine(),
                request -> assertMethodAndArguments(request, NeovimApi.GET_CURRENT_LINE)
        );
    }

    @Test
    public void setCurrentLineTest() throws InterruptedException, ExecutionException {
        // Happy case
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, null)),
                () -> neovimStreamApi.setCurrentLine("content"),
                request -> assertMethodAndArguments(request, NeovimApi.SET_CURRENT_LINE, "content")
        );

        // Error case
        assertErrorBehavior(
                () -> neovimStreamApi.setCurrentLine("badcontent"),
                request -> assertMethodAndArguments(request, NeovimApi.SET_CURRENT_LINE, "badcontent")
        );
    }

    @Test
    public void deleteCurrentLineTest() throws InterruptedException, ExecutionException {
        // Happy case
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, null)),
                () -> neovimStreamApi.deleteCurrentLine(),
                request -> assertMethodAndArguments(request, NeovimApi.DEL_CURRENT_LINE)
        );

        // Error case
        assertErrorBehavior(
                () -> neovimStreamApi.deleteCurrentLine(),
                request -> assertMethodAndArguments(request, NeovimApi.DEL_CURRENT_LINE)
        );
    }

    @Test
    public void getBuffersTest() throws InterruptedException, ExecutionException {
        // Happy case
        List<MessagePackExtensionType> buffers = List.of(
                new MessagePackExtensionType((byte) NeovimCustomType.BUFFER.getTypeId(), new byte[]{1}),
                new MessagePackExtensionType((byte) NeovimCustomType.BUFFER.getTypeId(), new byte[]{2}),
                new MessagePackExtensionType((byte) NeovimCustomType.BUFFER.getTypeId(), new byte[]{3})
        );
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, buffers)),
                () -> neovimStreamApi.getBuffers(),
                request -> assertMethodAndArguments(request, NeovimApi.LIST_BUFS),
                result -> {
                    assertEquals(1, result.get(0).get().getId());
                    assertEquals(2, result.get(1).get().getId());
                    assertEquals(3, result.get(2).get().getId());
                }
        );

        // Error case
        assertErrorBehavior(
                () -> neovimStreamApi.getBuffers(),
                request -> assertMethodAndArguments(request, NeovimApi.LIST_BUFS)
        );
    }

    @Test
    public void getCurrentBufferTest() throws InterruptedException, ExecutionException {
        // Happy case
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, new MessagePackExtensionType((byte) NeovimCustomType.BUFFER.getTypeId(), new byte[]{1}))),
                () -> neovimStreamApi.getCurrentBuffer(),
                request -> assertMethodAndArguments(request, NeovimApi.GET_CURRENT_BUF),
                result -> assertEquals(1, result.get().getId())
        );

        // Error case
        assertErrorBehavior(
                () -> neovimStreamApi.getCurrentBuffer(),
                request -> assertMethodAndArguments(request, NeovimApi.GET_CURRENT_BUF)
        );
    }

    @Test
    public void setCurrentBufferTest() throws InterruptedException, ExecutionException {
        // Happy case
        Buffer buffer = new Buffer(1);
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, null)),
                () -> neovimStreamApi.setCurrentBuffer(buffer),
                request -> assertMethodAndArguments(request, NeovimApi.SET_CURRENT_BUF)
        );

        // Error case
        Buffer badBuffer = new Buffer(4);
        assertErrorBehavior(
                () -> neovimStreamApi.setCurrentBuffer(badBuffer),
                request -> assertMethodAndArguments(request, NeovimApi.SET_CURRENT_BUF)
        );
    }

    @Test
    public void getWindowsTest() throws InterruptedException, ExecutionException {
        // Happy case
        List<MessagePackExtensionType> windows = List.of(
                new MessagePackExtensionType((byte) NeovimCustomType.WINDOW.getTypeId(), new byte[]{1}),
                new MessagePackExtensionType((byte) NeovimCustomType.WINDOW.getTypeId(), new byte[]{2}),
                new MessagePackExtensionType((byte) NeovimCustomType.WINDOW.getTypeId(), new byte[]{3}),
                new MessagePackExtensionType((byte) NeovimCustomType.WINDOW.getTypeId(), new byte[]{4}),
                new MessagePackExtensionType((byte) NeovimCustomType.WINDOW.getTypeId(), new byte[]{5})
        );
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, windows)),
                () -> neovimStreamApi.getWindows(),
                request -> assertMethodAndArguments(request, NeovimApi.LIST_WINS),
                result -> {
                    assertEquals(1, result.get(0).get().getId());
                    assertEquals(2, result.get(1).get().getId());
                    assertEquals(3, result.get(2).get().getId());
                    assertEquals(4, result.get(3).get().getId());
                    assertEquals(5, result.get(4).get().getId());
                }
        );

        // Error case
        assertErrorBehavior(
                () -> neovimStreamApi.getWindows(),
                request -> assertMethodAndArguments(request, NeovimApi.LIST_WINS)
        );
    }

    @Test
    public void getCurrentWindowTest() throws InterruptedException, ExecutionException {
        // Happy case
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, new MessagePackExtensionType((byte) NeovimCustomType.WINDOW.getTypeId(), new byte[]{5}))),
                () -> neovimStreamApi.getCurrentWindow(),
                request -> assertMethodAndArguments(request, NeovimApi.GET_CURRENT_WIN),
                result -> assertEquals(5, result.get().getId())
        );

        // Error case
        assertErrorBehavior(
                () -> neovimStreamApi.getCurrentWindow(),
                request -> assertMethodAndArguments(request, NeovimApi.GET_CURRENT_WIN)
        );
    }

    @Test
    public void setCurrentWindowTest() throws InterruptedException, ExecutionException {
        // Happy case
        Window window = new Window(3);
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, null)),
                () -> neovimStreamApi.setCurrentWindow(window),
                request -> assertMethodAndArguments(request, NeovimApi.SET_CURRENT_WIN)
        );

        // Error case
        Window badWindow = new Window(7);
        assertErrorBehavior(
                () -> neovimStreamApi.setCurrentWindow(badWindow),
                request -> assertMethodAndArguments(request, NeovimApi.SET_CURRENT_WIN)
        );
    }

    @Test
    public void getTabpagesTest() throws InterruptedException, ExecutionException {
        // Happy case
        List<MessagePackExtensionType> tabpages = List.of(
                new MessagePackExtensionType((byte) NeovimCustomType.TABPAGE.getTypeId(), new byte[]{1}),
                new MessagePackExtensionType((byte) NeovimCustomType.TABPAGE.getTypeId(), new byte[]{2})
        );
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, tabpages)),
                () -> neovimStreamApi.getTabpages(),
                request -> assertMethodAndArguments(request, NeovimApi.LIST_TABPAGES),
                result -> {
                    assertEquals(1, result.get(0).get().getId());
                    assertEquals(2, result.get(1).get().getId());
                }
        );

        // Error case
        assertErrorBehavior(
                () -> neovimStreamApi.getTabpages(),
                request -> assertMethodAndArguments(request, NeovimApi.LIST_TABPAGES)
        );
    }

    @Test
    public void getCurrentTabpageTest() throws InterruptedException, ExecutionException {
        // Happy case
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, new MessagePackExtensionType((byte) NeovimCustomType.TABPAGE.getTypeId(), new byte[]{2}))),
                () -> neovimStreamApi.getCurrentTabpage(),
                request -> assertMethodAndArguments(request, NeovimApi.GET_CURRENT_TABPAGE),
                result -> assertEquals(2, result.get().getId())
        );

        // Error case
        assertErrorBehavior(
                () -> neovimStreamApi.getCurrentTabpage(),
                request -> assertMethodAndArguments(request, NeovimApi.GET_CURRENT_TABPAGE)
        );
    }

    @Test
    public void setCurrentTabpageTest() throws InterruptedException, ExecutionException {
        // Happy case
        Tabpage tabpage = new Tabpage(4);
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, null)),
                () -> neovimStreamApi.setCurrentTabpage(tabpage),
                request -> assertMethodAndArguments(request, NeovimApi.SET_CURRENT_TABPAGE)
        );

        // Error case
        Tabpage badTabpage = new Tabpage(2);
        assertErrorBehavior(
                () -> neovimStreamApi.setCurrentTabpage(badTabpage),
                request -> assertMethodAndArguments(request, NeovimApi.SET_CURRENT_TABPAGE)
        );
    }

    @Test
    public void getColormapTest() throws InterruptedException, ExecutionException {
        // Happy case
        Map<String, Integer> colorMap = Map.of(
                "bg", 1,
                "fg", 3
        );
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, colorMap)),
                () -> neovimStreamApi.getColorMap(),
                request -> assertMethodAndArguments(request, NeovimApi.GET_COLOR_MAP),
                result -> {
                    assertEquals(1, result.getColorMap().get("bg").intValue());
                    assertEquals(3, result.getColorMap().get("fg").intValue());

                    // Ensure to string doesn't crash
                    result.toString();
                }
        );

        // Error case
        assertErrorBehavior(
                () -> neovimStreamApi.getColorMap(),
                request -> assertMethodAndArguments(request, NeovimApi.GET_COLOR_MAP)
        );
    }

    @Test
    public void getModeTest() throws InterruptedException, ExecutionException {
        // Happy case
        Map modeMap = Map.of(
                "mode", "n",
                "blocking", false
        );
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, modeMap)),
                () -> neovimStreamApi.getMode(),
                request -> assertMethodAndArguments(request, NeovimApi.GET_MODE),
                result -> {
                    assertEquals("n", result.getMode());
                    assertFalse(result.isBlocking());

                    // Ensure to string doesn't crash
                    result.toString();
                }
        );

        // Error case
        assertErrorBehavior(
                () -> neovimStreamApi.getMode(),
                request -> assertMethodAndArguments(request, NeovimApi.GET_MODE)
        );
    }

    @Test
    public void getApiInfoTest() throws InterruptedException, ExecutionException {
        // Happy case
        List apiInfo = List.of(
                1,
                Map.of(
                        "error_types", Map.of("Exception", Map.of("id", 0)),
                        "functions", List.of(
                                Map.of(
                                        "method", true,
                                        "name", "method_name",
                                        "parameters", List.of(List.of("String", "param1"), List.of("Integer", "param2")),
                                        "return_type", "Integer",
                                        "since", 4
                                ),
                                Map.of(
                                        "method", false,
                                        "name", "old_method_name",
                                        "parameters", List.of(),
                                        "return_type", "String",
                                        "since", 0,
                                        "deprecated_since", 1
                                )
                        ),
                        "types", Map.of("Buffer", Map.of("id", 0, "prefix", "nvim_buf_")),
                        "ui_events", List.of(
                                Map.of(
                                        "name", "event_name",
                                        "parameters", List.of(List.of("String", "param1")),
                                        "since", 4
                                )
                        ),
                        "ui_options", List.of("opt1", "opt2"),
                        "version", Map.of(
                                "api_compatible", 0,
                                "api_level", 4,
                                "api_prerelease", false,
                                "major", 0,
                                "minor", 3,
                                "patch", 1
                        )
                )
        );
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, apiInfo)),
                () -> neovimStreamApi.getApiInfo(),
                request -> assertMethodAndArguments(request, NeovimApi.GET_API_INFO),
                result -> {
                    assertEquals(1, (int) result.getChannelId());

                    // Functions
                    assertEquals(2, result.getFunctions().size());
                    FunctionInfo newFunctionInfo = result.getFunctions().get(0);
                    assertTrue(newFunctionInfo.isMethod());
                    assertEquals("method_name", newFunctionInfo.getName());
                    assertEquals("Integer", newFunctionInfo.getReturnType());
                    assertEquals(2, newFunctionInfo.getParameters().size());
                    assertEquals(4, newFunctionInfo.getSince());
                    assertEquals(0, newFunctionInfo.getDeprecatedSince());
                    ParamInfo firstParam = newFunctionInfo.getParameters().get(0);
                    ParamInfo secondParam = newFunctionInfo.getParameters().get(1);
                    assertEquals("String", firstParam.getType());
                    assertEquals("param1", firstParam.getName());
                    assertEquals("Integer", secondParam.getType());
                    assertEquals("param2", secondParam.getName());
                    // Ensure to string doesn't crash
                    firstParam.toString();
                    secondParam.toString();
                    FunctionInfo oldFunctionInfo = result.getFunctions().get(1);
                    assertFalse(oldFunctionInfo.isMethod());
                    assertEquals("old_method_name", oldFunctionInfo.getName());
                    assertEquals("String", oldFunctionInfo.getReturnType());
                    assertTrue(oldFunctionInfo.getParameters().isEmpty());
                    assertEquals(0, oldFunctionInfo.getSince());
                    assertEquals(1, oldFunctionInfo.getDeprecatedSince());
                    // Ensure to string doesn't crash
                    newFunctionInfo.toString();
                    oldFunctionInfo.toString();

                    // Types
                    assertEquals(1, result.getTypes().size());
                    TypeInfo typeInfo = result.getTypes().get(0);
                    assertEquals("Buffer", typeInfo.getName());
                    assertEquals("nvim_buf_", typeInfo.getPrefix());
                    assertEquals(0, typeInfo.getId());
                    // Ensure to string doesn't crash
                    typeInfo.toString();

                    // UI Events
                    assertEquals(1, result.getUiEvents().size());
                    UiEventInfo uiEventInfo = result.getUiEvents().get(0);
                    assertEquals("event_name", uiEventInfo.getName());
                    assertEquals(4, uiEventInfo.getSince());
                    assertEquals(1, uiEventInfo.getParameters().size());
                    ParamInfo paramInfo = uiEventInfo.getParameters().get(0);
                    assertEquals("String", paramInfo.getType());
                    assertEquals("param1", paramInfo.getName());
                    // Ensure to string doesn't crash
                    uiEventInfo.toString();

                    // UI Options
                    assertEquals(2, result.getUiOptions().size());
                    assertEquals("opt1", result.getUiOptions().get(0));
                    assertEquals("opt2", result.getUiOptions().get(1));

                    // Version
                    assertEquals(0, result.getVersion().getCompatible());
                    assertEquals(4, result.getVersion().getLevel());
                    assertEquals(0, result.getVersion().getMajor());
                    assertEquals(3, result.getVersion().getMinor());
                    assertEquals(1, result.getVersion().getPatch());
                    assertFalse(result.getVersion().isPreRelease());
                    assertEquals("0.3.1", result.getVersion().getVersionString());
                    // Ensure to string doesn't crash
                    result.getVersion().toString();
//
                    // Ensure to string doesn't crash
                    result.toString();
                }
        );

        // Error case
        assertErrorBehavior(
                () -> neovimStreamApi.getApiInfo(),
                request -> assertMethodAndArguments(request, NeovimApi.GET_API_INFO)
        );
    }

    @Test
    public void callDictFunctionTest() throws InterruptedException, ExecutionException {
        // Happy case
        Map map = Map.of(
                "par1", "n"
        );
        List args = List.of("arg1", "arg2");
        String function = "function_name";
        Object callResult = "result of function";
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, callResult)),
                () -> neovimStreamApi.callDictFunction(map, function, args),
                request -> assertMethodAndArguments(request, NeovimApi.CALL_DICT_FUNCTION, map, function, args),
                result -> assertEquals(callResult, result)
        );

        // Error case
        Map badMap = Map.of(
                "par6", "null"
        );
        List badArgs = List.of();
        String badFunction = "bad_func";
        assertErrorBehavior(
                () -> neovimStreamApi.callDictFunction(badMap, badFunction, badArgs),
                request -> assertMethodAndArguments(request, NeovimApi.CALL_DICT_FUNCTION, badMap, badFunction, badArgs)
        );
    }

    @Test
    public void getCommandsTest() throws InterruptedException, ExecutionException {
        // Happy case
        Map map = Map.of(
                "command1", "realcommand"
        );
        GetCommandsOptions commandsOptions = new GetCommandsOptions(false);
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, map)),
                () -> neovimStreamApi.getCommands(commandsOptions),
                request -> assertMethodAndArguments(request, NeovimApi.GET_COMMANDS, commandsOptions),
                result -> assertEquals(map, result)
        );

        // Error case
        GetCommandsOptions badOptions = new GetCommandsOptions(true);
        assertErrorBehavior(
                () -> neovimStreamApi.getCommands(badOptions),
                request -> assertMethodAndArguments(request, NeovimApi.GET_COMMANDS, badOptions)
        );
    }

    @Test
    public void setClientInfoTest() throws InterruptedException, ExecutionException {
        // Happy case
        ClientVersionInfo clientVersionInfo = new ClientVersionInfo(1, 1, 1, "dev");
        // Ensure to string doesn't crash
        clientVersionInfo.toString();
        ClientType clientType = ClientType.REMOTE;
        Map<String, MethodInfo> methodInfoMap = Map.of(
                "method_name", new MethodInfo(false, 3),
                "method_2_name", new MethodInfo(true, 1)
        );
        ClientAttributes clientAttributes = new ClientAttributes(
                "web",
                "MIT",
                "logo"
        );
        // Ensure to string doesn't crash
        clientAttributes.toString();
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, null)),
                () -> neovimStreamApi.setClientInfo("name", clientVersionInfo, clientType, methodInfoMap, clientAttributes),
                request -> assertMethodAndArguments(request, NeovimApi.SET_CLIENT_INFO, "name", clientVersionInfo, clientType, methodInfoMap, clientAttributes)
        );

        // Error case
        ClientVersionInfo badClientVersionInfo = new ClientVersionInfo(1, 1, 1, "dev");
        ClientType badClientType = ClientType.REMOTE;
        Map<String, MethodInfo> badMethodInfoMap = Map.of(
                "method_name", new MethodInfo(false, 3),
                "method_2_name", new MethodInfo(true, 1)
        );
        ClientAttributes badClientAttributes = new ClientAttributes(
                "web",
                "MIT",
                "logo"
        );
        assertErrorBehavior(
                () -> neovimStreamApi.setClientInfo("wrong", badClientVersionInfo, badClientType, badMethodInfoMap, badClientAttributes),
                request -> assertMethodAndArguments(request, NeovimApi.SET_CLIENT_INFO, "wrong", badClientVersionInfo, badClientType, badMethodInfoMap, badClientAttributes)
        );
    }

    @Test
    public void getChannelInfoTest() throws InterruptedException, ExecutionException {
        // Happy case
        Map channelInfo = Map.of(
                "id", 1,
                "stream", "socket",
                "mode", "rpc",
                "client", Map.of(
                        "name", "client_name",
                        "version", Map.of(
                                "major", 1,
                                "minor", 2,
                                "patch", 3,
                                "prerelease", "dev"
                        ),
                        "type", "remote",
                        "methods", Map.of(),
                        "attributes", Map.of()
                )
        );
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, channelInfo)),
                () -> neovimStreamApi.getChannelInfo(1),
                request -> assertMethodAndArguments(request, NeovimApi.GET_CHANNEL_INFO, 1),
                result -> {
                    assertEquals(1, result.getId());
                    assertEquals(ChannelInfo.Stream.SOCKET, result.getStream());
                    assertEquals(ChannelInfo.Mode.RPC, result.getMode());
                    assertNotNull(result.getClient());
                    ClientInfo clientInfo = result.getClient();
                    assertEquals("client_name", clientInfo.getName());
                    assertEquals(ClientType.REMOTE, clientInfo.getType());
                    assertTrue(clientInfo.getMethods().isEmpty());
                    assertNotNull(clientInfo.getVersion());
                    ClientVersionInfo clientVersionInfo = clientInfo.getVersion();
                    assertEquals(1, clientVersionInfo.getMajor());
                    assertEquals(2, clientVersionInfo.getMinor());
                    assertEquals(3, clientVersionInfo.getPatch());
                    assertEquals("dev", clientVersionInfo.getPreRelease());

                    ClientAttributes clientAttributes = clientInfo.getAttributes();
                    assertNull(clientAttributes.getWebsite());
                    assertNull(clientAttributes.getLicense());
                    assertNull(clientAttributes.getLogo());
                    // Ensure to string doesn't crash
                    clientVersionInfo.toString();
                    clientInfo.toString();
                    clientAttributes.toString();
                    result.toString();
                }
        );

        // Error case
        assertErrorBehavior(
                () -> neovimStreamApi.getChannelInfo(1),
                request -> assertMethodAndArguments(request, NeovimApi.GET_CHANNEL_INFO, 1)
        );
    }

    @Test
    public void getChannelsTest() throws InterruptedException, ExecutionException {
        // Happy case
        List<Map> channels = List.of(
                Map.of(
                        "id", 1,
                        "stream", "socket",
                        "mode", "rpc",
                        "client", Map.of(
                                "name", "client_name",
                                "version", Map.of(
                                        "major", 1,
                                        "minor", 2,
                                        "patch", 3,
                                        "prerelease", "dev"
                                ),
                                "type", "remote",
                                "methods", Map.of(),
                                "attributes", Map.of()
                        )
                ),
                Map.of(
                        "id", 2,
                        "stream", "stdio",
                        "mode", "bytes"
                ),
                Map.of(
                        "id", 3,
                        "stream", "stderr",
                        "mode", "terminal"
                ),
                Map.of(
                        "id", 4,
                        "stream", "job",
                        "mode", "pty"
                )
        );
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, channels)),
                () -> neovimStreamApi.getChannels(),
                request -> assertMethodAndArguments(request, NeovimApi.LIST_CHANNELS),
                result -> {
                    assertEquals(4, result.size());
                    ChannelInfo firstResult = result.get(0);
                    assertEquals(1, firstResult.getId());
                    assertEquals(ChannelInfo.Stream.SOCKET, firstResult.getStream());
                    assertEquals(ChannelInfo.Mode.RPC, firstResult.getMode());
                    assertNotNull(firstResult.getClient());
                    ClientInfo clientInfo = firstResult.getClient();
                    assertEquals("client_name", clientInfo.getName());
                    assertEquals(ClientType.REMOTE, clientInfo.getType());
                    assertTrue(clientInfo.getMethods().isEmpty());
                    assertNotNull(clientInfo.getVersion());
                    ClientVersionInfo clientVersionInfo = clientInfo.getVersion();
                    assertEquals(1, clientVersionInfo.getMajor());
                    assertEquals(2, clientVersionInfo.getMinor());
                    assertEquals(3, clientVersionInfo.getPatch());
                    assertEquals("dev", clientVersionInfo.getPreRelease());

                    ClientAttributes clientAttributes = clientInfo.getAttributes();
                    assertNull(clientAttributes.getWebsite());
                    assertNull(clientAttributes.getLicense());
                    assertNull(clientAttributes.getLogo());

                    assertEquals(2, result.get(1).getId());
                    assertEquals(3, result.get(2).getId());
                    assertEquals(4, result.get(3).getId());
                    assertEquals(ChannelInfo.Stream.STANDARD_IO, result.get(1).getStream());
                    assertEquals(ChannelInfo.Stream.STANDARD_ERROR, result.get(2).getStream());
                    assertEquals(ChannelInfo.Stream.JOB, result.get(3).getStream());
                    assertEquals(ChannelInfo.Mode.BYTES, result.get(1).getMode());
                    assertEquals(ChannelInfo.Mode.TERMINAL, result.get(2).getMode());
                    assertEquals(ChannelInfo.Mode.PSEUDOTERMINAL, result.get(3).getMode());
                    // Ensure to string doesn't crash
                    clientVersionInfo.toString();
                    clientInfo.toString();
                    clientAttributes.toString();
                    firstResult.toString();
                    result.toString();
                }
        );

        // Error case
        assertErrorBehavior(
                () -> neovimStreamApi.getChannels(),
                request -> assertMethodAndArguments(request, NeovimApi.LIST_CHANNELS)
        );
    }

    @Test
    public void parseExpressionTest() throws InterruptedException, ExecutionException {
        // Happy case
        Map map = Map.of(
                "expr", "result"
        );
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, map)),
                () -> neovimStreamApi.parseExpression("expr", "flags", true),
                request -> assertMethodAndArguments(request, NeovimApi.PARSE_EXPRESSION, "expr", "flags", true),
                result -> assertEquals(map, result)
        );

        // Error case
        assertErrorBehavior(
                () -> neovimStreamApi.parseExpression("badexpr", "badflags", false),
                request -> assertMethodAndArguments(request, NeovimApi.PARSE_EXPRESSION, "badexpr", "badflags", false)
        );
    }

    @Test
    public void getUisTest() throws InterruptedException, ExecutionException {
        // Happy case
        List uis = List.of(
                Map.of(
                        "height", 100,
                        "width", 200,
                        "rgb", false,
                        "chan", 1
                ),
                Map.of(
                        "height", 500,
                        "width", 500,
                        "rgb", true,
                        "chan", 2
                )
        );
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, uis)),
                () -> neovimStreamApi.getUis(),
                request -> assertMethodAndArguments(request, NeovimApi.LIST_UIS),
                result -> {
                    assertEquals(2, result.size());
                    assertEquals(100, result.get(0).getHeight());
                    assertEquals(200, result.get(0).getWidth());
                    assertFalse(result.get(0).isRgb());
                    assertEquals(1, result.get(0).getChan());
                    assertEquals(500, result.get(1).getHeight());
                    assertEquals(500, result.get(1).getWidth());
                    assertTrue(result.get(1).isRgb());
                    assertEquals(2, result.get(1).getChan());
                    // Ensure to string doesn't crash
                    result.toString();
                }
        );

        // Error case
        assertErrorBehavior(
                () -> neovimStreamApi.getUis(),
                request -> assertMethodAndArguments(request, NeovimApi.LIST_UIS)
        );
    }

    @Test
    public void getProcessChildrenTest() throws InterruptedException, ExecutionException {
        // Happy case
        List children = List.of(
                1,2,3
        );
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, children)),
                () -> neovimStreamApi.getProcessChildren(),
                request -> assertMethodAndArguments(request, NeovimApi.GET_PROC_CHILDREN),
                result -> {
                    assertEquals(3, result.size());
                    assertEquals(1, children.get(0));
                    assertEquals(2, children.get(1));
                    assertEquals(3, children.get(2));
                }
        );

        // Error case
        assertErrorBehavior(
                () -> neovimStreamApi.getProcessChildren(),
                request -> assertMethodAndArguments(request, NeovimApi.GET_PROC_CHILDREN)
        );
    }

    @Test
    public void getProcessTest() throws InterruptedException, ExecutionException {
        // Happy case
        Map procInfo = Map.of(
                "name", "proc"
        );
        assertNormalBehavior(
                () -> CompletableFuture.completedFuture(new ResponseMessage(1, null, procInfo)),
                () -> neovimStreamApi.getProcess(),
                request -> assertMethodAndArguments(request, NeovimApi.GET_PROC),
                result -> assertEquals(procInfo, result)
        );

        // Error case
        assertErrorBehavior(
                () -> neovimStreamApi.getProcess(),
                request -> assertMethodAndArguments(request, NeovimApi.GET_PROC)
        );
    }

    // region Testing helpers
    private void assertNormalBehavior(
            Supplier<CompletableFuture<ResponseMessage>> preparedResponse,
            Supplier<CompletableFuture<Void>> callSupplier,
            Consumer<RequestMessage> requestAsserter
    ) throws ExecutionException, InterruptedException {
        assertNormalBehavior(preparedResponse, callSupplier, requestAsserter, Assert::assertNull);
    }

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