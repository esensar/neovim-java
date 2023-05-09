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

import com.ensarsarajcic.neovim.java.corerpc.client.RpcListener;
import com.ensarsarajcic.neovim.java.corerpc.message.NotificationMessage;
import com.ensarsarajcic.neovim.java.corerpc.message.RequestMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

/**
 * Class acting as both a container of {@link RpcListener.RequestCallback}
 * and {@link RpcListener.NotificationCallback} and as a listener
 * <p>
 * It passes all notifications/requests to contained listeners through a {@link ExecutorService}
 * By default {@link ImmediateExecutorService} is used
 * Default instance with {@link ImmediateExecutorService} is used by default in {@link NeovimHandlerManager}
 * <p>
 * This allows handlers to not block used streamer when notifications/requests arrive and when many handlers are used
 * <p>
 * Example:
 * <pre>
 *     {@code
 *     NeovimHandlerProxy neovimHandlerProxy = new NeovimHandlerProxy(customExecutorService);
 *     NeovimHandlerManager neovimHandlerManager = new NeovimHandlerManager(neovimHandlerProxy);
 *
 *     neovimHandlerManager.registerNeovimHandler(uiEventHandler);
 *     neovimHandlerManager.attachToStream(neovimStream); // All notifications/requests are passed down using customExecutorService now
 *     }
 * </pre>
 */
public final class NeovimHandlerProxy implements RpcListener.RequestCallback, RpcListener.NotificationCallback {
    private static final Logger log = LoggerFactory.getLogger(NeovimHandlerProxy.class);

    private Map<String, List<RpcListener.NotificationCallback>> notificationCallbacks = new HashMap<>();
    private Map<String, List<RpcListener.RequestCallback>> requestCallbacks = new HashMap<>();
    private Map<String, RpcListener.RequestCallback> requestHandlers = new HashMap<>();
    private RpcListener.RequestCallback fallbackHandler;

    private ExecutorService executorService;

    public NeovimHandlerProxy() {
        this.executorService = new ImmediateExecutorService();
    }

    public NeovimHandlerProxy(ExecutorService executorService) {
        Objects.requireNonNull(executorService, "executorService may not be null");
        this.executorService = executorService;
    }

    public void addNotificationCallback(String notificationName, RpcListener.NotificationCallback notificationCallback) {
        this.notificationCallbacks.compute(notificationName, (k, list) -> {
            if (list == null) {
                list = new LinkedList<>();
            }
            list.add(notificationCallback);
            return list;
        });
        log.info("Registered a new notification callback for {} ({})", notificationName, notificationCallback);
    }

    public void addNotificationCallbacks(String notificationName, List<RpcListener.NotificationCallback> notificationCallback) {
        this.notificationCallbacks.compute(notificationName, (k, list) -> {
            if (list == null) {
                list = new LinkedList<>();
            }
            list.addAll(notificationCallback);
            return list;
        });
        log.info("Registered a new batch of notification callbacks for {} ({})", notificationName, notificationCallback.size());
    }

    public void addRequestCallback(String requestName, RpcListener.RequestCallback requestCallback) {
        this.requestCallbacks.compute(requestName, (k, list) -> {
            if (list == null) {
                list = new LinkedList<>();
            }
            list.add(requestCallback);
            return list;
        });
        log.info("Registered a new request callback for {} ({})", requestName, requestCallback);
    }

    public void addRequestCallbacks(String requestName, List<RpcListener.RequestCallback> requestCallback) {
        this.requestCallbacks.compute(requestName, (k, list) -> {
            if (list == null) {
                list = new LinkedList<>();
            }
            list.addAll(requestCallback);
            return list;
        });
        log.info("Registered a new batch of request callbacks for {} ({})", requestName, requestCallback.size());
    }

    public void setFallbackHandler(RpcListener.RequestCallback fallbackHandler) {
        this.fallbackHandler = fallbackHandler;
    }

    public void removeNotificationCallback(String notificationName, RpcListener.NotificationCallback notificationCallback) {
        this.notificationCallbacks.compute(notificationName, (k, list) -> {
            if (list == null) {
                return null;
            }
            list.remove(notificationCallback);
            return list;
        });
        log.info("Removed a notification callback for {} ({})", notificationName, notificationCallback);
    }

    public void removeNotificationCallbacks(String notificationName, List<RpcListener.NotificationCallback> notificationCallback) {
        this.notificationCallbacks.compute(notificationName, (k, list) -> {
            if (list == null) {
                return null;
            }
            list.removeAll(notificationCallback);
            return list;
        });
        log.info("Removed a batch of notification callbacks for {} ({})", notificationName, notificationCallback.size());
    }

    public void removeRequestCallback(String requestName, RpcListener.RequestCallback requestCallback) {
        this.requestCallbacks.compute(requestName, (k, list) -> {
            if (list == null) {
                return null;
            }
            list.remove(requestCallback);
            return list;
        });
        log.info("Removed a request callback for {} ({})", requestName, requestCallback);
    }

    public void removeRequestCallbacks(String requestName, List<RpcListener.RequestCallback> requestCallback) {
        this.requestCallbacks.compute(requestName, (k, list) -> {
            if (list == null) {
                return null;
            }
            list.removeAll(requestCallback);
            return list;
        });
        log.info("Removed a batch of request callbacks for {} ({})", requestName, requestCallback.size());
    }

    @Override
    public void notificationReceived(NotificationMessage notificationMessage) {
        log.debug("Passing down a notification: {}", notificationMessage);
        var callbacks = this.notificationCallbacks.get(notificationMessage.getName());
        if (callbacks != null) {
            callbacks.forEach(it -> executorService.submit(() -> it.notificationReceived(notificationMessage)));
        }
    }

    @Override
    public void requestReceived(RequestMessage requestMessage) {
        log.debug("Passing down a request: {}", requestMessage);
        var handler = this.requestHandlers.get(requestMessage.getMethod());

        if (handler == null) {
            fallbackHandler.requestReceived(requestMessage);
            return;
        }

        executorService.submit(() -> handler.requestReceived(requestMessage));

        var callbacks = this.requestCallbacks.get(requestMessage.getMethod());
        if (callbacks != null) {
            callbacks.forEach(it -> executorService.submit(() -> it.requestReceived(requestMessage)));
        }
    }
}
