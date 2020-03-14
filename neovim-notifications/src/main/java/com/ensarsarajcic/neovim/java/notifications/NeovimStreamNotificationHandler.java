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

package com.ensarsarajcic.neovim.java.notifications;

import com.ensarsarajcic.neovim.java.api.util.ObjectMappers;
import com.ensarsarajcic.neovim.java.corerpc.message.NotificationMessage;
import com.ensarsarajcic.neovim.java.corerpc.reactive.ReactiveRpcStreamer;
import com.ensarsarajcic.neovim.java.notifications.buffer.BufferEvent;
import com.ensarsarajcic.neovim.java.notifications.ui.NeovimRedrawEvent;
import com.ensarsarajcic.neovim.java.notifications.ui.UIEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Flow;
import java.util.stream.Collectors;

/**
 * Implementation of {@link NeovimNotificationHandler} based on {@link ReactiveRpcStreamer}
 * <p>
 * Utilizes {@link NotificationCreatorCollector} for generating actual objects from raw data and {@link ReactiveRpcStreamer}
 * for reading incoming notifications. Output should provide properly generated notifications of the right type
 * which can be casted to access notification specific data.
 * <p>
 * Example:
 * <pre>
 *     {@code
 *     NeovimNotificationHandler notificationHandler = new NeovimNotificationHandler(streamer);
 *
 *     notification.uiEvents().subscribe(uiEventSubscriber);
 *     notification.bufferEvents().subscribe(bufferEventSubscriber);
 *     }
 * </pre>
 */
public final class NeovimStreamNotificationHandler implements NeovimNotificationHandler {
    private static final Logger log = LoggerFactory.getLogger(NeovimStreamNotificationHandler.class);

    private ReactiveRpcStreamer reactiveRPCStreamer;
    private ObjectMapper objectMapper;

    /**
     * Creates a new {@link NeovimStreamNotificationHandler} reading notifications from {@link ReactiveRpcStreamer}
     * passed in the constructor. It may not be null.
     * @param reactiveRPCStreamer streamer to be used to read notifications
     * @throws NullPointerException if reactiveRPCStreamer is null
     */
    public NeovimStreamNotificationHandler(ReactiveRpcStreamer reactiveRPCStreamer) {
        Objects.requireNonNull(reactiveRPCStreamer, "reactiveRPCStreamer is required to receive notifications");
        this.reactiveRPCStreamer = reactiveRPCStreamer;
        this.objectMapper = ObjectMappers.defaultNeovimMapper();
    }

    @Override
    public Flow.Publisher<NeovimRedrawEvent> uiEvents() {
        FilterProcessor<NotificationMessage> uiEventFilterProcessor = new FilterProcessor<>(
                notificationMessage -> notificationMessage.getName().equals(NeovimRedrawEvent.NAME));
        MappingProcessor<NotificationMessage, NeovimRedrawEvent> uiEventMappingProcessor =
                new MappingProcessor<>(notificationMessage -> {
                    List<List> rawEvents = objectMapper.convertValue(notificationMessage.getArguments(),
                            objectMapper.getTypeFactory().constructCollectionLikeType(List.class, List.class));
                    return new NeovimRedrawEvent(
                            rawEvents.stream()
                                    .map(NeovimStreamNotificationHandler::eventFromRawData)
                                    .flatMap(Collection::stream)
                                    .collect(Collectors.toList())
                    );
                });
        reactiveRPCStreamer.notificationsFlow().subscribe(uiEventFilterProcessor);
        uiEventFilterProcessor.subscribe(uiEventMappingProcessor);
        return uiEventMappingProcessor;
    }

    @Override
    public Flow.Publisher<BufferEvent> bufferEvents() {
        FilterProcessor<NotificationMessage> bufferEventFilterProcessor = new FilterProcessor<>(
                notificationMessage -> notificationMessage.getName().startsWith(BufferEvent.PREFIX));
        MappingProcessor<NotificationMessage, BufferEvent> bufferEventMappingProcessor =
                new MappingProcessor<>(
                        notificationMessage ->
                                NotificationCreatorCollector.getBufferEventCreators()
                                        .get(notificationMessage.getName())
                                        .apply(notificationMessage.getArguments())
                );
        reactiveRPCStreamer.notificationsFlow().subscribe(bufferEventFilterProcessor);
        bufferEventFilterProcessor.subscribe(bufferEventMappingProcessor);
        return bufferEventMappingProcessor;
    }

    private static List<UIEvent> eventFromRawData(List data) {
        if (data.isEmpty()) {
            log.error("Cannot create UIEvent because data is empty!");
            throw new IllegalArgumentException("Empty data!");
        }
        String name = (String) data.get(0);
        List<List> eventsList = (List<List>) data.subList(1, data.size());
        return eventsList.stream()
                .map(o -> {
                    try {
                        return NotificationCreatorCollector.getUIEventCreators().get(name).apply(o);
                    } catch (NullPointerException ex) {
                        log.error("Missing creator for ui event {}" + name, ex);
                        throw ex;
                    }
                })
                .collect(Collectors.toList());
    }
}
