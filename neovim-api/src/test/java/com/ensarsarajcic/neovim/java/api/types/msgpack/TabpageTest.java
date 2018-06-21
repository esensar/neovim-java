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

public class TabpageTest {

    @Test
    public void equalsTest() {
        Tabpage tabpageOne = new Tabpage(1);
        Tabpage tabpageTwo = new Tabpage(2);

        assertNotEquals(tabpageOne, tabpageTwo);
        assertEquals(tabpageOne.equals(tabpageTwo), tabpageTwo.equals(tabpageOne));

        Tabpage tabpageOneAgain = new Tabpage(1);
        Tabpage tabpageOneSame = tabpageOne;

        assertEquals(tabpageOne, tabpageOneSame);
        assertEquals(tabpageOne, tabpageOneAgain);
        assertEquals(tabpageOne.equals(tabpageOneAgain), tabpageOneAgain.equals(tabpageOneSame));
    }

    @Test
    public void hashCodeTest() {
        Tabpage tabpageOne = new Tabpage(1);
        Tabpage tabpageTwo = new Tabpage(2);

        assertNotEquals(tabpageOne.hashCode(), tabpageTwo.hashCode());

        Tabpage tabpageOneAgain = new Tabpage(1);
        Tabpage tabpageOneSame = tabpageOne;

        assertEquals(tabpageOne.hashCode(), tabpageOneSame.hashCode());
        assertEquals(tabpageOne.hashCode(), tabpageOneAgain.hashCode());
    }

    @Test
    public void compareToTest() {
        Tabpage tabpageOne = new Tabpage(1);
        Tabpage tabpageTwo = new Tabpage(2);

        assertTrue(tabpageOne.compareTo(tabpageTwo) < 0);
        assertTrue(tabpageTwo.compareTo(tabpageOne) > 0);

        Tabpage tabpageOneAgain = new Tabpage(1);

        assertEquals(0, tabpageOne.compareTo(tabpageOneAgain));
        assertEquals(0, tabpageOneAgain.compareTo(tabpageOne));
    }
}