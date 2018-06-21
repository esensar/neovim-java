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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class UiOptions {
    public static final UiOptions TERMINAL = new UiOptions(false, false, false, false, false);
    public static final UiOptions FULL_UI = new UiOptions(true, true, true, true, true);

    @JsonProperty("rgb")
    private final boolean rgb;
    @JsonProperty("ext_popupmenu")
    private final boolean extPopupMenu;
    @JsonProperty("ext_tabline")
    private final boolean extTabline;
    @JsonProperty("ext_cmdline")
    private final boolean extCmdline;
    @JsonProperty("ext_wildmenu")
    private final boolean extWildmenu;

    public UiOptions(
            @JsonProperty("rgb")
                    boolean rgb,
            @JsonProperty("ext_popupmenu")
                    boolean extPopupMenu,
            @JsonProperty("ext_tabline")
                    boolean extTabline,
            @JsonProperty("ext_cmdline")
                    boolean extCmdline,
            @JsonProperty("ext_wildmenu")
                    boolean extWildmenu) {
        this.rgb = rgb;
        this.extPopupMenu = extPopupMenu;
        this.extTabline = extTabline;
        this.extCmdline = extCmdline;
        this.extWildmenu = extWildmenu;
    }

    public boolean isRgb() {
        return rgb;
    }

    public boolean isExtPopupMenu() {
        return extPopupMenu;
    }

    public boolean isExtTabline() {
        return extTabline;
    }

    public boolean isExtCmdline() {
        return extCmdline;
    }

    public boolean isExtWildmenu() {
        return extWildmenu;
    }
}
