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
