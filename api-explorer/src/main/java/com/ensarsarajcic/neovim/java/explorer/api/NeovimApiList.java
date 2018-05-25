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

public final class NeovimApiList {

    @JsonProperty("error_types")
    private Map<String, NeovimError> errorTypes;
    private List<NeovimFunction> functions;
    private Map<String, NeovimType> types;
    @JsonProperty("ui_events")
    private List<NeovimUiEvent> uiEvents;
    private NeovimVersion version;

    public Map<String, NeovimError> getErrorTypes() {
        return errorTypes;
    }

    public void setErrorTypes(Map<String, NeovimError> errorTypes) {
        this.errorTypes = errorTypes;
    }

    public List<NeovimFunction> getFunctions() {
        return functions;
    }

    public void setFunctions(List<NeovimFunction> functions) {
        this.functions = functions;
    }

    public Map<String, NeovimType> getTypes() {
        return types;
    }

    public void setTypes(Map<String, NeovimType> types) {
        this.types = types;
    }

    public List<NeovimUiEvent> getUiEvents() {
        return uiEvents;
    }

    public void setUiEvents(List<NeovimUiEvent> uiEvents) {
        this.uiEvents = uiEvents;
    }

    public NeovimVersion getVersion() {
        return version;
    }

    public void setVersion(NeovimVersion version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "NeovimApiList{" +
                "errorTypes=" + errorTypes +
                ", functions=" + functions +
                ", types=" + types +
                ", uiEvents=" + uiEvents +
                ", version=" + version +
                '}';
    }
}
