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

package com.ensarsarajcic.neovim.java.notifications.ui.global;

public final class ModeInfo {

    private enum CursorShape {
        BLOCK,
        HORIZONTAL,
        VERTICAL
    }

    private CursorShape cursorShape;
    private int cellPercentage;
    private int blinkWait;
    private int blinkOn;
    private int blinkOff;
    private int highlightId;
    private int highlightLangmapId;
    private String shortName;
    private String fullName;
    private Object mouseShape;

    public ModeInfo(CursorShape cursorShape,
                    int cellPercentage,
                    int blinkWait,
                    int blinkOn,
                    int blinkOff,
                    int highlightId,
                    int highlightLangmapId,
                    String shortName,
                    String fullName,
                    Object mouseShape) {
        this.cursorShape = cursorShape;
        this.cellPercentage = cellPercentage;
        this.blinkWait = blinkWait;
        this.blinkOn = blinkOn;
        this.blinkOff = blinkOff;
        this.highlightId = highlightId;
        this.highlightLangmapId = highlightLangmapId;
        this.shortName = shortName;
        this.fullName = fullName;
        this.mouseShape = mouseShape;
    }

    public CursorShape getCursorShape() {
        return cursorShape;
    }

    public int getCellPercentage() {
        return cellPercentage;
    }

    public int getBlinkWait() {
        return blinkWait;
    }

    public int getBlinkOn() {
        return blinkOn;
    }

    public int getBlinkOff() {
        return blinkOff;
    }

    public int getHighlightId() {
        return highlightId;
    }

    public int getHighlightLangmapId() {
        return highlightLangmapId;
    }

    public String getShortName() {
        return shortName;
    }

    public String getFullName() {
        return fullName;
    }

    public Object getMouseShape() {
        return mouseShape;
    }
}
