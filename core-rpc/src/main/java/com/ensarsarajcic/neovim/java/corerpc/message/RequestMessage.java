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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.ArrayList;

/**
 * Defines a request (either made by client or server)
 * Requests are expected to be blocking! Response is expected and they should be handled immediately.
 * <p>
 * Format is defined as:
 * * type as Integer
 * * id as Integer
 * * method as String
 * * arguments as Array
 */
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@JsonPropertyOrder({"type", "id", "method", "arguments"})
@JsonDeserialize(builder = RequestMessage.Builder.class)
public final class RequestMessage implements IdentifiableMessage {

    private final String method;
    private final ArrayList<Object> arguments;
    private final int id;

    private RequestMessage(Builder builder) {
        this.method = builder.method;
        this.id = builder.id;
        this.arguments = new ArrayList<>(builder.arguments);
    }

    @JsonProperty("method")
    public String getMethod() {
        return method;
    }

    @JsonProperty("arguments")
    public ArrayList getArguments() {
        return arguments;
    }

    @Override
    @JsonProperty("id")
    public int getId() {
        return id;
    }

    @Override
    @JsonProperty("type")
    public MessageType getType() {
        return MessageType.REQUEST;
    }

    /**
     * Builder for {@link RequestMessage}
     * Outside users should not use {@link #withId(int)} method.
     * It will be replaced by the library anyway, before being sent
     */
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    @JsonPropertyOrder({"type", "id", "method", "arguments"})
    @JsonPOJOBuilder
    public static class Builder {
        private final String method;
        private final ArrayList<Object> arguments;
        private int id;

        /**
         * Prepares new builder for {@link RequestMessage} with just a method name.
         * Arguments are empty, but may be added later
         *
         * @param method name of the method
         */
        public Builder(String method) {
            this(method, new ArrayList<>());
        }

        /**
         * Prepares new builder for {@link RequestMessage} with method name and arguments.
         * More arguments may be added later
         *
         * @param method    name of the method
         * @param arguments arguments of the message
         */
        @JsonCreator
        public Builder(
                @JsonProperty("method") String method,
                @JsonProperty("arguments") ArrayList<?> arguments) {
            this.method = method;
            this.arguments = new ArrayList<>(arguments);
        }

        /**
         * Adds id to the message. This should be added just before sending the message.
         * Outside of library, this should not be used
         *
         * @param id id to add
         */
        public Builder withId(int id) {
            this.id = id;
            return this;
        }

        /**
         * Adds all arguments provided
         *
         * @param arguments argument list to add
         */
        public Builder addArguments(ArrayList<?> arguments) {
            this.arguments.addAll(arguments);
            return this;
        }

        /**
         * Adds a single argument
         *
         * @param argument argument to add
         */
        public Builder addArgument(Object argument) {
            this.arguments.add(argument);
            return this;
        }

        /**
         * Creates a new {@link RequestMessage} using arguments added to this instance
         *
         * @return a new {@link RequestMessage}. Multiple calls will create different instances.
         */
        public RequestMessage build() {
            return new RequestMessage(this);
        }
    }

    @Override
    public String toString() {
        return "RequestMessage{"
                + "method='" + method + '\''
                + ", arguments=" + arguments
                + ", id=" + id + '}';
    }
}
