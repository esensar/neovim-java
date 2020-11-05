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

package com.ensarsarajcic.neovim.java.explorer.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Definition of a Neovim type (custom msgpack type) as returned by API info
 * Name of the type is not a part of the class, since this is directly mapped from api info response
 * Used only by API Explorer
 */
public final class NeovimType {

    private final int id;
    private final String prefix;

    public int getId() {
        return id;
    }

    public String getPrefix() {
        return prefix;
    }

    public NeovimType(
            @JsonProperty("id") int id,
            @JsonProperty("prefix") String prefix) {
        this.id = id;
        this.prefix = prefix;
    }

    @Override
    public String toString() {
        return "NeovimType{"
                + "id=" + id
                + ", prefix='" + prefix + '\'' + '}';
    }
}
