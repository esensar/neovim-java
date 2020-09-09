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

package com.ensarsarajcic.neovim.java.explorer.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Definition of a Neovim API Function as provided by Neovim API Info
 * Used only by API Explorer
 */
public final class NeovimFunction {

    private final boolean method;
    private final String name;
    private final String returnType;
    private final int since;
    private final int deprecatedSince;
    private final List<List<String>> parameters;

    public NeovimFunction(
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
                    List<List<String>> parameters) {
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

    public List<List<String>> getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return "NeovimFunction{"
                + "method=" + method
                + ", name='" + name + '\''
                + ", returnType='" + returnType + '\''
                + ", since=" + since
                + ", deprecatedSince=" + deprecatedSince
                + ", parameters=" + parameters + '}';
    }
}
