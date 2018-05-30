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

import org.junit.Before;
import org.junit.Test;

import java.util.Set;
import java.util.concurrent.*;

import static org.junit.Assert.*;

public class SequentialMessageIdGeneratorTest {

    private SequentialMessageIdGenerator sequentialMessageIdGenerator;

    @Before
    public void setUp() throws Exception {
        this.sequentialMessageIdGenerator = new SequentialMessageIdGenerator();
    }

    @Test
    public void startsFromOne() {
        // Given a fresh generator
        // Next id should generate 1
        int firstId = sequentialMessageIdGenerator.nextId();
        assertEquals(1, firstId);
    }

    @Test
    public void increasesByOne() {
        // Given a fresh generator
        for (int i = 1; i < 100; i++) {
            // Each id should be 1 higher than last one
            assertEquals(i, sequentialMessageIdGenerator.nextId());
        }
    }

    @Test
    public void worksInMultithreadedEnvironment() throws InterruptedException {
        // Given multithreaded environment
        ExecutorService executorService = new ThreadPoolExecutor(
                3,
                5,
                10000,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>()
        );

        Set<Integer> integers = ConcurrentHashMap.newKeySet();

        int repeatCount = 25000;
        final CountDownLatch countDownLatch = new CountDownLatch(repeatCount);

        // When generator is used multiple times
        for (int i = 0; i < repeatCount; i++) {
            executorService.submit(() -> {
                integers.add(sequentialMessageIdGenerator.nextId());
                countDownLatch.countDown();
            });
        }

        if (!countDownLatch.await(2000, TimeUnit.MILLISECONDS)) {
            fail("Timeout");
        }

        // No duplicates are generated and number is properly increased
        assertEquals(repeatCount + 1, sequentialMessageIdGenerator.nextId());
        assertEquals(repeatCount, integers.size());
        executorService.shutdown();
    }
}