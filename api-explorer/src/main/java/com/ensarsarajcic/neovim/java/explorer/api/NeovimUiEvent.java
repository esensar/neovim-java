package com.ensarsarajcic.neovim.java.explorer.api;

import java.util.List;

public final class NeovimUiEvent {

    private String name;
    private List<List<String>> parameters;
    private int since;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<List<String>> getParameters() {
        return parameters;
    }

    public void setParameters(List<List<String>> parameters) {
        this.parameters = parameters;
    }

    public int getSince() {
        return since;
    }

    public void setSince(int since) {
        this.since = since;
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
