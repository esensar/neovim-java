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

import com.ensarsarajcic.neovim.java.api.util.ObjectMappers;
import com.ensarsarajcic.neovim.java.notifications.ui.UIEvent;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;
import java.util.function.Function;

public final class HighlightSetEvent implements UIGridEvent {
    public static final String NAME = "highlight_set";

    public static final Function<List, UIEvent> CREATOR = list -> new HighlightSetEvent(
            (Integer) list.get(1),
            ObjectMappers.defaultNeovimMapper().convertValue(list.get(2), Attributes.class)
    );

    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    public static final class Attributes {
        private int foreground;
        private int background;
        private int special;
        private boolean reverse;
        private boolean italic;
        private boolean bold;
        private boolean underline;
        private boolean undercurl;

        public Attributes(int foreground,
                          int background,
                          int special,
                          boolean reverse,
                          boolean italic,
                          boolean bold,
                          boolean underline,
                          boolean undercurl) {
            this.foreground = foreground;
            this.background = background;
            this.special = special;
            this.reverse = reverse;
            this.italic = italic;
            this.bold = bold;
            this.underline = underline;
            this.undercurl = undercurl;
        }

        public int getForeground() {
            return foreground;
        }

        public int getBackground() {
            return background;
        }

        public int getSpecial() {
            return special;
        }

        public boolean isReverse() {
            return reverse;
        }

        public boolean isItalic() {
            return italic;
        }

        public boolean isBold() {
            return bold;
        }

        public boolean isUnderline() {
            return underline;
        }

        public boolean isUndercurl() {
            return undercurl;
        }
    }

    private int color;
    private Attributes attributes;

    public HighlightSetEvent(int color, Attributes attributes) {
        this.color = color;
        this.attributes = attributes;
    }

    public int getColor() {
        return color;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    @Override
    public String getEventName() {
        return NAME;
    }
}
