package com.ensarsarajcic.neovim.java.pluginhost.state;

import com.fasterxml.jackson.annotation.JsonProperty;


public record CommandState(
        @JsonProperty("lineStart")
        Integer lineStart,
        @JsonProperty("lineEnd")
        Integer lineEnd,
        @JsonProperty("range")
        Integer range,
        @JsonProperty("count")
        Integer count,
        @JsonProperty("bang")
        String bang,
        @JsonProperty("mods")
        String mods,
        @JsonProperty("register")
        String register,
        @JsonProperty("args")
        String args
) {
}
