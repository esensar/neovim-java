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

package com.ensarsarajcic.neovim.java.handler;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Simple implementation of {@link ExecutorService} which runs all tasks on thread the call was made from
 * <p>
 * This is useful as a quick implementation, and should be used only when blocking is fine
 * <p>
 */
public final class ImmediateExecutorService extends AbstractExecutorService {

    private boolean shutdown = false;

    /**
     * Sets the shutdown flag just for {@link #isShutdown()} and {@link #isTerminated()} methods
     */
    @Override
    public void shutdown() {
        shutdown = true;
    }

    /**
     * Returns an empty list of runnables, since it can't hold any and sets the same flag as {@link #shutdown()}
     *
     * @return an empty list
     */
    @Override
    public List<Runnable> shutdownNow() {
        shutdown = true;
        return Collections.emptyList();
    }

    /**
     * Returns the shutdown flag set by {@link #shutdown()} and {@link #shutdownNow()}
     *
     * @return shutdown flag
     */
    @Override
    public boolean isShutdown() {
        return shutdown;
    }

    /**
     * Returns the shutdown flag set by {@link #shutdown()} and {@link #shutdownNow()}
     *
     * @return shutdown flag
     */
    @Override
    public boolean isTerminated() {
        return shutdown;
    }

    /**
     * No-op
     *
     * @return true
     */
    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return true;
    }

    /**
     * Immediately runs the passed command, unless shutdown flag is set
     * If shutdown flag is set, this is a no-op
     *
     * @param command to run
     */
    @Override
    public void execute(Runnable command) {
        if (!shutdown) {
            command.run();
        }
    }
}
