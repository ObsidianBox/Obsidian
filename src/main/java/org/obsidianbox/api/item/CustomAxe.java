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
package org.obsidianbox.api.item;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import org.obsidianbox.api.Game;
import org.obsidianbox.api.addon.Addon;
import org.obsidianbox.api.lang.Languages;

public class CustomAxe extends ItemAxe {
    private final Addon addon;
    private final String identifier;

    public CustomAxe(Addon addon, String identifier, String displayName, ToolMaterial toolMaterial, boolean showInCreativeTab) {
        super(toolMaterial);
        this.addon = addon;
        this.identifier = identifier;

        setTextureName(Game.MOD_ID.toLowerCase() + ":" + addon.getDescription().getIdentifier() + "/axes/" + identifier);
        addon.getGame().getLanguages().put(addon, Languages.ENGLISH_AMERICAN, "item." + identifier + ".name", displayName);
        if (showInCreativeTab) {
            setCreativeTab(addon.getGame().getTabs());
        }
        GameRegistry.registerItem(this, addon.getDescription().getIdentifier() + "_" + identifier);
    }

    @Override
    public String getUnlocalizedName() {
        return addon.getDescription().getIdentifier() + ".item." + identifier;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return I18n.format(getUnlocalizedName() + ".name");
    }

    public final Addon getAddon() {
        return addon;
    }

    public final String getIdentifier() {
        return identifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomAxe)) {
            return false;
        }

        final CustomAxe that = (CustomAxe) o;

        return addon.equals(that.addon) && identifier.equals(that.identifier);
    }

    @Override
    public int hashCode() {
        int result = addon.hashCode();
        result = 31 * result + identifier.hashCode();
        return result;
    }
}
