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

package com.ensarsarajcic.neovim.java.notifications.ui.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
public final class MessageShowEvent implements UiMessageEvent {
    public static final String NAME = "msg_show";

    private final String kind;
    private final List<MessageChunk> content;
    private final boolean replaceLast;

    public MessageShowEvent(
            @JsonProperty(value = "kind", index = 0) String kind,
            @JsonProperty(value = "content", index = 1) List<MessageChunk> content,
            @JsonProperty(value = "replace_last", index = 2) boolean replaceLast) {
        this.kind = kind;
        this.content = content;
        this.replaceLast = replaceLast;
    }

    public String getKind() {
        return kind;
    }

    public List<MessageChunk> getContent() {
        return content;
    }

    public boolean isReplaceLast() {
        return replaceLast;
    }

    @Override
    public String getEventName() {
        return NAME;
    }

    @Override
    public String toString() {
        return "MessageShowEvent{"
                + "kind='" + kind + '\''
                + ", content=" + content
                + ", replaceLast=" + replaceLast + '}';
    }
}
