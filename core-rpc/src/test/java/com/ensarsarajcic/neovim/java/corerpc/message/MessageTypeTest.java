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

import org.junit.Test;

import static org.junit.Assert.*;

public class MessageTypeTest {

    @Test
    public void fromIntTest() {
        // 0 should be request
        assertEquals(MessageType.REQUEST, MessageType.fromInt(0));
        // 1 should be response
        assertEquals(MessageType.RESPONSE, MessageType.fromInt(1));
        // 2 should be notification
        assertEquals(MessageType.NOTIFICATION, MessageType.fromInt(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromInvalidIntTest() {
        // when an invalid int is used it should throw an exception
        MessageType.fromInt(27);
    }

    @Test
    public void asIntTest() {
        // 0 should be request
        assertEquals(0, MessageType.REQUEST.asInt());
        // 1 should be response
        assertEquals(1, MessageType.RESPONSE.asInt());
        // 2 should be notification
        assertEquals(2, MessageType.NOTIFICATION.asInt());
    }
}