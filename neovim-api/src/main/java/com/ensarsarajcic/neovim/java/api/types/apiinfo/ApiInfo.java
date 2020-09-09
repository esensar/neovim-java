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

package com.ensarsarajcic.neovim.java.api.types.apiinfo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Models complete NeovimApis API info
 */
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@JsonPropertyOrder({"channelId", "apiInfo"})
@JsonIgnoreProperties(ignoreUnknown = true)
public final class ApiInfo {
    @JsonProperty("channelId")
    private Integer channelId;
    @JsonProperty("apiInfo")
    private ApiInfoInternal apiInfoInternal;

    static final class ApiInfoInternal {
        private final Map<String, ErrorInfo.Props> errorTypes;
        private final List<FunctionInfo> functions;
        private final Map<String, TypeInfo.Props> types;
        private final List<UiEventInfo> uiEvents;
        private final List<String> uiOptions;
        private final VersionInfo version;

        ApiInfoInternal(
                @JsonProperty("error_types")
                        Map<String, ErrorInfo.Props> errorTypes,
                @JsonProperty("functions")
                        List<FunctionInfo> functions,
                @JsonProperty("types")
                        Map<String, TypeInfo.Props> types,
                @JsonProperty("ui_events")
                        List<UiEventInfo> uiEvents,
                @JsonProperty("ui_options")
                        List<String> uiOptions,
                @JsonProperty("version")
                        VersionInfo version) {
            this.errorTypes = errorTypes;
            this.functions = functions;
            this.types = types;
            this.uiEvents = uiEvents;
            this.uiOptions = uiOptions;
            this.version = version;
        }
    }

    public Integer getChannelId() {
        return channelId;
    }

    public List<ErrorInfo> getErrors() {
        return apiInfoInternal.errorTypes
                .entrySet()
                .stream()
                .map(stringPropsEntry -> new ErrorInfo(stringPropsEntry.getKey(), stringPropsEntry.getValue()))
                .collect(Collectors.toList());
    }

    public List<FunctionInfo> getFunctions() {
        return apiInfoInternal.functions;
    }

    public List<TypeInfo> getTypes() {
        return apiInfoInternal.types
                .entrySet()
                .stream()
                .map(stringPropsEntry -> new TypeInfo(stringPropsEntry.getKey(), stringPropsEntry.getValue()))
                .collect(Collectors.toList());
    }

    public List<UiEventInfo> getUiEvents() {
        return apiInfoInternal.uiEvents;
    }

    public List<String> getUiOptions() {
        return apiInfoInternal.uiOptions;
    }

    public VersionInfo getVersion() {
        return apiInfoInternal.version;
    }

    @Override
    public String toString() {
        return "ApiInfo{"
                + "channelId=" + channelId
                + ", errorTypes=" + getErrors()
                + ", functions=" + getFunctions()
                + ", types=" + getTypes()
                + ", uiEvents=" + getUiEvents()
                + ", version=" + getVersion() + '}';
    }
}
