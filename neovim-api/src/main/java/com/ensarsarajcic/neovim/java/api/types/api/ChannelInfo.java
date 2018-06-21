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

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class ChannelInfo {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public enum Stream {
        STANDARD_IO("stdio"),
        STANDARD_ERROR("stderr"),
        SOCKET("socket"),
        JOB("job");
        private String value;

        @JsonCreator
        public static Stream fromString(String value) {
            for (Stream stream : values()) {
                if (stream.value.equals(value)) {
                    return stream;
                }
            }
            throw new IllegalArgumentException(String.format("Stream (%s) does not exist.", value));
        }

        Stream(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "Stream{" +
                    "value='" + value + '\'' +
                    '}';
        }
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public enum Mode {
        BYTES("bytes"),
        TERMINAL("terminal"),
        RPC("rpc"),
        PSEUDOTERMINAL("pty");

        private String value;

        @JsonCreator
        public static Mode fromString(String value) {
            for (Mode mode : values()) {
                if (mode.value.equals(value)) {
                    return mode;
                }
            }
            throw new IllegalArgumentException(String.format("Mode (%s) does not exist.", value));
        }

        Mode(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "Mode{" +
                    "value='" + value + '\'' +
                    '}';
        }
    }

    private int id;
    private Stream stream;
    private Mode mode;
    private ClientInfo client;

    public ChannelInfo(
            @JsonProperty("id")
            int id,
            @JsonProperty("stream")
            Stream stream,
            @JsonProperty("mode")
            Mode mode,
            @JsonProperty("client")
            ClientInfo client) {
        this.id = id;
        this.stream = stream;
        this.mode = mode;
        this.client = client;
    }

    public int getId() {
        return id;
    }

    public Stream getStream() {
        return stream;
    }

    public Mode getMode() {
        return mode;
    }

    public ClientInfo getClient() {
        return client;
    }

    @Override
    public String toString() {
        return "ChannelInfo{" +
                "id=" + id +
                ", stream=" + stream +
                ", mode=" + mode +
                ", client=" + client +
                '}';
    }
}
