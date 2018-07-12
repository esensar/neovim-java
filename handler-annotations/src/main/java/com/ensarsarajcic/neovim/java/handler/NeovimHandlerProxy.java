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

import com.ensarsarajcic.neovim.java.corerpc.client.RPCListener;
import com.ensarsarajcic.neovim.java.corerpc.message.NotificationMessage;
import com.ensarsarajcic.neovim.java.corerpc.message.RequestMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

public final class NeovimHandlerProxy implements RPCListener.RequestCallback, RPCListener.NotificationCallback {

    private List<RPCListener.NotificationCallback> notificationCallbacks = new ArrayList<>();
    private List<RPCListener.RequestCallback> requestCallbacks = new ArrayList<>();

    private ExecutorService executorService;

    public NeovimHandlerProxy() {
        this.executorService = new ImmediateExecutorService();
    }

    public NeovimHandlerProxy(ExecutorService executorService) {
        Objects.requireNonNull(executorService, "executorService may not be null");
        this.executorService = executorService;
    }

    public void addNotificationCallback(RPCListener.NotificationCallback notificationCallback) {
        this.notificationCallbacks.add(notificationCallback);
    }

    public void addRequestCallback(RPCListener.RequestCallback requestCallback) {
        this.requestCallbacks.add(requestCallback);
    }

    public void removeNotificationCallback(RPCListener.NotificationCallback notificationCallback) {
        this.notificationCallbacks.remove(notificationCallback);
    }

    public void removeRequestCallback(RPCListener.RequestCallback requestCallback) {
        this.requestCallbacks.remove(requestCallback);
    }

    @Override
    public void notificationReceived(NotificationMessage notificationMessage) {
        this.notificationCallbacks.forEach(it -> executorService.submit(() -> it.notificationReceived(notificationMessage)));
    }

    @Override
    public void requestReceived(RequestMessage requestMessage) {
        this.requestCallbacks.forEach(it -> executorService.submit(() -> it.requestReceived(requestMessage)));
    }
}
