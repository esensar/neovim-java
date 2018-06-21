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

package com.ensarsarajcic.neovim.java.api.types.msgpack;

import org.junit.Test;

import static org.junit.Assert.*;

public class WindowTest {

    @Test
    public void equalsTest() {
        Window windowOne = new Window(1);
        Window windowTwo = new Window(2);

        assertNotEquals(windowOne, windowTwo);
        assertEquals(windowOne.equals(windowTwo), windowTwo.equals(windowOne));

        Window windowOneAgain = new Window(1);
        Window windowOneSame = windowOne;

        assertEquals(windowOne, windowOneSame);
        assertEquals(windowOne, windowOneAgain);
        assertEquals(windowOne.equals(windowOneAgain), windowOneAgain.equals(windowOneSame));
    }

    @Test
    public void hashCodeTest() {
        Window windowOne = new Window(1);
        Window windowTwo = new Window(2);

        assertNotEquals(windowOne.hashCode(), windowTwo.hashCode());

        Window windowOneAgain = new Window(1);
        Window windowOneSame = windowOne;

        assertEquals(windowOne.hashCode(), windowOneSame.hashCode());
        assertEquals(windowOne.hashCode(), windowOneAgain.hashCode());
    }

    @Test
    public void compareToTest() {
        Window windowOne = new Window(1);
        Window windowTwo = new Window(2);

        assertTrue(windowOne.compareTo(windowTwo) < 0);
        assertTrue(windowTwo.compareTo(windowOne) > 0);

        Window windowOneAgain = new Window(1);

        assertEquals(0, windowOne.compareTo(windowOneAgain));
        assertEquals(0, windowOneAgain.compareTo(windowOne));
    }
}