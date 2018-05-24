package com.ensarsarajcic.neovim.java.explorer.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public final class NeovimFunction {

    private boolean method;
    private String name;
    @JsonProperty("return_type")
    private String returnType;
    private int since;
    @JsonProperty("deprecated_since")
    private int deprecatedSince;
    private List<List<String>> parameters;

    public boolean isMethod() {
        return method;
    }

    public void setMethod(boolean method) {
        this.method = method;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public int getSince() {
        return since;
    }

    public void setSince(int since) {
        this.since = since;
    }

    public int getDeprecatedSince() {
        return deprecatedSince;
    }

    public void setDeprecatedSince(int deprecatedSince) {
        this.deprecatedSince = deprecatedSince;
    }

    public List<List<String>> getParameters() {
        return parameters;
    }

    public void setParameters(List<List<String>> parameters) {
        this.parameters = parameters;
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
