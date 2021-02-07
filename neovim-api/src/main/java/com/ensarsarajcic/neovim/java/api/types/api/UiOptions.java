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

    @JsonProperty("rgb")
    private final boolean rgb;
    @JsonProperty("override")
    private final boolean override;
    @JsonProperty("ext_cmdline")
    private final boolean extCmdline;
    @JsonProperty("ext_hlstate")
    private final boolean extHlState;
    @JsonProperty("ext_linegrid")
    private final boolean extLineGrid;
    @JsonProperty("ext_messages")
    private final boolean extMessages;
    @JsonProperty("ext_multigrid")
    private final boolean extMultiGrid;
    @JsonProperty("ext_popupmenu")
    private final boolean extPopupMenu;
    @JsonProperty("ext_tabline")
    private final boolean extTabline;
    @JsonProperty("ext_termcolors")
    private final boolean extTermColors;

    public UiOptions(
            @JsonProperty("rgb")
                    boolean rgb,
            @JsonProperty("override")
                    boolean override,
            @JsonProperty("ext_cmdline")
                    boolean extCmdline,
            @JsonProperty("ext_hlstate")
                    boolean extHlState,
            @JsonProperty("ext_linegrid")
                    boolean extLineGrid,
            @JsonProperty("ext_messages")
                    boolean extMessages,
            @JsonProperty("ext_multigrid")
                    boolean extMultiGrid,
            @JsonProperty("ext_popupmenu")
                    boolean extPopupMenu,
            @JsonProperty("ext_tabline")
                    boolean extTabline,
            @JsonProperty("ext_termcolors")
                    boolean extTermColors) {
        this.rgb = rgb;
        this.override = override;
        this.extCmdline = extCmdline;
        this.extHlState = extHlState;
        this.extLineGrid = extLineGrid;
        this.extMessages = extMessages;
        this.extMultiGrid = extMultiGrid;
        this.extPopupMenu = extPopupMenu;
        this.extTabline = extTabline;
        this.extTermColors = extTermColors;
    }

    public boolean isRgb() {
        return rgb;
    }

    public boolean isOverride() {
        return override;
    }

    public boolean isExtCmdline() {
        return extCmdline;
    }

    public boolean isExtHlState() {
        return extHlState;
    }

    public boolean isExtLineGrid() {
        return extLineGrid;
    }

    public boolean isExtMessages() {
        return extMessages;
    }

    public boolean isExtMultiGrid() {
        return extMultiGrid;
    }

    public boolean isExtPopupMenu() {
        return extPopupMenu;
    }

    public boolean isExtTabline() {
        return extTabline;
    }

    public boolean isExtTermColors() {
        return extTermColors;
    }

    public static final UiOptions TERMINAL = new UiOptions(
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false
    );
    public static final UiOptions FULL_UI = new UiOptions(
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true
    );
}
