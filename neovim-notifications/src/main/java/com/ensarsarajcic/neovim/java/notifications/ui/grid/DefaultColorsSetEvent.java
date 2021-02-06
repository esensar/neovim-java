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

package com.ensarsarajcic.neovim.java.notifications.ui.grid;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
public final class DefaultColorsSetEvent implements UiGridEvent {
    public static final String NAME = "default_colors_set";

    private final int rgbFg;
    private final int rgbBg;
    private final int rgbSp;
    private final int ctermFg;
    private final int ctermBg;

    public DefaultColorsSetEvent(
            @JsonProperty(value = "rgb_fg", index = 0) int rgbFg,
            @JsonProperty(value = "rgb_bg", index = 1) int rgbBg,
            @JsonProperty(value = "rgb_sp", index = 2) int rgbSp,
            @JsonProperty(value = "cterm_fg", index = 3) int ctermFg,
            @JsonProperty(value = "cterm_bg", index = 4) int ctermBg
    ) {
        this.rgbFg = rgbFg;
        this.rgbBg = rgbBg;
        this.rgbSp = rgbSp;
        this.ctermFg = ctermFg;
        this.ctermBg = ctermBg;
    }

    public int getRgbFg() {
        return rgbFg;
    }

    public int getRgbBg() {
        return rgbBg;
    }

    public int getRgbSp() {
        return rgbSp;
    }

    public int getCtermFg() {
        return ctermFg;
    }

    public int getCtermBg() {
        return ctermBg;
    }

    @Override
    public String getEventName() {
        return NAME;
    }

    @Override
    public String toString() {
        return "DefaultColorsSetEvent{"
                + "rgbFg=" + rgbFg
                + ", rgbBg=" + rgbBg
                + ", rgbSp=" + rgbSp
                + ", ctermFg=" + ctermFg
                + ", ctermBg=" + ctermBg
                + '}';
    }
}
