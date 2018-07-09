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
import com.ensarsarajcic.neovim.java.corerpc.client.RPCStreamer;
import com.ensarsarajcic.neovim.java.handler.annotations.NeovimNotificationHandler;
import com.ensarsarajcic.neovim.java.handler.annotations.NeovimRequestHandler;
import com.ensarsarajcic.neovim.java.handler.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public final class NeovimHandlerManager {

    private NeovimHandlerProxy neovimHandlerProxy;
    private Map<Object, Map.Entry<List<RPCListener.NotificationCallback>, List<RPCListener.RequestCallback>>> handlers = new HashMap<>();

    public NeovimHandlerManager() {
        this.neovimHandlerProxy = new NeovimHandlerProxy();
    }

    public void attachToStream(RPCStreamer rpcStreamer) {
        rpcStreamer.addNotificationCallback(neovimHandlerProxy);
        rpcStreamer.addRequestCallback(neovimHandlerProxy);
    }

    public void registerNeovimHandler(Object handler) {
        if (handlers.containsKey(handler)) {
            return;
        }

        List<Map.Entry<Method, NeovimNotificationHandler>> notificationHandlers =
                ReflectionUtils.getMethodsAnnotatedWith(handler.getClass(), NeovimNotificationHandler.class);
        List<Map.Entry<Method, NeovimRequestHandler>> requestHandlers =
                ReflectionUtils.getMethodsAnnotatedWith(handler.getClass(), NeovimRequestHandler.class);

        handlers.put(handler, new AbstractMap.SimpleEntry<>(new ArrayList<>(), new ArrayList<>()));

        List<RPCListener.NotificationCallback> notificationCallbacks = notificationHandlers.stream()
                .map(methodNeovimNotificationHandlerEntry -> {
                    String notificationName = methodNeovimNotificationHandlerEntry.getValue().value();
                    return (RPCListener.NotificationCallback) notificationMessage -> {
                        if (notificationName.equals(notificationMessage.getName())) {
                            try {
                                methodNeovimNotificationHandlerEntry.getKey().invoke(handler, notificationMessage);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                e.printStackTrace();
                                throw new RuntimeException(e);
                            }
                        }
                    };
                })
                .collect(Collectors.toList());

        notificationCallbacks.forEach(neovimHandlerProxy::addNotificationCallback);
        handlers.get(handler).getKey().addAll(notificationCallbacks);

        List<RPCListener.RequestCallback> requestCallbacks = requestHandlers.stream()
                .map(methodNeovimRequestHandlerEntry -> {
                    String notificationName = methodNeovimRequestHandlerEntry.getValue().value();
                    return (RPCListener.RequestCallback) requestMessage -> {
                        if (notificationName.equals(requestMessage.getMethod())) {
                            try {
                                methodNeovimRequestHandlerEntry.getKey().invoke(handler, requestMessage);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                e.printStackTrace();
                                throw new RuntimeException(e);
                            }
                        }
                    };
                })
                .collect(Collectors.toList());

        requestCallbacks.forEach(neovimHandlerProxy::addRequestCallback);
        handlers.get(handler).getValue().addAll(requestCallbacks);
    }

    public void unregisterNeovimHandler(Object handler) {
        handlers.remove(handler);
    }
}
