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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@JsonPropertyOrder({"row", "col", "buffer", "buffername"})
@JsonIgnoreProperties(ignoreUnknown = true)
public final class MarkInfo {

    private final int row;
    private final int col;
    private final int buffer;
    private final String bufferName;

    public MarkInfo(
            @JsonProperty("row")
                    int row,
            @JsonProperty("col")
                    int col,
            @JsonProperty("buffer")
            int buffer,
            @JsonProperty("buffername")
            String bufferName) {
        this.row = row;
        this.col = col;
        this.buffer = buffer;
        this.bufferName = bufferName;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getBuffer() {
        return buffer;
    }

    public String getBufferName() {
        return bufferName;
    }

    @Override
    public String toString() {
        return "MarkInfo{"
                + "row=" + row
                + ", col=" + col
                + ", buffer=" + buffer
                + ", bufferName='" + bufferName + '\''
                + '}';
    }
}
