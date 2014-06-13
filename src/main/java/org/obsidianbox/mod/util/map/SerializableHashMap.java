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
package org.obsidianbox.mod.util.map;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.obsidianbox.api.util.map.DefaultedKey;
import org.obsidianbox.api.util.map.SerializableMap;
import org.obsidianbox.mod.addon.AddonClassLoader;

/**
 * Manages a string keyed, serializable object hashmap that can be serialized easily to an array of bytes and deserialized from an array of bytes, intended for persistence and network transfers.
 *
 * This should not contain null values.
 */
public class SerializableHashMap implements SerializableMap {
    public static final String NILTYPE = "NULL";
    // This doesn't need to be persisted across restarts
    private static final long serialVersionUID = 1L;
    protected final ConcurrentHashMap<String, Serializable> map;

    public SerializableHashMap() {
        // Let's scale these values down a little
        // TODO: maybe tweak these a little
        this.map = new ConcurrentHashMap<>(8, .9f, 5);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        if (key instanceof String) {
            return containsKey((String) key);
        }
        return false;
    }

    public boolean containsKey(String key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public Serializable get(Object key) {
        return get(key, null);
    }

    @Override
    public Serializable put(String key, Serializable value) {
        if (value == null || NILTYPE.equals(value)) {
            return map.remove(key);
        }
        return map.put(key, value);
    }

    @Override
    public Serializable remove(Object key) {
        if (key instanceof String) {
            return remove((String) key);
        } else if (key instanceof DefaultedKey) {
            return remove(((DefaultedKey<?>) key).getKeyString());
        }
        return null;
    }

    public Serializable remove(String key) {
        return map.remove(key);
    }    @Override
    public <T> T get(String key, Class<T> clazz) {
        Serializable s = get(key);
        if (s != null) {
            try {
                return clazz.cast(s);
            } catch (ClassCastException ignore) {
            }
        }
        return null;
    }

    @Override
    public void putAll(Map<? extends String, ? extends Serializable> m) {
        for (Map.Entry<? extends String, ? extends Serializable> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<String> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<Serializable> values() {
        return new Values();
    }

    @Override
    public Set<java.util.Map.Entry<String, Serializable>> entrySet() {
        return new EntrySet();
    }

    @SuppressWarnings ("unchecked")
    @Override
    public <T extends Serializable> T get(Object key, T defaultValue) {
        if (key instanceof DefaultedKey) {
            return get((DefaultedKey<T>) key);
        }
        if (!(key instanceof String)) {
            return defaultValue;
        }

        final String keyString = (String) key;
        final T value;
        try {
            value = (T) map.get(keyString);
        } catch (ClassCastException e) {
            return defaultValue;
        }

        if (value == null || NILTYPE.equals(value)) {
            if (defaultValue == null) {
                return null;
            }
            Serializable old = putIfAbsent(keyString, defaultValue);
            if (old != null) {
                return (T) old;
            } else {
                return defaultValue;
            }
        }

        return value;
    }

    @Override
    public <T extends Serializable> T get(DefaultedKey<T> key) {
        T defaultValue = key.getDefaultValue();
        String keyString = key.getKeyString();
        return get(keyString, defaultValue);
    }

    @SuppressWarnings ("unchecked")
    @Override
    public <T extends Serializable> T put(DefaultedKey<T> key, T value) {
        String keyString = key.getKeyString();
        try {
            return (T) put(keyString, value);
        } catch (ClassCastException e) {
            return null;
        }
    }

    @SuppressWarnings ("unchecked")
    @Override
    public <T extends Serializable> T putIfAbsent(DefaultedKey<T> key, T value) {
        String keyString = key.getKeyString();
        try {
            return (T) putIfAbsent(keyString, value);
        } catch (ClassCastException e) {
            return null;
        }
    }

    @Override
    public Serializable putIfAbsent(String key, Serializable value) {
        if (value == null || NILTYPE.equals(value)) {
            return map.remove(key);
        }
        return map.putIfAbsent(key, value);
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        for (Map.Entry<? extends String, ? extends Serializable> e : entrySet()) {
            builder.append(e.getKey());
            builder.append(e.getValue());
        }
        return builder.toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SerializableHashMap)) {
            return false;
        }

        SerializableHashMap other = (SerializableHashMap) obj;
        if (isEmpty() && other.isEmpty()) {
            return true;
        }

        for (Map.Entry<? extends String, ? extends Serializable> e : entrySet()) {
            Serializable value = e.getValue();
            Serializable otherValue = other.get(e.getKey());
            if (value != null) {
                if (!value.equals(otherValue)) {
                    return false;
                }
            } else if (otherValue != null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder toString = new StringBuilder("DataMap {");
        for (Map.Entry<? extends String, ? extends Serializable> e : entrySet()) {
            toString.append("(");
            toString.append(e.getKey());
            toString.append(", ");
            toString.append(e.getValue());
            toString.append("), ");
        }
        toString.delete(toString.length() - 3, toString.length());
        toString.append("}");
        return toString.toString();
    }

    public static class AddonClassResolverObjectInputStream extends ObjectInputStream {
        public AddonClassResolverObjectInputStream(InputStream in) throws IOException {
            super(in);
        }

        @Override
        protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
            try {
                return super.resolveClass(desc);
            } catch (ClassNotFoundException e) {
                return AddonClassLoader.findAddonClass(desc.getName());
            }
        }
    }

    private final class EntrySet extends AbstractSet<Map.Entry<String, Serializable>> {
        int size = map.size();

        @Override
        public Iterator<java.util.Map.Entry<String, Serializable>> iterator() {
            return new EntryIterator();
        }

        @Override
        public int size() {
            return size;
        }
    }

    private final class Values extends AbstractCollection<Serializable> {
        @Override
        public Iterator<Serializable> iterator() {
            return new ValueIterator();
        }

        @Override
        public int size() {
            return map.size();
        }

        @Override
        public boolean contains(Object o) {
            return containsValue(o);
        }

        @Override
        public void clear() {
            map.clear();
        }
    }

    private final class EntryIterator implements Iterator<Map.Entry<String, Serializable>> {
        Serializable next, current;
        int index = 0;
        int expectedAmount = map.size();
        ArrayList<Serializable> values = new ArrayList<>();
        ArrayList<String> keys = new ArrayList<>();

        EntryIterator() {
            for (String s : map.keySet()) {
                keys.add(s);
                values.add(map.get(s));
            }
            current = null;
            if (expectedAmount == 0) {
                next = null;
            } else {
                next = values.get(index);
            }
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public Map.Entry<String, Serializable> next() {
            if (map.size() != expectedAmount) {
                throw new ConcurrentModificationException();
            }
            index++;
            current = next;
            if (index < expectedAmount) {
                next = values.get(index);
            } else {
                next = null;
            }
            return new Entry(keys.get(index - 1), current);
        }

        @Override
        public void remove() {
            if (current == null) {
                throw new IllegalStateException();
            }
            if (map.size() != expectedAmount) {
                throw new ConcurrentModificationException();
            }
            current = null;
            SerializableHashMap.this.remove(keys.get(index));
        }
    }

    private final class Entry implements Map.Entry<String, Serializable> {
        final String key;
        Serializable value;

        Entry(String key, Serializable value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public Serializable getValue() {
            return value;
        }

        @Override
        public Serializable setValue(Serializable value) {
            this.value = value;
            return SerializableHashMap.this.put(key, value);
        }
    }

    private final class ValueIterator implements Iterator<Serializable> {
        Serializable next, current;
        int index = 0;
        int expectedAmount = map.size();
        ArrayList<Serializable> values = new ArrayList<>();
        ArrayList<String> keys = new ArrayList<>();

        ValueIterator() {
            for (String s : map.keySet()) {
                keys.add(s);
                values.add(map.get(s));
            }
            if (expectedAmount > 1) {
                current = values.get(index);
                next = values.get(index + 1);
            } else if (expectedAmount > 0) {
                current = values.get(index);
                next = null;
            } else {
                current = next = null;
            }
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public Serializable next() {
            if (map.size() != expectedAmount) {
                throw new ConcurrentModificationException();
            }
            index++;
            current = next;
            if (index < expectedAmount) {
                next = values.get(index);
            } else {
                next = null;
            }
            return current;
        }

        @Override
        public void remove() {
            if (current == null) {
                throw new IllegalStateException();
            }
            if (map.size() != expectedAmount) {
                throw new ConcurrentModificationException();
            }
            current = null;
            SerializableHashMap.this.remove(keys.get(index));
        }
    }

    /**
     * This serializes only the data, as opposed to the whole object.
     */
    @Override
    public byte[] serialize() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(map);
            return out.toByteArray();
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to compress SerializableMap");
        }
    }

    /**
     * This deserializes only the data, as opposed to the whole object.
     */
    @Override
    @SuppressWarnings ("unchecked")
    public void deserialize(byte[] serializedData, boolean wipe) throws IOException {
        if (wipe) {
            map.clear();
        }
        InputStream in = new ByteArrayInputStream(serializedData);
        ObjectInputStream ois = new AddonClassResolverObjectInputStream(in);
        try {
            // Because it may be a map of maps, we want to UPDATE inner maps, not overwrite
            for (Map.Entry<String, ? extends Serializable> e : ((Map<String, ? extends Serializable>) ois.readObject()).entrySet()) {
                if (e.getValue() instanceof Map && map.get(e.getKey()) instanceof Map) {
                    ((Map) map.get(e.getKey())).putAll((Map) e.getValue());
                } else {
                    put(e.getKey(), e.getValue());
                }
            }
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException("Unable to decompress SerializableHashMap", ex);
        }
    }



    @Override
    public void deserialize(byte[] compressedData) throws IOException {
        deserialize(compressedData, true);
    }

    @Override
    public SerializableMap deepCopy() {
        SerializableMap map = new SerializableHashMap();
        try {
            map.deserialize(serialize(), true);
        } catch (IOException e) {
            throw new RuntimeException("Unable to create a deep copy!", e);
        }
        return map;
    }
}

