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

package com.ensarsarajcic.neovim.java.testing;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Wraps an executor service to manually submit tasks
 */
public final class ManualQueueExecutorService implements ExecutorService {

    private ExecutorService executorService;

    private List<Map.Entry<CountDownLatch, Callable>> callables = new ArrayList<>();

    public ManualQueueExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public void shutdown() {
        executorService.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return executorService.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return executorService.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return executorService.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return executorService.awaitTermination(timeout, unit);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return prepareCallable(task);
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        Callable<T> callable = () -> {
            task.run();
            return result;
        };
        return prepareCallable(callable);
    }

    @Override
    public Future<?> submit(Runnable task) {
        Callable<?> callable = () -> {
            task.run();
            return null;
        };
        return prepareCallable(callable);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return tasks.stream()
                .map(this::prepareCallable)
                .collect(Collectors.toList());
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return invokeAll(tasks);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return invokeAll(tasks).get(0).get();
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return invokeAny(tasks);
    }

    @Override
    public void execute(Runnable command) {
        Callable<?> callable = () -> {
            command.run();
            return null;
        };
        prepareCallable(callable);
    }

    private <T> Future<T> prepareCallable(Callable<T> task) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        callables.add(new AbstractMap.SimpleEntry<>(countDownLatch, task));
        return CompletableFuture.supplyAsync(() -> {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            try {
                return executorService.submit(task).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
    }

    public void runOne() {
        if (!callables.isEmpty()) {
            callables.get(0).getKey().countDown();
            callables.remove(0);
        }
    }
}
