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
import com.ensarsarajcic.neovim.java.notifications.ui.UIEvent;
import com.ensarsarajcic.neovim.java.notifications.util.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;

final class NotificationCreatorCollector {

    private static Map<String, Function<List, UIEvent>> uiEventCreators = null;
    private static Map<String, Function<List, BufferEvent>> bufferEventCreators = null;


    private NotificationCreatorCollector() {
        throw new AssertionError("No instance");
    }

    private static Map<String, Function<List, UIEvent>> createUIEventCreators() {
        Map<String, Function<List, UIEvent>> uiEventCreators = new HashMap<>();
        try {
            Class[] classes = ReflectionUtils.getClasses(UIEvent.class.getPackageName());
            for (Class clazz : classes) {
                if (!UIEvent.class.isAssignableFrom(clazz)) {
                    continue;
                }
                Class<? extends UIEvent> uiEventClass = clazz;

                try {
                    Field nameField = uiEventClass.getDeclaredField("NAME");
                    Function<List, UIEvent> creator = null;
                    try {
                        Field creatorField = uiEventClass.getDeclaredField("CREATOR");
                        creator = (Function<List, UIEvent>) creatorField.get(null);
                    } catch (NoSuchFieldException ex) {
                        creator = list -> ObjectMappers.defaultNeovimMapper().convertValue(list, uiEventClass);
                    }
                    String name = (String) nameField.get(null);
                    uiEventCreators.put(name, creator);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return uiEventCreators;
    }

    private static Map<String, Function<List, BufferEvent>> createBufferEventCreators() {
        Map<String, Function<List, BufferEvent>> bufferEventCreators = new HashMap<>();
        try {
            Class[] classes = ReflectionUtils.getClasses(BufferEvent.class.getPackageName());
            for (Class clazz : classes) {
                if (!BufferEvent.class.isAssignableFrom(clazz)) {
                    continue;
                }
                Class<? extends BufferEvent> bufferEventClass = clazz;

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
                    e.printStackTrace();
                }
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return bufferEventCreators;
    }

    public static Map<String, Function<List, UIEvent>> getUIEventCreators() {
        if (uiEventCreators == null) {
            synchronized (NotificationCreatorCollector.class) {
                if (uiEventCreators == null) {
                    uiEventCreators = createUIEventCreators();
                }
            }
        }
        return uiEventCreators;
    }

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
