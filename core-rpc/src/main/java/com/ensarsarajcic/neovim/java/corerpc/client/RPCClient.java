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
 * Wrapper around {@link RPCStreamer}
 * This class should be used for communication. It provides convenience factory methods
 */
public final class RPCClient implements RPCStreamer {

    private static ObjectMapper defaultObjectMapper;
    private static ExecutorService defaultExecutorService;
    private static RPCClient defaultSharedInstance;

    private RPCStreamer rpcStreamer;

    private RPCClient(RPCStreamer rpcStreamer) {
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
            synchronized (RPCClient.class) {
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
            synchronized (RPCClient.class) {
                if (defaultExecutorService == null) {
                    defaultExecutorService = createDefaultExecutorService();
                }
            }
        }

        return defaultExecutorService;
    }

    private static RPCSender createAsyncRPCSender(ExecutorService executorService, ObjectMapper objectMapper) {
        return new AsyncRPCSender(executorService, objectMapper);
    }

    private static RPCSender createDefaultAsyncRPCSender() {
        return createAsyncRPCSender(getDefaultExecutorService(), getDefaultObjectMapper());
    }

    private static RPCListener createAsyncRPCListener(ExecutorService executorService, ObjectMapper objectMapper) {
        return new BackgroundRPCListener(executorService, objectMapper);
    }

    private static RPCListener createDefaultAsyncRPCListener() {
        return createAsyncRPCListener(getDefaultExecutorService(), getDefaultObjectMapper());
    }

    private static RPCStreamer createDefaultAsyncRPCStreamer() {
        return createRPCStreamer(createDefaultAsyncRPCSender(), createDefaultAsyncRPCListener());
    }

    private static RPCStreamer createDefaultAsyncRPCStreamer(ExecutorService executorService, ObjectMapper objectMapper) {
        return createRPCStreamer(createAsyncRPCSender(executorService, objectMapper),
                createAsyncRPCListener(executorService, objectMapper));
    }

    private static RPCStreamer createRPCStreamer(RPCSender rpcSender, RPCListener rpcListener) {
        return new PackStream(rpcSender, rpcListener);
    }

    /**
     * Creates a default instance of {@link RPCClient} based on:
     * * {@link PackStream} for two-way communication
     * * {@link AsyncRPCSender} for sending data
     * * {@link BackgroundRPCListener} for receiving data
     *
     * @return <b>New instance</b> of {@link RPCClient}
     */
    public static RPCClient createDefaultAsyncInstance() {
        return new RPCClient(createDefaultAsyncRPCStreamer());
    }

    /**
     * Takes a default instance (shared - singleton) of {@link RPCClient}
     *
     * @return <b>Default shared instance</b> of {@link RPCClient}
     */
    public static RPCClient getDefaultAsyncInstance() {
        if (defaultSharedInstance == null) {
            synchronized (RPCClient.class) {
                if (defaultSharedInstance == null) {
                    defaultSharedInstance = createDefaultAsyncInstance();
                }
            }
        }

        return defaultSharedInstance;
    }

    @Override
    public void attach(RPCConnection rpcConnection) {
        rpcStreamer.attach(rpcConnection);
    }

    @Override
    public void send(Message message) throws IOException {
        rpcStreamer.send(message);
    }

    @Override
    public void send(RequestMessage.Builder requestMessage) throws IOException {
        rpcStreamer.send(requestMessage);
    }

    @Override
    public void send(RequestMessage.Builder requestMessage, RPCListener.ResponseCallback responseCallback) throws IOException {
        rpcStreamer.send(requestMessage, responseCallback);
    }

    @Override
    public void addRequestCallback(RPCListener.RequestCallback requestCallback) {
        rpcStreamer.addRequestCallback(requestCallback);
    }

    @Override
    public void removeRequestCallback(RPCListener.RequestCallback requestCallback) {
        rpcStreamer.removeRequestCallback(requestCallback);
    }

    @Override
    public void addNotificationCallback(RPCListener.NotificationCallback notificationCallback) {
        rpcStreamer.addNotificationCallback(notificationCallback);
    }

    @Override
    public void removeNotificationCallback(RPCListener.NotificationCallback notificationCallback) {
        rpcStreamer.removeNotificationCallback(notificationCallback);
    }

    /**
     * Builder for {@link RPCClient} to simplify configuration
     * Everything is set to default at the start and following may be changed:
     * * Underlying {@link RPCStreamer}
     * * If default {@link RPCStreamer} is used, {@link RPCSender} and {@link RPCListener} may be changed
     * * If default {@link RPCSender} or {@link RPCListener} are used, {@link ObjectMapper} and {@link ExecutorService}
     * can be changed
     */
    public static class Builder {
        private ObjectMapper objectMapper = getDefaultObjectMapper();
        private ExecutorService executorService = getDefaultExecutorService();

        /**
         * Creates a default {@link RPCClient} builder
         * If build is called right after this, default instance will be <b>created</b>
         */
        public Builder() {
        }

        /**
         * Changes default {@link RPCStreamer} with the one given
         * After this change is made, dependencies of the default {@link RPCStreamer} can not be changed
         *
         * @param rpcStreamer {@link RPCStreamer} instance to use
         * @return instance of a different, more limited builder
         */
        public CustomRPCStreamerBuilder withRPCStreamer(RPCStreamer rpcStreamer) {
            Objects.requireNonNull(rpcStreamer, "rpcStreamer may not be null");
            return new CustomRPCStreamerBuilder(rpcStreamer);
        }

        /**
         * Builder used when {@link RPCStreamer} is changed
         */
        public static class CustomRPCStreamerBuilder {
            private final RPCStreamer rpcStreamer;

            private CustomRPCStreamerBuilder(RPCStreamer rpcStreamer) {
                this.rpcStreamer = rpcStreamer;
            }

            /**
             * Creates a new {@link RPCClient} instance with given {@link RPCStreamer}
             */
            public RPCClient build() {
                return new RPCClient(rpcStreamer);
            }
        }

        /**
         * Changes default {@link RPCSender} with the one given
         * After this change, default {@link RPCStreamer} is used and its dependencies may be changed
         *
         * @param rpcSender {@link RPCSender} instance to use
         * @return instance of a different, more limited builder
         */
        public CustomRPCSenderBuilder withRPCSender(RPCSender rpcSender) {
            return new CustomRPCSenderBuilder(rpcSender, executorService, objectMapper);
        }

        /**
         * Changes default {@link RPCListener} with the one given
         * After this change, default {@link RPCStreamer} is used and its dependencies may be changed
         *
         * @param rpcListener {@link RPCListener} instance to use
         * @return instance of a different, more limited builder
         */
        public CustomRPCListenerBuilder withRPCListener(RPCListener rpcListener) {
            return new CustomRPCListenerBuilder(rpcListener, executorService, objectMapper);
        }

        /**
         * Changes default {@link RPCListener} and {@link RPCSender} with the ones given
         * After this change, default {@link RPCStreamer} is used and its dependencies may be changed
         *
         * @param rpcSender   {@link RPCSender} instance to use
         * @param rpcListener {@link RPCListener} instance to use
         * @return instance of a different, more limited builder
         */
        public DefaultRPCStreamerFullBuilder withRPCSenderAndListener(RPCSender rpcSender, RPCListener rpcListener) {
            return new DefaultRPCStreamerFullBuilder(rpcSender, rpcListener);
        }

        /**
         * Builder used when both {@link RPCSender} and {@link RPCListener} are changed
         */
        public static class DefaultRPCStreamerFullBuilder {
            private RPCSender rpcSender;
            private RPCListener rpcListener;

            private DefaultRPCStreamerFullBuilder(RPCSender rpcSender, RPCListener rpcListener) {
                Objects.requireNonNull(rpcSender, "rpcSender may not be null");
                Objects.requireNonNull(rpcListener, "rpcListener may not be null");
                this.rpcSender = rpcSender;
                this.rpcListener = rpcListener;
            }

            /**
             * Changes default {@link RPCSender} with the one given
             *
             * @param rpcSender {@link RPCSender} instance to use
             */
            public DefaultRPCStreamerFullBuilder withRPCSender(RPCSender rpcSender) {
                Objects.requireNonNull(rpcSender, "rpcSender may not be null");
                this.rpcSender = rpcSender;
                return this;
            }

            /**
             * Changes default {@link RPCListener} with the one given
             *
             * @param rpcListener {@link RPCListener} instance to use
             */
            public DefaultRPCStreamerFullBuilder withRPCListener(RPCListener rpcListener) {
                Objects.requireNonNull(rpcListener, "rpcListener may not be null");
                this.rpcListener = rpcListener;
                return this;
            }

            /**
             * Creates a new {@link RPCClient} instance with default {@link RPCStreamer} and given sender and listener
             */
            public RPCClient build() {
                return new RPCClient(createRPCStreamer(rpcSender, rpcListener));
            }
        }

        /**
         * Builder used when {@link RPCSender} is changed
         */
        public static class CustomRPCSenderBuilder {
            private RPCSender rpcSender;
            private ExecutorService executorService;
            private ObjectMapper objectMapper;

            private CustomRPCSenderBuilder(RPCSender rpcSender, ExecutorService executorService, ObjectMapper objectMapper) {
                this.rpcSender = rpcSender;
                this.executorService = executorService;
                this.objectMapper = objectMapper;
            }

            /**
             * Changes default {@link RPCListener} with instance given
             * After this change, default {@link RPCListener} and its dependencies are discarded
             *
             * @param rpcListener {@link RPCListener} instance to use
             * @return an instance of a different, more limited builder
             */
            public DefaultRPCStreamerFullBuilder withRPCListener(RPCListener rpcListener) {
                return new DefaultRPCStreamerFullBuilder(rpcSender, rpcListener);
            }

            /**
             * Changes {@link RPCSender} with instance given
             *
             * @param rpcSender {@link RPCSender} instance to use
             */
            public CustomRPCSenderBuilder withRPCSender(RPCSender rpcSender) {
                Objects.requireNonNull(rpcSender, "rpcSender may not be null");
                this.rpcSender = rpcSender;
                return this;
            }

            /**
             * Changes {@link ObjectMapper} used by default {@link RPCListener} with instance given
             *
             * @param objectMapper {@link ObjectMapper} instance to use
             */
            public CustomRPCSenderBuilder withObjectMapper(ObjectMapper objectMapper) {
                Objects.requireNonNull(objectMapper, "objectMapper may not be null");
                this.objectMapper = objectMapper;
                return this;
            }

            /**
             * Changes {@link ExecutorService} used by default {@link RPCListener} with instance given
             *
             * @param executorService {@link ExecutorService} instance to use
             */
            public CustomRPCSenderBuilder withExecutorService(ExecutorService executorService) {
                Objects.requireNonNull(executorService, "executorService may not be null");
                this.executorService = executorService;
                return this;
            }

            /**
             * Creates a new {@link RPCClient} instance with default {@link RPCStreamer}, default {@link RPCListener}
             * with given {@link ObjectMapper} and {@link ExecutorService}, together with custom {@link RPCSender}
             */
            public RPCClient build() {
                return new RPCClient(createRPCStreamer(rpcSender, createAsyncRPCListener(executorService, objectMapper)));
            }
        }

        /**
         * Builder used when {@link RPCListener} is changed
         */
        public static class CustomRPCListenerBuilder {
            private RPCListener rpcListener;
            private ExecutorService executorService;
            private ObjectMapper objectMapper;

            private CustomRPCListenerBuilder(RPCListener rpcListener, ExecutorService executorService, ObjectMapper objectMapper) {
                this.rpcListener = rpcListener;
                this.executorService = executorService;
                this.objectMapper = objectMapper;
            }

            /**
             * Changes default {@link RPCSender} with instance given
             * After this change, default {@link RPCSender} and its dependencies are discarded
             *
             * @param rpcSender {@link RPCSender} instance to use
             * @return an instance of a different, more limited builder
             */
            public DefaultRPCStreamerFullBuilder withRPCSender(RPCSender rpcSender) {
                return new DefaultRPCStreamerFullBuilder(rpcSender, rpcListener);
            }

            /**
             * Changes {@link RPCListener} with instance given
             *
             * @param rpcListener {@link RPCListener} instance to use
             */
            public CustomRPCListenerBuilder withRPCListener(RPCListener rpcListener) {
                Objects.requireNonNull(rpcListener, "rpcSender may not be null");
                this.rpcListener = rpcListener;
                return this;
            }

            /**
             * Changes {@link ObjectMapper} used by default {@link RPCSender} with instance given
             *
             * @param objectMapper {@link ObjectMapper} instance to use
             */
            public CustomRPCListenerBuilder withObjectMapper(ObjectMapper objectMapper) {
                Objects.requireNonNull(objectMapper, "objectMapper may not be null");
                this.objectMapper = objectMapper;
                return this;
            }

            /**
             * Changes {@link ExecutorService} used by default {@link RPCSender} with instance given
             *
             * @param executorService {@link ExecutorService} instance to use
             */
            public CustomRPCListenerBuilder withExecutorService(ExecutorService executorService) {
                Objects.requireNonNull(executorService, "executorService may not be null");
                this.executorService = executorService;
                return this;
            }

            /**
             * Creates a new {@link RPCClient} instance with default {@link RPCStreamer}, default {@link RPCSender}
             * with given {@link ObjectMapper} and {@link ExecutorService}, together with custom {@link RPCListener}
             */
            public RPCClient build() {
                return new RPCClient(createRPCStreamer(createAsyncRPCSender(executorService, objectMapper), rpcListener));
            }
        }

        /**
         * Changes {@link ObjectMapper} used by default {@link RPCListener} with instance given
         *
         * @param objectMapper {@link ObjectMapper} instance to use
         */
        public Builder withObjectMapper(ObjectMapper objectMapper) {
            Objects.requireNonNull(objectMapper, "objectMapper may not be null");
            this.objectMapper = objectMapper;
            return this;
        }

        /**
         * Changes {@link ExecutorService} used by default {@link RPCListener} with instance given
         *
         * @param executorService {@link ExecutorService} instance to use
         */
        public Builder withExecutorService(ExecutorService executorService) {
            Objects.requireNonNull(executorService, "executorService may not be null");
            this.executorService = executorService;
            return this;
        }

        /**
         * Creates a new {@link RPCClient} instance with default {@link RPCStreamer}, {@link RPCSender} and {@link RPCListener}
         * with custom dependencies for those ({@link ExecutorService} and {@link ObjectMapper})
         */
        public RPCClient build() {
            return new RPCClient(createDefaultAsyncRPCStreamer(executorService, objectMapper));
        }
    }
}
