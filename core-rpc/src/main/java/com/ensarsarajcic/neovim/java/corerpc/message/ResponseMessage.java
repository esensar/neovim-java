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

/**
 * Defines a response
 *
 * Format is defined as:
 *  * type as Integer
 *  * id as Integer
 *  * error as RPCError
 *  * result
 */
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@JsonPropertyOrder({"type", "id", "error", "result"})
@JsonDeserialize(builder = ResponseMessage.Builder.class)
public final class ResponseMessage implements IdentifiableMessage {

    private final int id;
    private final RpcError error;
    private final Object result;

    private ResponseMessage(Builder builder) {
        this(builder.id, builder.error, builder.result);
    }

    /**
     * Creates a new {@link ResponseMessage}
     *
     * @param id response id - it should match id of request
     * @param error error if present
     * @param result result if present
     */
    public ResponseMessage(int id, RpcError error, Object result) {
        this.id = id;
        this.error = error;
        this.result = result;
    }

    @JsonProperty("error")
    public RpcError getError() {
        return error;
    }

    @JsonProperty("result")
    public Object getResult() {
        return result;
    }

    @Override
    @JsonProperty("id")
    public int getId() {
        return id;
    }

    @Override
    @JsonProperty("type")
    public MessageType getType() {
        return MessageType.RESPONSE;
    }

    /**
     * Builder for {@link ResponseMessage} for convention
     * Outside users should not use {@link #withId(int)} method.
     * It will be replaced by the library anyway, before being sent
     */
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    @JsonPropertyOrder({"type", "id", "error", "result"})
    @JsonPOJOBuilder
    public static class Builder {
        private RpcError error;
        private Object result;
        private int id;

        /**
         * Prepares new builder for successful {@link ResponseMessage} with a result
         * Result may be changed later
         * Error may be added later, but is probably a bad thing to do
         * @param result result to send, may be any object, but it must be serializable to MsgPack
         */
        public Builder(Object result) {
            this(0, null, result);
        }

        /**
         * Prepares new builder for error {@link ResponseMessage} with an error
         * Result may be added later, but is probably a bad thing to do
         * Error may be changed later
         * @param error {@link RpcError} error to send
         */
        public Builder(RpcError error) {
            this(0, error, null);
        }

        /**
         * Prepares new builder for error {@link ResponseMessage} with an error and a result
         * Both may be changed later
         * This constructor is meant for use by deserializer
         * @param error {@link RpcError} error to send
         * @param result result to send, may be any object, but it must be serializable to MsgPack
         */
        @JsonCreator
        public Builder(
                @JsonProperty("id") int id,
                @JsonProperty("error") RpcError error,
                @JsonProperty("result") Object result) {
            this.id = id;
            this.error = error;
            this.result = result;
        }

        /**
         * Adds id to the message. This should match the request that this responds to
         * @param id id to add
         */
        public Builder withId(int id) {
            this.id = id;
            return this;
        }

        /**
         * Adds (or changes) error to the message.
         * @param error error to add
         */
        public Builder withError(RpcError error) {
            this.error = error;
            return this;
        }

        /**
         * Adds (or changes) result to the message.
         * @param result result to add
         */
        public Builder withResult(Object result) {
            this.result = result;
            return this;
        }

        /**
         * Creates a new {@link ResponseMessage} using arguments added to this instance
         * @return a new {@link ResponseMessage}. Multiple calls will create different instances.
         */
        public ResponseMessage build() {
            return new ResponseMessage(this);
        }
    }

    @Override
    public String toString() {
        return "ResponseMessage{" +
                "id=" + id +
                ", error=" + error +
                ", result=" + result +
                '}';
    }
}
