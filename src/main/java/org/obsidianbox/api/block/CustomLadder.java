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
package org.obsidianbox.api.block;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.BlockLadder;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.obsidianbox.api.Game;
import org.obsidianbox.api.addon.Addon;
import org.obsidianbox.api.lang.Languages;

import java.util.List;

public class CustomLadder extends BlockLadder {
    private final Addon addon;
    private final String identifier;

    public CustomLadder(Addon addon, String identifier, String displayName, boolean showInCreativeTab) {
        this.addon = addon;
        this.identifier = identifier;

        setBlockName(addon.getDescription() + ".title.block" + identifier);
        setBlockTextureName(Game.MOD_ID.toLowerCase() + ":" + addon.getDescription().getIdentifier() + "/" + identifier);
        addon.getGame().getLanguages().put(addon, Languages.ENGLISH_AMERICAN, "tile.block." + identifier + ".name", displayName);
        if (showInCreativeTab) {
            setCreativeTab(addon.getGame().getTabs());
        }
        GameRegistry.registerBlock(this, addon.getDescription().getIdentifier() + "_" + identifier);
    }

    @Override
    public final String getLocalizedName() { return I18n.format(getUnlocalizedName() + ".name"); }

    @Override
    public final String getUnlocalizedName() { return addon.getDescription().getIdentifier() + ".tile.block." + identifier; }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list) { list.add(new ItemStack(item, 1, 0)); }


}
