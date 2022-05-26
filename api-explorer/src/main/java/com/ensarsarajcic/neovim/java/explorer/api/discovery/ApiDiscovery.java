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
import com.ensarsarajcic.neovim.java.api.types.apiinfo.ApiInfo;
import com.ensarsarajcic.neovim.java.api.types.apiinfo.ErrorInfo;
import com.ensarsarajcic.neovim.java.api.types.apiinfo.FunctionInfo;
import com.ensarsarajcic.neovim.java.api.types.apiinfo.ParamInfo;
import com.ensarsarajcic.neovim.java.api.types.apiinfo.TypeInfo;
import com.ensarsarajcic.neovim.java.api.types.apiinfo.UiEventInfo;
import com.ensarsarajcic.neovim.java.api.types.apiinfo.VersionInfo;
import com.ensarsarajcic.neovim.java.corerpc.client.RpcConnection;
import com.ensarsarajcic.neovim.java.explorer.api.NeovimApiList;
import com.ensarsarajcic.neovim.java.explorer.api.NeovimError;
import com.ensarsarajcic.neovim.java.explorer.api.NeovimFunction;
import com.ensarsarajcic.neovim.java.explorer.api.NeovimType;
import com.ensarsarajcic.neovim.java.explorer.api.NeovimUiEvent;
import com.ensarsarajcic.neovim.java.explorer.api.NeovimVersion;
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
import java.util.stream.Collectors;

/**
 * Collection of utils for collecting API Info for API Explorer GUI
 */
public final class ApiDiscovery {

    private ApiDiscovery() {
        //no instance
    }

    /**
     * Generates a process call for loading up API info
     */
    private static List<String> createArgs(String executable) {
        var allArgs = new ArrayList<String>(2);
        allArgs.add(executable);
        allArgs.add("--api-info");
        return allArgs;
    }

    /**
     * Loads API Info from --api-info
     */
    public static NeovimApiList discoverApi(String executable) throws IOException {
        var pb = new ProcessBuilder(createArgs(executable));
        var neovim = pb.start();
        var factory = new MessagePackFactory();
        factory.disable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
        factory.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        return new ObjectMapper(factory).readerFor(NeovimApiList.class).readValue(neovim.getInputStream());
    }

    /**
     * Loads API Info from running Neovim instance, represented with {@link RpcConnection}
     *
     * @param rpcConnection connection to Neovim instance
     */
    public static NeovimApiList discoverApiFromConnection(RpcConnection rpcConnection) throws ExecutionException, InterruptedException {
        var neovimApi = NeovimApis.getApiForConnection(rpcConnection);
        return discoverApiFromInstance(neovimApi);
    }

    public static NeovimApiList discoverApiFromInstance(NeovimApi neovimApi) throws ExecutionException, InterruptedException {
        var apiInfo = neovimApi.getApiInfo().get();
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
        var neovimErrorMap = new HashMap<String, NeovimError>();
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
                        transformParamInfo(functionInfo.getParameters())
                )).collect(Collectors.toList());
    }

    private static Map<String, NeovimType> transformTypes(List<TypeInfo> typeInfos) {
        var neovimTypeMap = new HashMap<String, NeovimType>();
        for (var typeInfo : typeInfos) {
            neovimTypeMap.put(typeInfo.getName(), new NeovimType(typeInfo.getId(), typeInfo.getPrefix()));
        }
        return neovimTypeMap;
    }

    private static List<NeovimUiEvent> transformUIEvents(List<UiEventInfo> uiEventInfos) {
        return uiEventInfos.stream()
                .map(functionInfo -> new NeovimUiEvent(
                        functionInfo.getName(),
                        transformParamInfo(functionInfo.getParameters()),
                        functionInfo.getSince()
                )).collect(Collectors.toList());
    }

    private static List<List<String>> transformParamInfo(List<ParamInfo> paramInfos) {
        return paramInfos.stream()
                .map(paramInfo -> List.of(paramInfo.getType(), paramInfo.getName())).collect(Collectors.toList());
    }
}
