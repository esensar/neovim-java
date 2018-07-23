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

package com.ensarsarajcic.neovim.java.corerpc.message;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Defines types used in RPC communication
 */
public enum MessageType {
    REQUEST(0),
    RESPONSE(1),
    NOTIFICATION(2);

    private int value;

    MessageType(int intValue) {
        this.value = intValue;
    }

    /**
     * Transforms passed integer into {@link MessageType}
     * @param value a valid integer representing {@link MessageType} (it should be inside bounds [0 - 2])
     * @return Corresponding instance of {@link MessageType}
     * @throws IllegalArgumentException if value is not inside bounds
     */
    public static MessageType fromInt(int value) {
        for(var messageType : values()) {
            if (value == messageType.value) {
                return messageType;
            }
        }
        throw new IllegalArgumentException(
                String.format("MessageType with int value of (%d) does not exist!", value));
    }

    /**
     * Gets integer representation of this {@link MessageType}
     * @return Corresponding integer value
     */
    @JsonValue
    public int asInt() {
        return value;
    }
}
