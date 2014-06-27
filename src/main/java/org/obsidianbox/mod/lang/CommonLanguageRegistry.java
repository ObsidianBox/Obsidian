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
package org.obsidianbox.obsidian.lang;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.obsidianbox.magma.Game;
import org.obsidianbox.magma.addon.Addon;
import org.obsidianbox.magma.lang.LanguageRegistry;
import org.obsidianbox.magma.lang.Languages;

public class CommonLanguageRegistry implements LanguageRegistry {
    private final Game game;
    /*
     * Addon -> Language -> Key (example: itemGroup.fruit.apple) -> value (example: "Apple")
     */
    private final Map<Addon, Map<Languages, Map<String, String>>> addonMap = new HashMap<>();

    public CommonLanguageRegistry(Game game) {
        this.game = game;
    }

    @Override
    public String put(Addon addon, Languages lang, String key, String value) {
        if (addon == null) {
            throw new IllegalArgumentException("Addon cannot be null!");
        }
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException(addon.getDescription().getName() + " attempted put a key that was null or empty!");
        }
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException(addon.getDescription().getName() + " attempted to put a value for " + key + " that was null or empty!");
        }

        Map<Languages, Map<String, String>> langMap = addonMap.get(addon);
        if (langMap == null) {
            langMap = new HashMap<>();
            addonMap.put(addon, langMap);
        }

        Map<String, String> keyMap = langMap.get(lang);
        if (keyMap == null) {
            keyMap = new HashMap<>();
            langMap.put(lang, keyMap);
        }

        return keyMap.put(key, value);
    }

    @Override
    public Map<String, String> get(Addon addon, Languages lang) {
        if (addon == null) {
            throw new IllegalArgumentException("Addon can not be null!");
        }
        return Collections.unmodifiableMap(addonMap.get(addon).get(lang));
    }

    public void register() {
        for (Map.Entry<Addon, Map<Languages, Map<String, String>>> addonEntry : addonMap.entrySet()) {
            final String identifier = addonEntry.getKey().getDescription().getIdentifier();

            for (Map.Entry<Languages, Map<String, String>> languageEntry : addonEntry.getValue().entrySet()) {
                Map<String, String> injectMap = new HashMap<>();
                for (Map.Entry<String, String> keyEntry : languageEntry.getValue().entrySet()) {
                    injectMap.put(identifier + "." + keyEntry.getKey(), keyEntry.getValue());
                    game.getLogger().info("Registering " + identifier + "." + keyEntry.getKey() + "=" + keyEntry.getValue());
                }
                cpw.mods.fml.common.registry.LanguageRegistry.instance().injectLanguage(languageEntry.getKey().value(), (HashMap<String, String>) injectMap);
            }
        }
    }
}
