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

package com.ensarsarajcic.neovim.java.api.types.apiinfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Contains definition of custom msgpack types used by NeovimApis API
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class TypeInfo {

    private final String name;
    private final Props props;

    public TypeInfo(String name, Props props) {
        this.name = name;
        this.props = props;
    }

    static final class Props {
        private final int id;
        private final String prefix;

        Props(
                @JsonProperty("id")
                        int id,
                @JsonProperty("prefix")
                        String prefix) {
            this.id = id;
            this.prefix = prefix;
        }

        @Override
        public String toString() {
            return "Props{"
                    + "id=" + id
                    + ", prefix='" + prefix + '\'' + '}';
        }
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return props.id;
    }

    public String getPrefix() {
        return props.prefix;
    }

    @Override
    public String toString() {
        return "TypeInfo{"
                + "id=" + props.id
                + ", prefix='" + props.prefix + '\''
                + "name='" + name + '\'' + '}';
    }
}
