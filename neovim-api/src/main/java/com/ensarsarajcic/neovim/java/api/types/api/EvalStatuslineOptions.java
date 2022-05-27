/*
 * MIT License
 *
 * Copyright (c) 2022 Ensar Sarajčić
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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class EvalStatuslineOptions {

    @JsonProperty("winid")
    private final int windowId;
    @JsonProperty("maxwidth")
    private final int maxwidth;
    @JsonProperty("fillchar")
    private final String fillChar;
    @JsonProperty("highlights")
    private final boolean highlights;
    @JsonProperty("use_winbar")
    private final boolean useWinbar;
    @JsonProperty("use_tabline")
    private final boolean useTabline;

    public EvalStatuslineOptions(int windowId, int maxwidth, String fillChar, boolean highlights, boolean useWinbar, boolean useTabline) {
        this.windowId = windowId;
        this.maxwidth = maxwidth;
        this.fillChar = fillChar;
        this.highlights = highlights;
        this.useWinbar = useWinbar;
        this.useTabline = useTabline;
    }

    public int getWindowId() {
        return windowId;
    }

    public int getMaxwidth() {
        return maxwidth;
    }

    public String getFillChar() {
        return fillChar;
    }

    public boolean isHighlights() {
        return highlights;
    }

    public boolean isUseWinbar() {
        return useWinbar;
    }

    public boolean isUseTabline() {
        return useTabline;
    }

    @Override
    public String toString() {
        return "EvalStatuslineOptions{"
                + "windowId=" + windowId
                + ", maxwidth=" + maxwidth
                + ", fillChar='" + fillChar + '\''
                + ", highlights=" + highlights
                + ", useWinbar=" + useWinbar
                + ", useTabline=" + useTabline
                + '}';
    }
}
