/**
 * This file is part of Obsidian, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2013-2014 ObsidianBox <http://obsidianbox.org/>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.obsidianbox.api.util.map;

import java.io.Serializable;
import java.util.Map;

/**
 * An extension of the default Java.util Map interface, that allows a default value to be returned when keys are not present in the map.
 */
public interface DefaultedMap<V extends Serializable> extends Map<String, V> {
    /**
     * Returns the value to which the specified key is mapped, or the default value if this map contains no mapping for the key.
     *
     * @param key the key whose associated value is to be returned
     * @param defaultValue to be returned if the key is not found
     * @return the value the key is mapped to or the default value
     */
    public <T extends V> T get(Object key, T defaultValue);

    /**
     * Returns the value to which the String for the specified key is mapped, or the default value given by the key, if this map contains no mapping for the key.<br>
     *
     * @param key the key whose associated value is to be returned
     * @return the value the key is mapped to or the default value
     */
    public <T extends V> T get(DefaultedKey<T> key);

    /**
     * Associates the specified value with the String for the given key and returns the previous value, or null if there was no previous mapping
     *
     * @param key the key whose associated value is to be returned
     * @param value the value the key is to be mapped
     * @return the previous value, or null if none
     */
    public <T extends V> T put(DefaultedKey<T> key, T value);

    /**
     * Associates the specified value with the String for the given key if there is no value associated with that key already and returns the previous value, or null if there was no previous mapping
     *
     * @param key the key whose associated value is to be returned
     * @param value the value the key is to be mapped
     * @return the previous value, or null if none
     */
    public <T extends V> T putIfAbsent(DefaultedKey<T> key, T value);

    /**
     * Associates the specified value with the key if there is no value associated with that key already and returns the previous value, or null if there was no previous mapping
     *
     * @param key the key whose associated value is to be returned
     * @param value the value the key is to be mapped
     * @return the previous value, or null if none
     */
    public V putIfAbsent(String key, V value);
}

