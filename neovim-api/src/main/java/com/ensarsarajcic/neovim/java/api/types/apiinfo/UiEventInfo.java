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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Contains definition of UI Events provided by NeovimApis instance
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class UiEventInfo {

    private String name;
    private List<ParamInfo> parameters;
    private int since;

    public UiEventInfo(
            @JsonProperty("name")
            String name,
            @JsonProperty("parameters")
            List<ParamInfo> parameters,
            @JsonProperty("since")
            int since) {
        this.name = name;
        this.parameters = parameters;
        this.since = since;
    }

    public String getName() {
        return name;
    }

    public List<ParamInfo> getParameters() {
        return parameters;
    }

    public int getSince() {
        return since;
    }

    @Override
    public String toString() {
        return "NeovimUiEvent{" +
                "name='" + name + '\'' +
                ", parameters=" + parameters +
                ", since=" + since +
                '}';
    }
}
