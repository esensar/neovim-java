package com.ensarsarajcic.neovim.java.notifications.ui.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
public final class MessageChunk {

    private final int highlightId;
    private final String text;

    public MessageChunk(
            @JsonProperty(value = "attr_id", index = 0) int highlightId,
            @JsonProperty(value = "text_chunk", index = 1) String text) {
        this.highlightId = highlightId;
        this.text = text;
    }

    public int getHighlightId() {
        return highlightId;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "MessageChunk{" +
                "highlightId=" + highlightId +
                ", text='" + text + '\'' +
                '}';
    }
}
