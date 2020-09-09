package com.ensarsarajcic.neovim.java.notifications.ui.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
public final class MessageHistoryEntry {

    private final String kind;
    private final List<MessageChunk> content;

    public MessageHistoryEntry(
            @JsonProperty(value = "kind", index = 0) String kind,
            @JsonProperty(value = "content", index = 1) List<MessageChunk> content) {
        this.kind = kind;
        this.content = content;
    }

    public String getKind() {
        return kind;
    }

    public List<MessageChunk> getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "MessageHistoryEntry{"
                + "kind='" + kind + '\''
                + ", content=" + content + '}';
    }
}
