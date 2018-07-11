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

package com.ensarsarajcic.neovim.java.api.atomic;

import java.util.concurrent.*;
import java.util.function.*;

public final class AtomicCallBuilderCompletableFuture<T> extends CompletableFuture<T> {

    @Override
    public boolean isDone() {
        return super.isDone();
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        return super.get();
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return super.get(timeout, unit);
    }

    @Override
    public T join() {
        throw new UnsupportedOperationException("CompletableFuture returned from AtomicCallBuilder should just be used for building");
    }

    @Override
    public T getNow(T valueIfAbsent) {
        return super.getNow(valueIfAbsent);
    }

    @Override
    public boolean complete(T value) {
        return super.complete(value);
    }

    @Override
    public boolean completeExceptionally(Throwable ex) {
        return super.completeExceptionally(ex);
    }

    @Override
    public <U> CompletableFuture<U> thenApply(Function<? super T, ? extends U> fn) {
        return super.thenApply(fn);
    }

    @Override
    public <U> CompletableFuture<U> thenApplyAsync(Function<? super T, ? extends U> fn) {
        return super.thenApplyAsync(fn);
    }

    @Override
    public <U> CompletableFuture<U> thenApplyAsync(Function<? super T, ? extends U> fn, Executor executor) {
        return super.thenApplyAsync(fn, executor);
    }

    @Override
    public CompletableFuture<Void> thenAccept(Consumer<? super T> action) {
        return super.thenAccept(action);
    }

    @Override
    public CompletableFuture<Void> thenAcceptAsync(Consumer<? super T> action) {
        return super.thenAcceptAsync(action);
    }

    @Override
    public CompletableFuture<Void> thenAcceptAsync(Consumer<? super T> action, Executor executor) {
        return super.thenAcceptAsync(action, executor);
    }

    @Override
    public CompletableFuture<Void> thenRun(Runnable action) {
        return super.thenRun(action);
    }

    @Override
    public CompletableFuture<Void> thenRunAsync(Runnable action) {
        return super.thenRunAsync(action);
    }

    @Override
    public CompletableFuture<Void> thenRunAsync(Runnable action, Executor executor) {
        return super.thenRunAsync(action, executor);
    }

    @Override
    public <U, V> CompletableFuture<V> thenCombine(CompletionStage<? extends U> other, BiFunction<? super T, ? super U, ? extends V> fn) {
        return super.thenCombine(other, fn);
    }

    @Override
    public <U, V> CompletableFuture<V> thenCombineAsync(CompletionStage<? extends U> other, BiFunction<? super T, ? super U, ? extends V> fn) {
        return super.thenCombineAsync(other, fn);
    }

    @Override
    public <U, V> CompletableFuture<V> thenCombineAsync(CompletionStage<? extends U> other, BiFunction<? super T, ? super U, ? extends V> fn, Executor executor) {
        return super.thenCombineAsync(other, fn, executor);
    }

    @Override
    public <U> CompletableFuture<Void> thenAcceptBoth(CompletionStage<? extends U> other, BiConsumer<? super T, ? super U> action) {
        return super.thenAcceptBoth(other, action);
    }

    @Override
    public <U> CompletableFuture<Void> thenAcceptBothAsync(CompletionStage<? extends U> other, BiConsumer<? super T, ? super U> action) {
        return super.thenAcceptBothAsync(other, action);
    }

    @Override
    public <U> CompletableFuture<Void> thenAcceptBothAsync(CompletionStage<? extends U> other, BiConsumer<? super T, ? super U> action, Executor executor) {
        return super.thenAcceptBothAsync(other, action, executor);
    }

    @Override
    public CompletableFuture<Void> runAfterBoth(CompletionStage<?> other, Runnable action) {
        return super.runAfterBoth(other, action);
    }

    @Override
    public CompletableFuture<Void> runAfterBothAsync(CompletionStage<?> other, Runnable action) {
        return super.runAfterBothAsync(other, action);
    }

    @Override
    public CompletableFuture<Void> runAfterBothAsync(CompletionStage<?> other, Runnable action, Executor executor) {
        return super.runAfterBothAsync(other, action, executor);
    }

    @Override
    public <U> CompletableFuture<U> applyToEither(CompletionStage<? extends T> other, Function<? super T, U> fn) {
        return super.applyToEither(other, fn);
    }

    @Override
    public <U> CompletableFuture<U> applyToEitherAsync(CompletionStage<? extends T> other, Function<? super T, U> fn) {
        return super.applyToEitherAsync(other, fn);
    }

    @Override
    public <U> CompletableFuture<U> applyToEitherAsync(CompletionStage<? extends T> other, Function<? super T, U> fn, Executor executor) {
        return super.applyToEitherAsync(other, fn, executor);
    }

    @Override
    public CompletableFuture<Void> acceptEither(CompletionStage<? extends T> other, Consumer<? super T> action) {
        return super.acceptEither(other, action);
    }

    @Override
    public CompletableFuture<Void> acceptEitherAsync(CompletionStage<? extends T> other, Consumer<? super T> action) {
        return super.acceptEitherAsync(other, action);
    }

    @Override
    public CompletableFuture<Void> acceptEitherAsync(CompletionStage<? extends T> other, Consumer<? super T> action, Executor executor) {
        return super.acceptEitherAsync(other, action, executor);
    }

    @Override
    public CompletableFuture<Void> runAfterEither(CompletionStage<?> other, Runnable action) {
        return super.runAfterEither(other, action);
    }

    @Override
    public CompletableFuture<Void> runAfterEitherAsync(CompletionStage<?> other, Runnable action) {
        return super.runAfterEitherAsync(other, action);
    }

    @Override
    public CompletableFuture<Void> runAfterEitherAsync(CompletionStage<?> other, Runnable action, Executor executor) {
        return super.runAfterEitherAsync(other, action, executor);
    }

    @Override
    public <U> CompletableFuture<U> thenCompose(Function<? super T, ? extends CompletionStage<U>> fn) {
        return super.thenCompose(fn);
    }

    @Override
    public <U> CompletableFuture<U> thenComposeAsync(Function<? super T, ? extends CompletionStage<U>> fn) {
        return super.thenComposeAsync(fn);
    }

    @Override
    public <U> CompletableFuture<U> thenComposeAsync(Function<? super T, ? extends CompletionStage<U>> fn, Executor executor) {
        return super.thenComposeAsync(fn, executor);
    }

    @Override
    public CompletableFuture<T> whenComplete(BiConsumer<? super T, ? super Throwable> action) {
        return super.whenComplete(action);
    }

    @Override
    public CompletableFuture<T> whenCompleteAsync(BiConsumer<? super T, ? super Throwable> action) {
        return super.whenCompleteAsync(action);
    }

    @Override
    public CompletableFuture<T> whenCompleteAsync(BiConsumer<? super T, ? super Throwable> action, Executor executor) {
        return super.whenCompleteAsync(action, executor);
    }

    @Override
    public <U> CompletableFuture<U> handle(BiFunction<? super T, Throwable, ? extends U> fn) {
        return super.handle(fn);
    }

    @Override
    public <U> CompletableFuture<U> handleAsync(BiFunction<? super T, Throwable, ? extends U> fn) {
        return super.handleAsync(fn);
    }

    @Override
    public <U> CompletableFuture<U> handleAsync(BiFunction<? super T, Throwable, ? extends U> fn, Executor executor) {
        return super.handleAsync(fn, executor);
    }

    @Override
    public CompletableFuture<T> toCompletableFuture() {
        return super.toCompletableFuture();
    }

    @Override
    public CompletableFuture<T> exceptionally(Function<Throwable, ? extends T> fn) {
        return super.exceptionally(fn);
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return super.cancel(mayInterruptIfRunning);
    }

    @Override
    public boolean isCancelled() {
        return super.isCancelled();
    }

    @Override
    public boolean isCompletedExceptionally() {
        return super.isCompletedExceptionally();
    }

    @Override
    public void obtrudeValue(T value) {
        super.obtrudeValue(value);
    }

    @Override
    public void obtrudeException(Throwable ex) {
        super.obtrudeException(ex);
    }

    @Override
    public int getNumberOfDependents() {
        return super.getNumberOfDependents();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public <U> CompletableFuture<U> newIncompleteFuture() {
        return super.newIncompleteFuture();
    }

    @Override
    public Executor defaultExecutor() {
        return super.defaultExecutor();
    }

    @Override
    public CompletableFuture<T> copy() {
        return super.copy();
    }

    @Override
    public CompletionStage<T> minimalCompletionStage() {
        return super.minimalCompletionStage();
    }

    @Override
    public CompletableFuture<T> completeAsync(Supplier<? extends T> supplier, Executor executor) {
        return super.completeAsync(supplier, executor);
    }

    @Override
    public CompletableFuture<T> completeAsync(Supplier<? extends T> supplier) {
        return super.completeAsync(supplier);
    }

    @Override
    public CompletableFuture<T> orTimeout(long timeout, TimeUnit unit) {
        return super.orTimeout(timeout, unit);
    }

    @Override
    public CompletableFuture<T> completeOnTimeout(T value, long timeout, TimeUnit unit) {
        return super.completeOnTimeout(value, timeout, unit);
    }
}
