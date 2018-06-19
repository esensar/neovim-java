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

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Contains definition of a NeovimApis API function
 */
public final class FunctionInfo {

    private boolean method;
    private String name;
    private String returnType;
    private int since;
    private int deprecatedSince;
    private List<ParamInfo> parameters;

    public FunctionInfo(
            @JsonProperty("method")
            boolean method,
            @JsonProperty("name")
            String name,
            @JsonProperty("return_type")
            String returnType,
            @JsonProperty("since")
            int since,
            @JsonProperty("deprecated_since")
            int deprecatedSince,
            @JsonProperty("parameters")
            List<ParamInfo> parameters) {
        this.method = method;
        this.name = name;
        this.returnType = returnType;
        this.since = since;
        this.deprecatedSince = deprecatedSince;
        this.parameters = parameters;
    }

    public boolean isMethod() {
        return method;
    }

    public String getName() {
        return name;
    }

    public String getReturnType() {
        return returnType;
    }

    public int getSince() {
        return since;
    }

    public int getDeprecatedSince() {
        return deprecatedSince;
    }

    public List<ParamInfo> getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return "NeovimFunction{" +
                "method=" + method +
                ", name='" + name + '\'' +
                ", returnType='" + returnType + '\'' +
                ", since=" + since +
                ", deprecatedSince=" + deprecatedSince +
                ", parameters=" + parameters +
                '}';
    }
}
