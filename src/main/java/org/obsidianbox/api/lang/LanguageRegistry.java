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
package org.obsidianbox.api.lang;

import java.util.Map;

import org.obsidianbox.api.addon.Addon;

public interface LanguageRegistry {
    /**
     * Puts a new language entry into the registry. <p> This must be done during the INITIALIZE phase of Addon management or an exception will be thrown.
     *
     * @param addon The addon registering the information
     * @param lang The language we're adding to (example: Languages.ENGLISH_AMERICAN)
     * @param key The key that will be used to lookup the value (example: item.fruit.applepie)
     * @param value The value of the full group and key (example: "Apple Pie", as in item.fruit.applepie=Apple Pie)
     * @return The previous value in the map, if any.
     */
    public String put(Addon addon, Languages lang, String key, String value);

    /**
     * Returns the entire map the {@link org.obsidianbox.api.addon.Addon} has put into the registry for {@link Languages}
     *
     * @param addon The addon we are looking up
     * @param lang The language we are looking up
     * @return The map
     */
    public Map<String, String> get(Addon addon, Languages lang);
}
