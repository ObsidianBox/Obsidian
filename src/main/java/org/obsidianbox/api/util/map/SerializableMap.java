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

import java.io.IOException;
import java.io.Serializable;

public interface SerializableMap extends DefaultedMap<Serializable>, Serializable {
    /**
     * Serializes the information in this map into an array of bytes.
     *
     * This information can be used to reconstruct a deep copy of the map, or for persistence.
     *
     * @return serialized bytes
     */
    public byte[] serialize();

    /**
     * Deserializes the array of information into the contents of the map. This will wipe all previous data in the map.
     *
     * @param data to deserialize
     * @throws IOException if an error in deserialization occurred
     */
    public void deserialize(byte[] data) throws IOException;

    /**
     * Deserializes the array of information into the contents of the map.
     *
     * @param data to deserialize
     * @param wipe true if the previous data in the map should be wiped
     * @throws IOException if an error in deserialization occurred
     */
    public void deserialize(byte[] data, boolean wipe) throws IOException;

    /**
     * Returns a deep copy of this map
     *
     * @return deep copy
     */
    public SerializableMap deepCopy();

    /**
     * Returns the value to which the specified key is mapped, or {@code null} if this map contains no mapping for the key, or the value is not a type or subtype of the given class.
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or {@code null} if this map contains no mapping for the key
     */
    public <T> T get(String key, Class<T> clazz);
}

