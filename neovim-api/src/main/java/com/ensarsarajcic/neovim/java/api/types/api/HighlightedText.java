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

package com.ensarsarajcic.neovim.java.api.types.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Class representing highlighted text group [text, hl_group]
 */
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@JsonPropertyOrder({"text", "hlGroup"})
@JsonIgnoreProperties(ignoreUnknown = true)
public final class HighlightedText {

    private final String text;
    private final String hlGroup;

    public HighlightedText(
            @JsonProperty("text")
            String text,
            @JsonProperty("hlGroup")
            String hlGroup) {
        this.text = text;
        this.hlGroup = hlGroup;
    }

    public HighlightedText(String text) {
        this(text, null);
    }

    public String getText() {
        return text;
    }

    public String getHlGroup() {
        return hlGroup;
    }

    @Override
    public String toString() {
        return "HighlightedText{" +
                "text='" + text + '\'' +
                ", hlGroup='" + hlGroup + '\'' +
                '}';
    }
}
