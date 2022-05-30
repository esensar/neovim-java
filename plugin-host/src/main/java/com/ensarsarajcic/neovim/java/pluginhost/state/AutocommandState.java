package com.ensarsarajcic.neovim.java.pluginhost.state;

import com.fasterxml.jackson.annotation.JsonProperty;


public record AutocommandState(
        @JsonProperty("file")
        String file,
        @JsonProperty("match")
        String match,
        @JsonProperty("buffer")
        Integer buffer
) {
}
