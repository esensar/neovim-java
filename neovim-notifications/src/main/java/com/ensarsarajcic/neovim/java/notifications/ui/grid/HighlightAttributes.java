package com.ensarsarajcic.neovim.java.notifications.ui.grid;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
// TODO make all fields optional
public final class HighlightAttributes {
    private final int foreground;
    private final int background;
    private final int special;
    private final boolean reverse;
    private final boolean italic;
    private final boolean bold;
    private final boolean strikethrough;
    private final boolean underline;
    private final boolean undercurl;
    private final int blend;

    public HighlightAttributes(
            @JsonProperty(value = "foreground", index = 1) int foreground,
            @JsonProperty(value = "background", index = 2) int background,
            @JsonProperty(value = "special", index = 3) int special,
            @JsonProperty(value = "reverse", index = 4) boolean reverse,
            @JsonProperty(value = "italic", index = 5) boolean italic,
            @JsonProperty(value = "bold", index = 6) boolean bold,
            @JsonProperty(value = "strikethrough", index = 7) boolean strikethrough,
            @JsonProperty(value = "underline", index = 8) boolean underline,
            @JsonProperty(value = "undercurl", index = 9) boolean undercurl,
            @JsonProperty(value = "blend", index = 10) int blend) {
        this.foreground = foreground;
        this.background = background;
        this.special = special;
        this.reverse = reverse;
        this.italic = italic;
        this.bold = bold;
        this.strikethrough = strikethrough;
        this.underline = underline;
        this.undercurl = undercurl;
        this.blend = blend;
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

    public boolean isStrikethrough() {
        return strikethrough;
    }

    public boolean isUnderline() {
        return underline;
    }

    public boolean isUndercurl() {
        return undercurl;
    }

    public int getBlend() {
        return blend;
    }

    @Override
    public String toString() {
        return "Attributes{"
                + "foreground=" + foreground
                + ", background=" + background
                + ", special=" + special
                + ", reverse=" + reverse
                + ", italic=" + italic
                + ", bold=" + bold
                + ", strikethrough=" + strikethrough
                + ", underline=" + underline
                + ", undercurl=" + undercurl
                + ", blend=" + blend + '}';
    }
}
