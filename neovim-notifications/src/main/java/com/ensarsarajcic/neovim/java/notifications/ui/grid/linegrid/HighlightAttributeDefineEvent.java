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

package com.ensarsarajcic.neovim.java.notifications.ui.grid.linegrid;

import com.ensarsarajcic.neovim.java.notifications.ui.grid.HighlightAttributes;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
public final class HighlightAttributeDefineEvent implements UILineGridEvent {
    public static final String NAME = "hl_attr_define";

    private int id;
    private HighlightAttributes rgbAttributes;
    private HighlightAttributes cTermAttrbutes;
    private List<HighlightInfo> info;

    public HighlightAttributeDefineEvent(
            @JsonProperty(value = "id", index = 0) int id,
            @JsonProperty(value = "rgb_attr", index = 1) HighlightAttributes rgbAttributes,
            @JsonProperty(value = "cterm_attr", index = 2) HighlightAttributes cTermAttrbutes,
            @JsonProperty(value = "info", index = 3) List<HighlightInfo> info) {
        this.id = id;
        this.rgbAttributes = rgbAttributes;
        this.cTermAttrbutes = cTermAttrbutes;
        this.info = info;
    }

    public int getId() {
        return id;
    }

    public HighlightAttributes getRgbAttributes() {
        return rgbAttributes;
    }

    public HighlightAttributes getcTermAttrbutes() {
        return cTermAttrbutes;
    }

    public List<HighlightInfo> getInfo() {
        return info;
    }

    @Override
    public String getEventName() {
        return NAME;
    }

    @Override
    public String toString() {
        return "HighlightAttributeDefineEvent{" +
                "id=" + id +
                ", rgbAttributes=" + rgbAttributes +
                ", cTermAttrbutes=" + cTermAttrbutes +
                ", info=" + info +
                '}';
    }
}
