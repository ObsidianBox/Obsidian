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
package org.obsidianbox.magma;

import org.junit.Test;
import org.obsidianbox.obsidian.util.map.SerializableHashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class SerializableHashMapTest {
    @Test
    public void test() {
        final SerializableHashMap test1 = new SerializableHashMap();
        test1.put("Test1", "Test2");
        byte[] data = null;
        try {
            data = test1.serialize();
        } catch (Exception e) {
            fail("SerializableHashMap failed to serialize: " + e.toString());
        }
        if (data != null) {
            SerializableHashMap test2 = null;
            try {
                test2 = new SerializableHashMap();
                test2.deserialize(data, true);
            } catch (Exception e) {
                fail("SerializableHashMap failed to read in serialized data: " + e.toString());
            }
            assertTrue(test1.get("Test1").equals(test2.get("Test1")));
            assertEquals(test1, test2);
        }
    }
}
