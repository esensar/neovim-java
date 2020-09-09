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
import java.util.Map;

/**
 * Represents full Neovim API Information
 * Used only by API Explorer for display
 */
public final class NeovimApiList {

    private final Map<String, NeovimError> errorTypes;
    private final List<NeovimFunction> functions;
    private final Map<String, NeovimType> types;
    private final List<NeovimUiEvent> uiEvents;
    private final List<String> uiOptions;
    private final NeovimVersion version;

    public NeovimApiList(
            @JsonProperty("error_types")
                    Map<String, NeovimError> errorTypes,
            @JsonProperty("functions")
                    List<NeovimFunction> functions,
            @JsonProperty("types")
                    Map<String, NeovimType> types,
            @JsonProperty("ui_events")
                    List<NeovimUiEvent> uiEvents,
            @JsonProperty("ui_options")
                    List<String> uiOptions,
            @JsonProperty("version")
                    NeovimVersion version) {
        this.errorTypes = errorTypes;
        this.functions = functions;
        this.types = types;
        this.uiEvents = uiEvents;
        this.uiOptions = uiOptions;
        this.version = version;
    }

    public Map<String, NeovimError> getErrorTypes() {
        return errorTypes;
    }

    public List<NeovimFunction> getFunctions() {
        return functions;
    }

    public Map<String, NeovimType> getTypes() {
        return types;
    }

    public List<NeovimUiEvent> getUiEvents() {
        return uiEvents;
    }

    public List<String> getUiOptions() {
        return uiOptions;
    }

    public NeovimVersion getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "NeovimApiList{"
                + "errorTypes=" + errorTypes
                + ", functions=" + functions
                + ", types=" + types
                + ", uiEvents=" + uiEvents
                + ", version=" + version + '}';
    }
}
