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
import org.msgpack.core.annotations.Nullable;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class CommandInfo {

    private final String name;
    private final String definition;
    private final int scriptId;
    private final boolean bang;
    private final boolean bar;
    private final boolean register;
    private final ArgumentNumberInfo numberOfArgs;

    public static final class ArgumentNumberInfo {
        private final int min;
        @Nullable
        private final Integer max;

        public ArgumentNumberInfo(int min, @Nullable Integer max) {
            this.min = min;
            this.max = max;
        }

        public static ArgumentNumberInfo fromString(String value) {
            switch (value) {
                case "*":
                    return new ArgumentNumberInfo(0, null);
                case "+":
                    return new ArgumentNumberInfo(1, null);
                case "?":
                    return new ArgumentNumberInfo(0, 1);
                default:
                    var count = Integer.parseInt(value);
                    return new ArgumentNumberInfo(count, count);
            }
        }

        public int getMin() {
            return min;
        }

        /**
         * Null value means infinite
         */
        @Nullable
        public Integer getMax() {
            return max;
        }

        @Override
        public String toString() {
            return "ArgumentNumberInfo{"
                    + "min=" + min
                    + ", max=" + max + '}';
        }
    }

    public CommandInfo(
            @JsonProperty("name")
                    String name,
            @JsonProperty("definition")
                    String definition,
            @JsonProperty("script_id")
                    int scriptId,
            @JsonProperty("bang")
                    boolean bang,
            @JsonProperty("bar")
                    boolean bar,
            @JsonProperty("register")
                    boolean register,
            @JsonProperty("nargs")
                    String numberOfArgs) {
        this.name = name;
        this.definition = definition;
        this.scriptId = scriptId;
        this.bang = bang;
        this.bar = bar;
        this.register = register;
        this.numberOfArgs = ArgumentNumberInfo.fromString(numberOfArgs);
    }

    public String getName() {
        return name;
    }

    public String getDefinition() {
        return definition;
    }

    public int getScriptId() {
        return scriptId;
    }

    public boolean isBang() {
        return bang;
    }

    public boolean isBar() {
        return bar;
    }

    public boolean isRegister() {
        return register;
    }

    public ArgumentNumberInfo getNumberOfArgs() {
        return numberOfArgs;
    }

    @Override
    public String toString() {
        return "CommandInfo{"
                + "name='" + name + '\''
                + ", definition='" + definition + '\''
                + ", scriptId=" + scriptId
                + ", bang=" + bang
                + ", bar=" + bar
                + ", register=" + register
                + ", numberOfArgs=" + numberOfArgs + '}';
    }
}
