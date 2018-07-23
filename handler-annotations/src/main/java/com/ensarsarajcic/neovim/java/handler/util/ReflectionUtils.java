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

package com.ensarsarajcic.neovim.java.handler.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Utilities for reflection required by this library, since all handlers are called reflectively
 */
public final class ReflectionUtils {

    private ReflectionUtils() {
        throw new AssertionError("No instances");
    }

    /**
     * Runs through passed type to find methods annotated with annotation
     * @param type type to run through
     * @param annotation annotation to look for
     * @param <T> type of annotation
     * @return list of method and annotation pairs
     */
    public static <T extends Annotation> List<Map.Entry<Method, T>> getMethodsAnnotatedWith(final Class<?> type, final Class<T> annotation) {
        final List<Map.Entry<Method, T>> methods = new ArrayList<>();
        Class<?> klass = type;
        // Iterate through hierarchy of passed type
        while (klass != Object.class) {
            // Get all methods
            final List<Method> allMethods = new ArrayList<>(Arrays.asList(klass.getDeclaredMethods()));
            for (final Method method : allMethods) {
                // Add methods that have the annotation
                if (method.isAnnotationPresent(annotation)) {
                    T annotInstance = method.getAnnotation(annotation);
                    methods.add(new AbstractMap.SimpleEntry<>(method, annotInstance));
                }
            }
            klass = klass.getSuperclass();
        }
        return methods;
    }
}
