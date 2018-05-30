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

import com.ensarsarajcic.neovim.java.corerpc.message.MessageType;
import com.ensarsarajcic.neovim.java.corerpc.message.NotificationMessage;
import com.ensarsarajcic.neovim.java.corerpc.message.RequestMessage;
import com.ensarsarajcic.neovim.java.corerpc.message.ResponseMessage;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BackgroundRPCListenerTest {

    @Mock
    ExecutorService executorService;

    @Mock
    ObjectMapper objectMapper;

    @Mock
    ObjectReader objectReader;

    @Mock
    InputStream inputStream;

    @InjectMocks
    BackgroundRPCListener backgroundRPCListener;

    @Test
    public void testStart() throws IOException {
        // Given a proper executor service and object mapper
        prepareSequentialExecutorService();
        given(objectMapper.reader()).willReturn(objectReader);
        given(objectReader.readTree(inputStream)).willReturn(JsonNodeFactory.instance.arrayNode(), (JsonNode) null);

        // When start is called, nothing special happens
        backgroundRPCListener.start(inputStream);
    }

    @Test
    public void testRequestListener() throws IOException {
        // Given a proper executor service and object mapper
        prepareSequentialExecutorService();
        given(objectMapper.reader()).willReturn(objectReader);
        ArrayNode requestNode = prepareRequestNode();
        RequestMessage requestMessage = new RequestMessage.Builder("test").build();
        given(objectMapper.treeToValue(any(), eq(RequestMessage.class))).willReturn(requestMessage);
        given(objectReader.readTree(inputStream)).willReturn(requestNode, (JsonNode) null);
        RPCListener.RequestCallback requestCallback = Mockito.mock(RPCListener.RequestCallback.class);

        backgroundRPCListener.listenForRequests(requestCallback);
        backgroundRPCListener.start(inputStream);

        verify(requestCallback).requestReceived(requestMessage);
    }

    @Test
    public void testRequestListenerWithoutStart() throws IOException {
        // Given a proper executor service and object mapper
        prepareSequentialExecutorService();
        RPCListener.RequestCallback requestCallback = Mockito.mock(RPCListener.RequestCallback.class);

        backgroundRPCListener.listenForRequests(requestCallback);

        verify(requestCallback, never()).requestReceived(any());
    }

    @Test
    public void testResponseListener() throws IOException {
        // Given a proper executor service and object mapper
        prepareSequentialExecutorService();
        given(objectMapper.reader()).willReturn(objectReader);
        ArrayNode responseNode = prepareResponseNode();
        ResponseMessage responseMessage = new ResponseMessage.Builder("test").build();
        given(objectMapper.treeToValue(any(), eq(ResponseMessage.class))).willReturn(responseMessage);
        given(objectReader.readTree(inputStream)).willReturn(responseNode, (JsonNode) null);
        RPCListener.ResponseCallback responseCallback = Mockito.mock(RPCListener.ResponseCallback.class);

        backgroundRPCListener.listenForResponse(responseMessage.getId(), responseCallback);
        backgroundRPCListener.start(inputStream);

        verify(responseCallback).responseReceived(responseMessage.getId(), responseMessage);
    }

    @Test
    public void testResponseListenerWithoutStart() throws IOException {
        // Given a proper executor service and object mapper
        prepareSequentialExecutorService();
        RPCListener.ResponseCallback requestCallback = Mockito.mock(RPCListener.ResponseCallback.class);

        backgroundRPCListener.listenForResponse(1, requestCallback);

        verify(requestCallback, never()).responseReceived(anyInt(), any());
    }

    @Test
    public void testNotificationListener() throws IOException {
        // Given a proper executor service and object mapper
        prepareSequentialExecutorService();
        given(objectMapper.reader()).willReturn(objectReader);
        ArrayNode notificationNode = prepareNotificationNode();
        NotificationMessage notificationMessage = new NotificationMessage.Builder("test").build();
        given(objectMapper.treeToValue(any(), eq(NotificationMessage.class))).willReturn(notificationMessage);
        given(objectReader.readTree(inputStream)).willReturn(notificationNode, (JsonNode) null);
        RPCListener.NotificationCallback notificationCallback = Mockito.mock(RPCListener.NotificationCallback.class);

        backgroundRPCListener.listenForNotifications(notificationCallback);
        backgroundRPCListener.start(inputStream);

        verify(notificationCallback).notificationReceived(notificationMessage);
    }

    @Test
    public void testNotificationListenerWithoutStart() throws IOException {
        // Given a proper executor service and object mapper
        prepareSequentialExecutorService();
        RPCListener.NotificationCallback notificationCallback = Mockito.mock(RPCListener.NotificationCallback.class);

        backgroundRPCListener.listenForNotifications(notificationCallback);

        verify(notificationCallback, never()).notificationReceived(any());
    }

    @Test(expected = NullPointerException.class)
    public void noNullExecutorService() {
        // when null executor service is passed to constructor, it throws exception
        new BackgroundRPCListener(null, objectMapper);
    }

    @Test(expected = NullPointerException.class)
    public void noNullObjectMapper() {
        // when null object mapper is passed to constructor, it throws exception
        new BackgroundRPCListener(executorService, null);
    }

    private ArrayNode prepareRequestNode() {
        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode(4);
        arrayNode.add(MessageType.REQUEST.asInt());
        arrayNode.add(MessageType.REQUEST.asInt());
        arrayNode.add(MessageType.REQUEST.asInt());
        arrayNode.add(MessageType.REQUEST.asInt());
        return arrayNode;
    }

    private ArrayNode prepareResponseNode() {
        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode(4);
        arrayNode.add(MessageType.RESPONSE.asInt());
        arrayNode.add(MessageType.RESPONSE.asInt());
        arrayNode.add(MessageType.RESPONSE.asInt());
        arrayNode.add(MessageType.RESPONSE.asInt());
        return arrayNode;
    }

    private ArrayNode prepareNotificationNode() {
        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode(3);
        arrayNode.add(MessageType.NOTIFICATION.asInt());
        arrayNode.add(MessageType.NOTIFICATION.asInt());
        arrayNode.add(MessageType.NOTIFICATION.asInt());
        return arrayNode;
    }

    private void prepareSequentialExecutorService() {
        doAnswer(invocationOnMock -> {
            ((Runnable) invocationOnMock.getArguments()[0]).run();
            return null;
        }).when(executorService).submit(any(Runnable.class));
    }
}