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

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model describing a (Neo)Vim key mapping
 */
public final class VimMapping {

    private boolean silent;
    private boolean noRemap;
    private String keyStroke;
    private String actionExpression;
    private String mode;
    private boolean noWait;
    private boolean expr;
    private int sid;
    private boolean buffer;

    public VimMapping(
            @JsonProperty("silent")
                    int silent,
            @JsonProperty("noremap")
                    int noRemap,
            @JsonProperty("lhs")
                    String keyStroke,
            @JsonProperty("rhs")
                    String actionExpression,
            @JsonProperty("mode")
                    String mode,
            @JsonProperty("nowait")
                    int noWait,
            @JsonProperty("expr")
                    int expr,
            @JsonProperty("sid")
                    int sid,
            @JsonProperty("buffer")
                    int buffer) {
        this.silent = silent != 0;
        this.noRemap = noRemap != 0;
        this.keyStroke = keyStroke;
        this.actionExpression = actionExpression;
        this.mode = mode;
        this.noWait = noWait != 0;
        this.expr = expr != 0;
        this.sid = sid;
        this.buffer = buffer != 0;
    }

    public boolean isSilent() {
        return silent;
    }

    public boolean isNoRemap() {
        return noRemap;
    }

    public String getKeyStroke() {
        return keyStroke;
    }

    public String getActionExpression() {
        return actionExpression;
    }

    public String getMode() {
        return mode;
    }

    public boolean isNoWait() {
        return noWait;
    }

    public boolean isExpr() {
        return expr;
    }

    public int getSid() {
        return sid;
    }

    public boolean isBuffer() {
        return buffer;
    }

    @Override
    public String toString() {
        return "VimMapping{" +
                "silent=" + silent +
                ", noRemap=" + noRemap +
                ", keyStroke='" + keyStroke + '\'' +
                ", actionExpression='" + actionExpression + '\'' +
                ", mode='" + mode + '\'' +
                ", noWait=" + noWait +
                ", expr=" + expr +
                ", sid=" + sid +
                ", buffer=" + buffer +
                '}';
    }
}
