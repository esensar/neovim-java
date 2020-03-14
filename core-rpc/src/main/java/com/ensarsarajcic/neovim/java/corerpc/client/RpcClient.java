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

package com.ensarsarajcic.neovim.java.corerpc.client;

import com.ensarsarajcic.neovim.java.corerpc.message.Message;
import com.ensarsarajcic.neovim.java.corerpc.message.RequestMessage;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.msgpack.jackson.dataformat.MessagePackFactory;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * Wrapper around {@link RpcStreamer}
 * This class should be used for communication.
 * It provides convenience factory methods
 * <p>
 * All calls are passed down to underlying {@link RpcStreamer}
 * <p>
 * Examples:
 * <pre>
 *     RPCStreamer defaultSharedClient = RPCClient.getDefaultAsyncInstance(); // shared singleton
 *
 *     RPCStreamer defaultClient = RPCClient.createDefaultAsyncInstance(); // new instance with same config as shared singleton
 *
 *     RPCStreamer customClient = new RPCClient.Builder()
 *          .withRPCStreamer(customStreamer)
 *          .build();
 *
 *     RPCStreamer customBasicsClient = new RPCClient.Builder()
 *          .withObjectMapper(customObjectMapper)
 *          .withExecutorService(customExecutorService)
 *          .build();
 *
 *     RPCStreamer customSenderListenerClient = new RPCClient.Builder()
 *          .withRPCListener(customRPCListener)
 *          .withRPCSender(customRPCSender)
 *          .build();
 * </pre>
 */
public final class RpcClient implements RpcStreamer {

    private static ObjectMapper defaultObjectMapper;
    private static ExecutorService defaultExecutorService;
    private static RpcClient defaultSharedInstance;

    private RpcStreamer rpcStreamer;

    private RpcClient(RpcStreamer rpcStreamer) {
        Objects.requireNonNull(rpcStreamer, "rpcStreamer is required for all operations");
        this.rpcStreamer = rpcStreamer;
    }

    private static ObjectMapper createDefaultObjectMapper() {
        var factory = new MessagePackFactory();
        factory.disable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
        factory.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        return new ObjectMapper(factory);
    }

    private static ObjectMapper getDefaultObjectMapper() {
        if (defaultObjectMapper == null) {
            synchronized (RpcClient.class) {
                if (defaultObjectMapper == null) {
                    defaultObjectMapper = createDefaultObjectMapper();
                }
            }
        }

        return defaultObjectMapper;
    }

    private static ExecutorService createDefaultExecutorService() {
        return new ThreadPoolExecutor(
                2,
                4,
                10000,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>()
        );
    }

    private static ExecutorService getDefaultExecutorService() {
        if (defaultExecutorService == null) {
            synchronized (RpcClient.class) {
                if (defaultExecutorService == null) {
                    defaultExecutorService = createDefaultExecutorService();
                }
            }
        }

        return defaultExecutorService;
    }

    private static RpcSender createAsyncRPCSender(ExecutorService executorService, ObjectMapper objectMapper) {
        return new AsyncRpcSender(executorService, objectMapper);
    }

    private static RpcSender createDefaultAsyncRPCSender() {
        return createAsyncRPCSender(getDefaultExecutorService(), getDefaultObjectMapper());
    }

    private static RpcListener createAsyncRPCListener(ExecutorService executorService, ObjectMapper objectMapper) {
        return new BackgroundRpcListener(executorService, objectMapper);
    }

    private static RpcListener createDefaultAsyncRPCListener() {
        return createAsyncRPCListener(getDefaultExecutorService(), getDefaultObjectMapper());
    }

    private static RpcStreamer createDefaultAsyncRPCStreamer() {
        return createRPCStreamer(createDefaultAsyncRPCSender(), createDefaultAsyncRPCListener());
    }

    private static RpcStreamer createDefaultAsyncRPCStreamer(ExecutorService executorService, ObjectMapper objectMapper) {
        return createRPCStreamer(createAsyncRPCSender(executorService, objectMapper),
                createAsyncRPCListener(executorService, objectMapper));
    }

    private static RpcStreamer createRPCStreamer(RpcSender rpcSender, RpcListener rpcListener) {
        return new PackStream(rpcSender, rpcListener);
    }

    /**
     * Creates a default instance of {@link RpcClient} based on:
     * * {@link PackStream} for two-way communication
     * * {@link AsyncRpcSender} for sending data
     * * {@link BackgroundRpcListener} for receiving data
     *
     * @return <b>New instance</b> of {@link RpcClient}
     */
    public static RpcClient createDefaultAsyncInstance() {
        return new RpcClient(createDefaultAsyncRPCStreamer());
    }

    /**
     * Takes a default instance (shared - singleton) of {@link RpcClient}
     *
     * @return <b>Default shared instance</b> of {@link RpcClient}
     */
    public static RpcClient getDefaultAsyncInstance() {
        if (defaultSharedInstance == null) {
            synchronized (RpcClient.class) {
                if (defaultSharedInstance == null) {
                    defaultSharedInstance = createDefaultAsyncInstance();
                }
            }
        }

        return defaultSharedInstance;
    }

    /**
     * Calls underlying {@link RpcStreamer}
     *
     * @param rpcConnection connection to attach to
     */
    @Override
    public void attach(RpcConnection rpcConnection) {
        rpcStreamer.attach(rpcConnection);
    }

    /**
     * Calls underlying {@link RpcStreamer}
     *
     * @param message message to send
     * @throws IOException when underlying {@link RpcStreamer} throws
     */
    @Override
    public void send(Message message) throws IOException {
        rpcStreamer.send(message);
    }

    /**
     * Calls underlying {@link RpcStreamer}
     *
     * @param requestMessage {@link RequestMessage.Builder} of message to send
     * @throws IOException when underlying {@link RpcStreamer} throws
     */
    @Override
    public void send(RequestMessage.Builder requestMessage) throws IOException {
        rpcStreamer.send(requestMessage);
    }

    /**
     * Calls underlying {@link RpcStreamer}
     *
     * @param requestMessage   {@link RequestMessage.Builder} of message to send
     * @param responseCallback {@link RpcListener.ResponseCallback} to be called when response arrives
     * @throws IOException when underlying {@link RpcStreamer} throws
     */
    @Override
    public void send(RequestMessage.Builder requestMessage, RpcListener.ResponseCallback responseCallback) throws IOException {
        rpcStreamer.send(requestMessage, responseCallback);
    }

    /**
     * Calls underlying {@link RpcStreamer}
     *
     * @param requestCallback {@link RpcListener.RequestCallback} to add
     */
    @Override
    public void addRequestCallback(RpcListener.RequestCallback requestCallback) {
        rpcStreamer.addRequestCallback(requestCallback);
    }

    /**
     * Calls underlying {@link RpcStreamer}
     *
     * @param requestCallback {@link RpcListener.RequestCallback} to remove
     */
    @Override
    public void removeRequestCallback(RpcListener.RequestCallback requestCallback) {
        rpcStreamer.removeRequestCallback(requestCallback);
    }

    /**
     * Calls underlying {@link RpcStreamer}
     *
     * @param notificationCallback {@link RpcListener.NotificationCallback} to add
     */
    @Override
    public void addNotificationCallback(RpcListener.NotificationCallback notificationCallback) {
        rpcStreamer.addNotificationCallback(notificationCallback);
    }

    /**
     * Calls underlying {@link RpcStreamer}
     *
     * @param notificationCallback {@link RpcListener.NotificationCallback} to remove
     */
    @Override
    public void removeNotificationCallback(RpcListener.NotificationCallback notificationCallback) {
        rpcStreamer.removeNotificationCallback(notificationCallback);
    }

    /**
     * Stops the underlying {@link RpcListener}
     * It is not expected for implementation to be reusable after calling this method!
     */
    @Override
    public void stop() {
      rpcStreamer.stop();
    }

    /**
     * Builder for {@link RpcClient} to simplify configuration
     * Everything is set to default at the start and following may be changed:
     * * Underlying {@link RpcStreamer}
     * * If default {@link RpcStreamer} is used, {@link RpcSender} and {@link RpcListener} may be changed
     * * If default {@link RpcSender} or {@link RpcListener} are used, {@link ObjectMapper} and {@link ExecutorService}
     * can be changed
     */
    public static class Builder {
        private ObjectMapper objectMapper = getDefaultObjectMapper();
        private ExecutorService executorService = getDefaultExecutorService();

        /**
         * Creates a default {@link RpcClient} builder
         * If build is called right after this, default instance will be <b>created</b>
         */
        public Builder() {
        }

        /**
         * Changes default {@link RpcStreamer} with the one given
         * After this change is made, dependencies of the default {@link RpcStreamer} can not be changed
         *
         * @param rpcStreamer {@link RpcStreamer} instance to use
         * @return instance of a different, more limited builder
         */
        public CustomRPCStreamerBuilder withRPCStreamer(RpcStreamer rpcStreamer) {
            Objects.requireNonNull(rpcStreamer, "rpcStreamer may not be null");
            return new CustomRPCStreamerBuilder(rpcStreamer);
        }

        /**
         * Builder used when {@link RpcStreamer} is changed
         */
        public static class CustomRPCStreamerBuilder {
            private final RpcStreamer rpcStreamer;

            private CustomRPCStreamerBuilder(RpcStreamer rpcStreamer) {
                this.rpcStreamer = rpcStreamer;
            }

            /**
             * Creates a new {@link RpcClient} instance with given {@link RpcStreamer}
             */
            public RpcClient build() {
                return new RpcClient(rpcStreamer);
            }
        }

        /**
         * Changes default {@link RpcSender} with the one given
         * After this change, default {@link RpcStreamer} is used and its dependencies may be changed
         *
         * @param rpcSender {@link RpcSender} instance to use
         * @return instance of a different, more limited builder
         */
        public CustomRPCSenderBuilder withRPCSender(RpcSender rpcSender) {
            return new CustomRPCSenderBuilder(rpcSender, executorService, objectMapper);
        }

        /**
         * Changes default {@link RpcListener} with the one given
         * After this change, default {@link RpcStreamer} is used and its dependencies may be changed
         *
         * @param rpcListener {@link RpcListener} instance to use
         * @return instance of a different, more limited builder
         */
        public CustomRPCListenerBuilder withRPCListener(RpcListener rpcListener) {
            return new CustomRPCListenerBuilder(rpcListener, executorService, objectMapper);
        }

        /**
         * Changes default {@link RpcListener} and {@link RpcSender} with the ones given
         * After this change, default {@link RpcStreamer} is used and its dependencies may be changed
         *
         * @param rpcSender   {@link RpcSender} instance to use
         * @param rpcListener {@link RpcListener} instance to use
         * @return instance of a different, more limited builder
         */
        public DefaultRPCStreamerFullBuilder withRPCSenderAndListener(RpcSender rpcSender, RpcListener rpcListener) {
            return new DefaultRPCStreamerFullBuilder(rpcSender, rpcListener);
        }

        /**
         * Builder used when both {@link RpcSender} and {@link RpcListener} are changed
         */
        public static class DefaultRPCStreamerFullBuilder {
            private RpcSender rpcSender;
            private RpcListener rpcListener;

            private DefaultRPCStreamerFullBuilder(RpcSender rpcSender, RpcListener rpcListener) {
                Objects.requireNonNull(rpcSender, "rpcSender may not be null");
                Objects.requireNonNull(rpcListener, "rpcListener may not be null");
                this.rpcSender = rpcSender;
                this.rpcListener = rpcListener;
            }

            /**
             * Changes default {@link RpcSender} with the one given
             *
             * @param rpcSender {@link RpcSender} instance to use
             */
            public DefaultRPCStreamerFullBuilder withRPCSender(RpcSender rpcSender) {
                Objects.requireNonNull(rpcSender, "rpcSender may not be null");
                this.rpcSender = rpcSender;
                return this;
            }

            /**
             * Changes default {@link RpcListener} with the one given
             *
             * @param rpcListener {@link RpcListener} instance to use
             */
            public DefaultRPCStreamerFullBuilder withRPCListener(RpcListener rpcListener) {
                Objects.requireNonNull(rpcListener, "rpcListener may not be null");
                this.rpcListener = rpcListener;
                return this;
            }

            /**
             * Creates a new {@link RpcClient} instance with default {@link RpcStreamer} and given sender and listener
             */
            public RpcClient build() {
                return new RpcClient(createRPCStreamer(rpcSender, rpcListener));
            }
        }

        /**
         * Builder used when {@link RpcSender} is changed
         */
        public static class CustomRPCSenderBuilder {
            private RpcSender rpcSender;
            private ExecutorService executorService;
            private ObjectMapper objectMapper;

            private CustomRPCSenderBuilder(RpcSender rpcSender, ExecutorService executorService, ObjectMapper objectMapper) {
                this.rpcSender = rpcSender;
                this.executorService = executorService;
                this.objectMapper = objectMapper;
            }

            /**
             * Changes default {@link RpcListener} with instance given
             * After this change, default {@link RpcListener} and its dependencies are discarded
             *
             * @param rpcListener {@link RpcListener} instance to use
             * @return an instance of a different, more limited builder
             */
            public DefaultRPCStreamerFullBuilder withRPCListener(RpcListener rpcListener) {
                return new DefaultRPCStreamerFullBuilder(rpcSender, rpcListener);
            }

            /**
             * Changes {@link RpcSender} with instance given
             *
             * @param rpcSender {@link RpcSender} instance to use
             */
            public CustomRPCSenderBuilder withRPCSender(RpcSender rpcSender) {
                Objects.requireNonNull(rpcSender, "rpcSender may not be null");
                this.rpcSender = rpcSender;
                return this;
            }

            /**
             * Changes {@link ObjectMapper} used by default {@link RpcListener} with instance given
             *
             * @param objectMapper {@link ObjectMapper} instance to use
             */
            public CustomRPCSenderBuilder withObjectMapper(ObjectMapper objectMapper) {
                Objects.requireNonNull(objectMapper, "objectMapper may not be null");
                this.objectMapper = objectMapper;
                return this;
            }

            /**
             * Changes {@link ExecutorService} used by default {@link RpcListener} with instance given
             *
             * @param executorService {@link ExecutorService} instance to use
             */
            public CustomRPCSenderBuilder withExecutorService(ExecutorService executorService) {
                Objects.requireNonNull(executorService, "executorService may not be null");
                this.executorService = executorService;
                return this;
            }

            /**
             * Creates a new {@link RpcClient} instance with default {@link RpcStreamer}, default {@link RpcListener}
             * with given {@link ObjectMapper} and {@link ExecutorService}, together with custom {@link RpcSender}
             */
            public RpcClient build() {
                return new RpcClient(createRPCStreamer(rpcSender, createAsyncRPCListener(executorService, objectMapper)));
            }
        }

        /**
         * Builder used when {@link RpcListener} is changed
         */
        public static class CustomRPCListenerBuilder {
            private RpcListener rpcListener;
            private ExecutorService executorService;
            private ObjectMapper objectMapper;

            private CustomRPCListenerBuilder(RpcListener rpcListener, ExecutorService executorService, ObjectMapper objectMapper) {
                this.rpcListener = rpcListener;
                this.executorService = executorService;
                this.objectMapper = objectMapper;
            }

            /**
             * Changes default {@link RpcSender} with instance given
             * After this change, default {@link RpcSender} and its dependencies are discarded
             *
             * @param rpcSender {@link RpcSender} instance to use
             * @return an instance of a different, more limited builder
             */
            public DefaultRPCStreamerFullBuilder withRPCSender(RpcSender rpcSender) {
                return new DefaultRPCStreamerFullBuilder(rpcSender, rpcListener);
            }

            /**
             * Changes {@link RpcListener} with instance given
             *
             * @param rpcListener {@link RpcListener} instance to use
             */
            public CustomRPCListenerBuilder withRPCListener(RpcListener rpcListener) {
                Objects.requireNonNull(rpcListener, "rpcSender may not be null");
                this.rpcListener = rpcListener;
                return this;
            }

            /**
             * Changes {@link ObjectMapper} used by default {@link RpcSender} with instance given
             *
             * @param objectMapper {@link ObjectMapper} instance to use
             */
            public CustomRPCListenerBuilder withObjectMapper(ObjectMapper objectMapper) {
                Objects.requireNonNull(objectMapper, "objectMapper may not be null");
                this.objectMapper = objectMapper;
                return this;
            }

            /**
             * Changes {@link ExecutorService} used by default {@link RpcSender} with instance given
             *
             * @param executorService {@link ExecutorService} instance to use
             */
            public CustomRPCListenerBuilder withExecutorService(ExecutorService executorService) {
                Objects.requireNonNull(executorService, "executorService may not be null");
                this.executorService = executorService;
                return this;
            }

            /**
             * Creates a new {@link RpcClient} instance with default {@link RpcStreamer}, default {@link RpcSender}
             * with given {@link ObjectMapper} and {@link ExecutorService}, together with custom {@link RpcListener}
             */
            public RpcClient build() {
                return new RpcClient(createRPCStreamer(createAsyncRPCSender(executorService, objectMapper), rpcListener));
            }
        }

        /**
         * Changes {@link ObjectMapper} used by default {@link RpcListener} with instance given
         *
         * @param objectMapper {@link ObjectMapper} instance to use
         */
        public Builder withObjectMapper(ObjectMapper objectMapper) {
            Objects.requireNonNull(objectMapper, "objectMapper may not be null");
            this.objectMapper = objectMapper;
            return this;
        }

        /**
         * Changes {@link ExecutorService} used by default {@link RpcListener} with instance given
         *
         * @param executorService {@link ExecutorService} instance to use
         */
        public Builder withExecutorService(ExecutorService executorService) {
            Objects.requireNonNull(executorService, "executorService may not be null");
            this.executorService = executorService;
            return this;
        }

        /**
         * Creates a new {@link RpcClient} instance with default {@link RpcStreamer}, {@link RpcSender} and {@link RpcListener}
         * with custom dependencies for those ({@link ExecutorService} and {@link ObjectMapper})
         */
        public RpcClient build() {
            return new RpcClient(createDefaultAsyncRPCStreamer(executorService, objectMapper));
        }
    }
}
