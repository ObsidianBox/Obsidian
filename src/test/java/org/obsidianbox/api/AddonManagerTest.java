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
package org.obsidianbox.api;

import cpw.mods.fml.relauncher.Side;
import org.junit.Test;
import org.obsidianbox.mod.addon.CommonAddonManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class AddonManagerTest {
    @Test
    public void test() {
        // Test internal addon code
        final Game game = new Game(Side.SERVER);
        game.setAddonManager(new CommonAddonManager(game));
        boolean exceptionCaught = false;
        try {
            game.getAddonManager().getAddon("internal");
        } catch (IllegalStateException ignore) {
            exceptionCaught = true;
        }
        if (!exceptionCaught) {
            fail("Getting the internal addon by identifier 'internal' should throw an exception but it did not!");
        }
        assertNull(game.getAddonManager().getAddon("test"));
        assertNotEquals(game.getAddonManager().getAddon("test"), ((CommonAddonManager) game.getAddonManager()).getInternalAddon());
        assertEquals(((CommonAddonManager) game.getAddonManager()).getInternalAddon(), ((CommonAddonManager) game.getAddonManager()).getInternalAddon());
    }
}