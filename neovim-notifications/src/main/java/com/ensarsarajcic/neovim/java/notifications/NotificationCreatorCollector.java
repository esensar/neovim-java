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
import com.ensarsarajcic.neovim.java.notifications.buffer.BufferEvent;
import com.ensarsarajcic.neovim.java.notifications.ui.UiEvent;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Class used internally by the library to find and provide creators for all notification types
 * These creators are either static fields named CREATOR which generate the object from raw array,
 * or are functions which utilize default neovim mapper to convert object
 * <p>
 * All of the creators are loaded upon first call and cached for further use
 */
final class NotificationCreatorCollector {
    private static final Logger log = LoggerFactory.getLogger(NotificationCreatorCollector.class);

    private static Map<String, Function<List, UiEvent>> uiEventCreators = null;
    private static Map<String, Function<List, BufferEvent>> bufferEventCreators = null;
    private static Reflections reflections = new Reflections("com.ensarsarajcic.neovim.java.notifications");


    private NotificationCreatorCollector() {
        throw new AssertionError("No instance");
    }
    

    private static Map<String, Function<List, UiEvent>> createUiEventCreators() {
        Map<String, Function<List, UiEvent>> uiEventCreators = new HashMap<>();
        var classes = reflections.getSubTypesOf(UiEvent.class);
        for (Class<? extends UiEvent> uiEventClass : classes) {
            if (!UiEvent.class.isAssignableFrom(uiEventClass) || uiEventClass.isInterface()) {
                continue;
            }
            try {
                Field nameField = uiEventClass.getDeclaredField("NAME");
                Function<List, UiEvent> creator = null;
                try {
                    Field creatorField = uiEventClass.getDeclaredField("CREATOR");
                    creator = (Function<List, UiEvent>) creatorField.get(null);
                } catch (NoSuchFieldException ex) {
                    creator = list -> ObjectMappers.defaultNeovimMapper().convertValue(list, uiEventClass);
                }
                String name = (String) nameField.get(null);
                uiEventCreators.put(name, creator);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                log.error("An error occurred while building creator for " + uiEventClass, e);
                e.printStackTrace();
            }
        }
        return uiEventCreators;
    }

    private static Map<String, Function<List, BufferEvent>> createBufferEventCreators() {
        Map<String, Function<List, BufferEvent>> bufferEventCreators = new HashMap<>();
        var classes = reflections.getSubTypesOf(BufferEvent.class);
        for (Class<? extends BufferEvent> bufferEventClass : classes) {
            if (!BufferEvent.class.isAssignableFrom(bufferEventClass) || bufferEventClass.isInterface()) {
                continue;
            }

            try {
                Field nameField = bufferEventClass.getDeclaredField("NAME");
                Function<List, BufferEvent> creator = null;
                try {
                    Field creatorField = bufferEventClass.getDeclaredField("CREATOR");
                    creator = (Function<List, BufferEvent>) creatorField.get(null);
                } catch (NoSuchFieldException ex) {
                    creator = list -> ObjectMappers.defaultNeovimMapper().convertValue(list, bufferEventClass);
                }
                String name = (String) nameField.get(null);
                bufferEventCreators.put(name, creator);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                log.error("An error occurred while building creator for " + bufferEventClass, e);
                e.printStackTrace();
            }
        }
        return bufferEventCreators;
    }

    /**
     * Provides all creators of {@link UiEvent} notifications
     *
     * @return Map of creators where key is notification name and value is function which creates notification from raw array
     */
    public static Map<String, Function<List, UiEvent>> getUIEventCreators() {
        if (uiEventCreators == null) {
            synchronized (NotificationCreatorCollector.class) {
                if (uiEventCreators == null) {
                    uiEventCreators = createUiEventCreators();
                }
            }
        }
        return uiEventCreators;
    }

    /**
     * Provides all creators of {@link BufferEvent} notifications
     *
     * @return Map of creators where key is notification name and value is function which creates notification from raw array
     */
    public static Map<String, Function<List, BufferEvent>> getBufferEventCreators() {
        if (bufferEventCreators == null) {
            synchronized (NotificationCreatorCollector.class) {
                if (bufferEventCreators == null) {
                    bufferEventCreators = createBufferEventCreators();
                }
            }
        }
        return bufferEventCreators;
    }
}
