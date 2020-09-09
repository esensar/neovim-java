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
 * Definition of a UI Event as provided by Neovim API Info
 * Used only by API Explorer
 */
public final class NeovimUiEvent {

    private final String name;
    private final List<List<String>> parameters;
    private final int since;

    public String getName() {
        return name;
    }

    public List<List<String>> getParameters() {
        return parameters;
    }

    public int getSince() {
        return since;
    }

    public NeovimUiEvent(
            @JsonProperty("name") String name,
            @JsonProperty("parameters") List<List<String>> parameters,
            @JsonProperty("since") int since) {
        this.name = name;
        this.parameters = parameters;
        this.since = since;
    }

    @Override
    public String toString() {
        return "NeovimUiEvent{"
                + "name='" + name + '\''
                + ", parameters=" + parameters
                + ", since=" + since + '}';
    }
}
