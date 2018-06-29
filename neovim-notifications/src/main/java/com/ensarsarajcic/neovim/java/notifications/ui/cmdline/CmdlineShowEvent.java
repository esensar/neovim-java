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

package com.ensarsarajcic.neovim.java.notifications.ui.cmdline;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
public final class CmdlineShowEvent implements UICmdlineEvent {
    public static final String NAME = "cmdline_show";

    private List<List> content;
    private int pos;
    private String firstc;
    private String prompt;
    private int indent;
    private int level;

    public CmdlineShowEvent(
            @JsonProperty(value = "content", index = 0) List<List> content,
            @JsonProperty(value = "pos", index = 1) int pos,
            @JsonProperty(value = "firstc", index = 2) String firstc,
            @JsonProperty(value = "prompt", index = 3) String prompt,
            @JsonProperty(value = "indent", index = 4) int indent,
            @JsonProperty(value = "level", index = 5) int level) {
        this.content = content;
        this.pos = pos;
        this.firstc = firstc;
        this.prompt = prompt;
        this.indent = indent;
        this.level = level;
    }

    public List<List> getContent() {
        return content;
    }

    public int getPos() {
        return pos;
    }

    public String getFirstc() {
        return firstc;
    }

    public String getPrompt() {
        return prompt;
    }

    public int getIndent() {
        return indent;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public String getEventName() {
        return NAME;
    }

    @Override
    public String toString() {
        return "CmdlineShowEvent{" +
                "content=" + content +
                ", pos=" + pos +
                ", firstc='" + firstc + '\'' +
                ", prompt='" + prompt + '\'' +
                ", indent=" + indent +
                ", level=" + level +
                '}';
    }
}
