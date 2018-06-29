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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
public final class HighlightSetEvent implements UIGridEvent {
    public static final String NAME = "highlight_set";

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Attributes {
        private int foreground;
        private int background;
        private int special;
        private boolean reverse;
        private boolean italic;
        private boolean bold;
        private boolean underline;
        private boolean undercurl;

        public Attributes(
                @JsonProperty(value = "foreground", index = 1) int foreground,
                @JsonProperty(value = "background", index = 2) int background,
                @JsonProperty(value = "special", index = 3) int special,
                @JsonProperty(value = "reverse", index = 4) boolean reverse,
                @JsonProperty(value = "italic", index = 5) boolean italic,
                @JsonProperty(value = "bold", index = 6) boolean bold,
                @JsonProperty(value = "underline", index = 7) boolean underline,
                @JsonProperty(value = "undercurl", index = 8) boolean undercurl) {
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

        @Override
        public String toString() {
            return "Attributes{" +
                    "foreground=" + foreground +
                    ", background=" + background +
                    ", special=" + special +
                    ", reverse=" + reverse +
                    ", italic=" + italic +
                    ", bold=" + bold +
                    ", underline=" + underline +
                    ", undercurl=" + undercurl +
                    '}';
        }
    }

    private Attributes attributes;

    public HighlightSetEvent(
            @JsonProperty(value = "attributes", index = 0) Attributes attributes) {
        this.attributes = attributes;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    @Override
    public String getEventName() {
        return NAME;
    }

    @Override
    public String toString() {
        return "HighlightSetEvent{" +
                "attributes=" + attributes +
                '}';
    }
}
