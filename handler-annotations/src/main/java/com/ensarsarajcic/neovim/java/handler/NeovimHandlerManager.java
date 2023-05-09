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
import com.ensarsarajcic.neovim.java.corerpc.client.RpcStreamer;
import com.ensarsarajcic.neovim.java.corerpc.message.NotificationMessage;
import com.ensarsarajcic.neovim.java.corerpc.message.RequestMessage;
import com.ensarsarajcic.neovim.java.corerpc.message.ResponseMessage;
import com.ensarsarajcic.neovim.java.corerpc.message.RpcError;
import com.ensarsarajcic.neovim.java.handler.annotations.NeovimNotificationHandler;
import com.ensarsarajcic.neovim.java.handler.annotations.NeovimRequestHandler;
import com.ensarsarajcic.neovim.java.handler.annotations.NeovimRequestListener;
import com.ensarsarajcic.neovim.java.handler.errors.NeovimHandlerException;
import com.ensarsarajcic.neovim.java.handler.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

/**
 * Simple class used to register and connect {@link NeovimNotificationHandler} and {@link NeovimRequestHandler}
 * It provides methods to register/unregister any kind of object as neovim handler, but only classes with methods
 * annotated with {@link NeovimNotificationHandler} or {@link NeovimRequestHandler} can be useful, since this will
 * find such methods and call them once notifications/requests arrive
 * <p>
 * To get notifications/requests it should first be attached to a {@link RpcStreamer} using {@link #attachToStream(RpcStreamer)}
 * Underneath, {@link NeovimHandlerProxy} is used to dispatch notifications/requests, which delegates thread management to it
 * <p>
 * Examples:
 * <pre>
 *     {@code
 *     NeovimHandlerManager neovimHandlerManager = new NeovimHandlerManager();
 *
 *     neovimHandlerManager.registerNeovimHandler(uiEventHandler);
 *     neovimHandlerManager.attachToStream(neovimStream);
 *     }
 * </pre>
 */
public final class NeovimHandlerManager {
    private static final Logger log = LoggerFactory.getLogger(NeovimHandlerManager.class);

    private final NeovimHandlerProxy neovimHandlerProxy;
    private final Map<Object, HandlerEntry> handlers = new HashMap<>();
    private final BiFunction<Class<?>, Object, Object> typeMapper;
    private WeakReference<RpcStreamer> rpcStreamer = new WeakReference<>(null);

    /**
     * Creates a new {@link NeovimHandlerManager} with default {@link NeovimHandlerProxy} using {@link ImmediateExecutorService}
     */
    public NeovimHandlerManager() {
        this(new NeovimHandlerProxy());
    }

    /**
     * Creates a new {@link NeovimHandlerManager} with given {@link NeovimHandlerProxy}
     *
     * @param neovimHandlerProxy proxy to use for dispatching notifications/messages
     * @throws NullPointerException if passed {@link NeovimHandlerProxy} is null
     */
    public NeovimHandlerManager(NeovimHandlerProxy neovimHandlerProxy) {
        this(neovimHandlerProxy, Class::cast);
    }

    /**
     * Creates a new {@link NeovimHandlerManager} with given {@link NeovimHandlerProxy} and type mapper
     *
     * @param neovimHandlerProxy proxy to use for dispatching notifications/messages
     * @param typeMapper function that attempts to map given argument to the given class
     * @throws NullPointerException if passed {@link NeovimHandlerProxy} is null
     */
    public NeovimHandlerManager(NeovimHandlerProxy neovimHandlerProxy, BiFunction<Class<?>, Object, Object> typeMapper) {
        Objects.requireNonNull(neovimHandlerProxy, "neovimHandlerProxy is required to dispatch notifications/messages");
        Objects.requireNonNull(typeMapper, "typeMapper is required to map arguments to handler methods");
        this.neovimHandlerProxy = neovimHandlerProxy;
        this.typeMapper = typeMapper;
    }

    /**
     * Attaches to given {@link RpcStreamer}
     * {@link RpcStreamer} does not have to be attached to an actual connection at the time of this call, since this
     * only sets up the notification/request callbacks
     *
     * @param rpcStreamer streamer whose notifications/requests should be analysed
     * @throws NullPointerException if {@link RpcStreamer} is null
     */
    public void attachToStream(RpcStreamer rpcStreamer) {
        log.info("Attaching handler manager to streamer ({})", rpcStreamer);
        Objects.requireNonNull(rpcStreamer, "rpcStreamer may not be null");
        rpcStreamer.addNotificationCallback(neovimHandlerProxy);
        rpcStreamer.addRequestCallback(neovimHandlerProxy);
        this.rpcStreamer = new WeakReference<>(rpcStreamer);

        this.neovimHandlerProxy.setFallbackHandler(requestMessage -> {
            var streamer = this.rpcStreamer.get();
            if (streamer != null) {
                try {
                    streamer.send(new ResponseMessage(requestMessage.getId(), RpcError.exception("No handler found for " + requestMessage.getMethod()), null));
                } catch (IOException e) {
                    log.error("Error occurred while invoking fallback handler for request: " + requestMessage.getMethod(), e);
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Registers a new handler
     * If this object is already registered, this is a no-op
     * If this object has methods annotated with {@link NeovimNotificationHandler} or {@link NeovimRequestHandler},
     * this method will have no effect, but it will still reflectively look for such methods
     * <p>
     * This works for both static and instance methods
     * <p>
     * Passed objects methods are prepared to be called when new notifications/requests arrive from attached {@link RpcStreamer}
     * This may be called prior to attaching, but no notifications/requests can arrive before attaching
     *
     * @param handler object to search for annotated methods
     */
    public void registerNeovimHandler(Object handler) {
        log.info("New neovim handler registered ({})", handler);
        if (handlers.containsKey(handler)) {
            return;
        }

        List<Map.Entry<Method, NeovimNotificationHandler>> notificationHandlers =
                ReflectionUtils.getMethodsAnnotatedWith(handler.getClass(), NeovimNotificationHandler.class);
        List<Map.Entry<Method, NeovimRequestListener>> requestListeners =
                ReflectionUtils.getMethodsAnnotatedWith(handler.getClass(), NeovimRequestListener.class);
        List<Map.Entry<Method, NeovimRequestHandler>> requestHandlers =
                ReflectionUtils.getMethodsAnnotatedWith(handler.getClass(), NeovimRequestHandler.class);

        HandlerEntry handlerEntry = new HandlerEntry();

        notificationHandlers.stream()
                .map(methodNeovimNotificationHandlerEntry -> {
                    String requestedName = methodNeovimNotificationHandlerEntry.getValue().value();
                    if (requestedName.isEmpty()) {
                        requestedName = String.format("%s.%s", handler.getClass().getCanonicalName(), methodNeovimNotificationHandlerEntry.getKey().getName());
                    }
                    final String notificationName = requestedName;
                    return new AbstractMap.SimpleEntry<String, RpcListener.NotificationCallback>(requestedName, notificationMessage -> {
                        try {
                            Object targetObject = null;
                            if (!Modifier.isStatic(methodNeovimNotificationHandlerEntry.getKey().getModifiers())) {
                                targetObject = handler;
                            }

                            if (methodNeovimNotificationHandlerEntry.getKey().getParameterCount() == 0) {
                                methodNeovimNotificationHandlerEntry.getKey().invoke(targetObject);
                            } else if (methodNeovimNotificationHandlerEntry.getKey().getParameterCount() == 1 && methodNeovimNotificationHandlerEntry.getKey().getParameterTypes()[0] == NotificationMessage.class) {
                                methodNeovimNotificationHandlerEntry.getKey().invoke(targetObject, notificationMessage);
                            } else {
                                ReflectionUtils.invokeMethodWithArgs(
                                        targetObject,
                                        methodNeovimNotificationHandlerEntry.getKey(),
                                        notificationMessage.getArguments(),
                                        typeMapper
                                );
                            }
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            log.error("Error occurred while invoking handler for notification: " + notificationName, e);
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }
                    });
                }).forEach((kv) -> handlerEntry.addNotificationCallback(kv.getKey(), kv.getValue()));

        requestHandlers.stream()
                .map(methodNeovimRequestHandlerEntry -> {
                    String requestedName = methodNeovimRequestHandlerEntry.getValue().value();
                    if (requestedName.isEmpty()) {
                        requestedName = String.format("%s.%s", handler.getClass().getCanonicalName(), methodNeovimRequestHandlerEntry.getKey().getName());
                    }
                    final String requestName = requestedName;
                    return new AbstractMap.SimpleEntry<String, RpcListener.RequestCallback>(requestedName, requestMessage -> {
                        try {
                            Object result = null;
                            NeovimHandlerException error = null;
                            try {
                                Object targetObject = null;
                                if (!Modifier.isStatic(methodNeovimRequestHandlerEntry.getKey().getModifiers())) {
                                    targetObject = handler;
                                }

                                if (methodNeovimRequestHandlerEntry.getKey().getParameterCount() == 0) {
                                    result = methodNeovimRequestHandlerEntry.getKey().invoke(targetObject);
                                } else if (methodNeovimRequestHandlerEntry.getKey().getParameterCount() == 1 && methodNeovimRequestHandlerEntry.getKey().getParameterTypes()[0] == RequestMessage.class) {
                                    result = methodNeovimRequestHandlerEntry.getKey().invoke(targetObject, requestMessage);
                                } else {
                                    result = ReflectionUtils.invokeMethodWithArgs(
                                            targetObject,
                                            methodNeovimRequestHandlerEntry.getKey(),
                                            requestMessage.getArguments(),
                                            typeMapper
                                    );
                                }
                            } catch (InvocationTargetException | IllegalAccessException ex) {
                                if (ex.getCause() instanceof NeovimHandlerException) {
                                    error = (NeovimHandlerException) ex.getCause();
                                } else {
                                    error = new NeovimHandlerException(ex.getMessage());
                                }
                            }
                            var streamer = rpcStreamer.get();
                            if (streamer != null) {
                                streamer.send(new ResponseMessage(requestMessage.getId(), error != null ? error.toRpcError() : null, result));
                            }
                        } catch (IOException e) {
                            log.error("Error ocurred while invoking handler for request: " + requestName, e);
                            e.printStackTrace();
                        }
                    });
                }).forEach(kv -> handlerEntry.setRequestCallback(kv.getKey(), kv.getValue()));

        requestListeners.stream()
                .map(methodNeovimRequestListenerEntry -> {
                    String requestedName = methodNeovimRequestListenerEntry.getValue().value();
                    if (requestedName.isEmpty()) {
                        requestedName = String.format("%s.%s", handler.getClass().getCanonicalName(), methodNeovimRequestListenerEntry.getKey().getName());
                    }
                    final String requestName = requestedName;
                    return new AbstractMap.SimpleEntry<String, RpcListener.RequestCallback>(requestName, requestMessage -> {
                        try {
                            Object targetObject = null;
                            if (!Modifier.isStatic(methodNeovimRequestListenerEntry.getKey().getModifiers())) {
                                targetObject = handler;
                            }

                            if (methodNeovimRequestListenerEntry.getKey().getParameterCount() == 0) {
                                methodNeovimRequestListenerEntry.getKey().invoke(targetObject);
                            } else if (methodNeovimRequestListenerEntry.getKey().getParameterCount() == 1 && methodNeovimRequestListenerEntry.getKey().getParameterTypes()[0] == RequestMessage.class) {
                                methodNeovimRequestListenerEntry.getKey().invoke(targetObject, requestMessage);
                            } else {
                                ReflectionUtils.invokeMethodWithArgs(
                                        targetObject,
                                        methodNeovimRequestListenerEntry.getKey(),
                                        requestMessage.getArguments(),
                                        typeMapper
                                );
                            }
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            log.error("Error ocurred while invoking request listener for request: " + requestName, e);
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }
                    });
                }).forEach(kv -> handlerEntry.addRequestListener(kv.getKey(), kv.getValue()));

        handlers.put(
                handler,
                handlerEntry
        );
        handlerEntry.register(neovimHandlerProxy);
    }

    /**
     * Unregisters passed handler
     * If it was not registered previously, this is a no-op
     * It will no longer receive notifications/requests after this point, unless it is registered again
     *
     * @param handler object to unregister
     */
    public void unregisterNeovimHandler(Object handler) {
        log.info("Neovim handler unregistered ({})", handler);
        var callbacks = handlers.remove(handler);
        callbacks.unregister(neovimHandlerProxy);
    }

    private static final class HandlerEntry {

        private final Map<String, RpcListener.RequestCallback> requestCallbacks = new HashMap<>();
        private final Map<String, List<RpcListener.RequestCallback>> requestListeners = new HashMap<>();
        private final Map<String, List<RpcListener.NotificationCallback>> notificationListener = new HashMap<>();

        public void addNotificationCallback(String notificationName, RpcListener.NotificationCallback notificationCallback) {
            this.notificationListener.compute(notificationName, (k, list) -> {
                if (list == null) {
                    list = new LinkedList<>();
                }
                list.add(notificationCallback);
                return list;
            });
        }

        public void addRequestListener(String requestName, RpcListener.RequestCallback requestCallback) {
            this.requestListeners.compute(requestName, (k, list) -> {
                if (list == null) {
                    list = new LinkedList<>();
                }
                list.add(requestCallback);
                return list;
            });
        }

        public void setRequestCallback(String requestName, RpcListener.RequestCallback requestCallback) {
            this.requestCallbacks.put(requestName, requestCallback);
        }

        public void register(NeovimHandlerProxy handlerProxy) {
            requestCallbacks.forEach(handlerProxy::addRequestCallback);
            requestListeners.forEach(handlerProxy::addRequestCallbacks);
            notificationListener.forEach(handlerProxy::addNotificationCallbacks);
        }

        public void unregister(NeovimHandlerProxy handlerProxy) {
            requestCallbacks.forEach(handlerProxy::removeRequestCallback);
            requestListeners.forEach(handlerProxy::removeRequestCallbacks);
            notificationListener.forEach(handlerProxy::removeNotificationCallbacks);
        }
    }
}
