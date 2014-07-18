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
package org.obsidianbox.obsidian;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import org.obsidianbox.magma.Game;
import org.obsidianbox.magma.lang.Languages;
import org.obsidianbox.obsidian.addon.CommonAddonManager;
import org.obsidianbox.obsidian.addon.InternalAddon;

public class CustomTabs extends CreativeTabs {
    private final InternalAddon addon;

    protected CustomTabs(Game game) {
        super(Game.MOD_ID.toLowerCase());
        this.addon = (InternalAddon) ((CommonAddonManager) game.getAddonManager()).getInternalAddon();
        game.getLanguages().put(addon, Languages.ENGLISH_AMERICAN, "itemGroup." + Game.MOD_ID.toLowerCase(), Game.MOD_ID);
    }

    @Override
    public String getTranslatedTabLabel() {
        return addon.getDescription().getIdentifier() + ".itemGroup." + Game.MOD_ID.toLowerCase();
    }

    @Override
    public Item getTabIconItem() {
        return addon.getTabIcon();
    }
}
