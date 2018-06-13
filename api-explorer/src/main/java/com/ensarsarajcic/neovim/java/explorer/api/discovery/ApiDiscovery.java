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

package com.ensarsarajcic.neovim.java.explorer.api.discovery;

import com.ensarsarajcic.neovim.java.api.NeovimApi;
import com.ensarsarajcic.neovim.java.api.NeovimApis;
import com.ensarsarajcic.neovim.java.api.types.apiinfo.*;
import com.ensarsarajcic.neovim.java.corerpc.client.RPCConnection;
import com.ensarsarajcic.neovim.java.explorer.api.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.msgpack.jackson.dataformat.MessagePackFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class ApiDiscovery {

    private static List<String> createArgs(String executable) {
        List<String> allArgs = new ArrayList<>(2);
        allArgs.add(executable);
        allArgs.add("--api-info");
        return allArgs;
    }

    public static NeovimApiList discoverApi() throws IOException {
        ProcessBuilder pb = new ProcessBuilder(createArgs("nvim"));
        Process neovim = pb.start();
        MessagePackFactory factory = new MessagePackFactory();
        factory.disable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
        factory.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        return new ObjectMapper(factory).readerFor(NeovimApiList.class).readValue(neovim.getInputStream());
    }

    public static NeovimApiList discoverApiFromConnection(RPCConnection rpcConnection) throws ExecutionException, InterruptedException {
        NeovimApi neovimApi = NeovimApis.getApiForConnection(rpcConnection);
        ApiInfo apiInfo = neovimApi.getApiInfo().get();
        return transform(apiInfo);
    }

    private static NeovimApiList transform(ApiInfo apiInfo) {
        return new NeovimApiList(
                transformErrors(apiInfo.getErrors()),
                transformFunctions(apiInfo.getFunctions()),
                transformTypes(apiInfo.getTypes()),
                transformUIEvents(apiInfo.getUiEvents()),
                apiInfo.getUiOptions(),
                transform(apiInfo.getVersion())
        );
    }

    private static NeovimVersion transform(VersionInfo versionInfo) {
        return new NeovimVersion(
                versionInfo.getCompatible(),
                versionInfo.getLevel(),
                versionInfo.isPreRelease(),
                versionInfo.getMajor(),
                versionInfo.getMinor(),
                versionInfo.getPatch()
        );
    }

    private static Map<String, NeovimError> transformErrors(List<ErrorInfo> errorInfos) {
        Map<String, NeovimError> neovimErrorMap = new HashMap<>();
        for (ErrorInfo errorInfo : errorInfos) {
            neovimErrorMap.put(errorInfo.getName(), new NeovimError(errorInfo.getId()));
        }
        return neovimErrorMap;
    }

    private static List<NeovimFunction> transformFunctions(List<FunctionInfo> functionInfos) {
        return functionInfos.stream()
                .map(functionInfo -> new NeovimFunction(
                        functionInfo.isMethod(),
                        functionInfo.getName(),
                        functionInfo.getReturnType(),
                        functionInfo.getSince(),
                        functionInfo.getDeprecatedSince(),
                        functionInfo.getParameters()
                )).collect(Collectors.toList());
    }

    private static Map<String, NeovimType> transformTypes(List<TypeInfo> typeInfos) {
        Map<String, NeovimType> neovimTypeMap = new HashMap<>();
        for (TypeInfo typeInfo : typeInfos) {
            neovimTypeMap.put(typeInfo.getName(), new NeovimType(typeInfo.getId(), typeInfo.getPrefix()));
        }
        return neovimTypeMap;
    }

    private static List<NeovimUiEvent> transformUIEvents(List<UiEventInfo> uiEventInfos) {
        return uiEventInfos.stream()
                .map(functionInfo -> new NeovimUiEvent(
                        functionInfo.getName(),
                        functionInfo.getParameters(),
                        functionInfo.getSince()
                )).collect(Collectors.toList());
    }
}
