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

import java.util.Map;

import cpw.mods.fml.relauncher.Side;
import org.junit.Test;
import org.obsidianbox.magma.addon.Addon;
import org.obsidianbox.magma.lang.Languages;
import org.obsidianbox.obsidian.lang.CommonLanguageManager;
import org.obsidianbox.obsidian.addon.CommonAddonManager;

import static org.junit.Assert.assertTrue;

public class LanguageRegistryTest {
    @Test
    public void test() {
        final Game game = new Game(Side.SERVER);
        game.setAddonManager(new CommonAddonManager(game));
        game.setLanguages(new CommonLanguageManager(game));

        final Addon addon = ((CommonAddonManager) game.getAddonManager()).getInternalAddon();
        game.getLanguages().put(addon, Languages.ENGLISH_AMERICAN, "internal.test.testy_text", "Testy Test!");
        game.getLanguages().put(addon, Languages.KLINGON, "internal.test.brutal_test_text", "A good death");

        final Map<String, String> americanMap = game.getLanguages().get(addon, Languages.ENGLISH_AMERICAN);
        final Map<String, String> klingonMap = game.getLanguages().get(addon, Languages.KLINGON);
        assertTrue(americanMap.size() == 1 && klingonMap.size() == 1);
        assertTrue(americanMap.containsKey("internal.test.testy_text"));
        assertTrue(klingonMap.get("internal.test.brutal_test_text").equals("A good death"));
    }
}
