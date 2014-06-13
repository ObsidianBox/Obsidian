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

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import org.obsidianbox.api.Game;
import org.obsidianbox.api.Materials;
import org.obsidianbox.api.addon.Addon;
import org.obsidianbox.api.lang.Languages;

public class CustomSlab extends BlockSlab {
    private final Addon addon;
    private final String identifier;
    private CustomSlab singleSlab, doubleSlab;
    private IIcon bottomIcon, sideIcon, topIcon;

    public CustomSlab(Addon addon, String identifier, String displayName, boolean showInCreativeTab) {
        this(addon, identifier, displayName, showInCreativeTab, false);
    }

    private CustomSlab(Addon addon, String identifier, String displayName, boolean showInCreativeTab, boolean isDoubleSlab) {
        super(isDoubleSlab, Materials.CUSTOM_SLAB);
        this.addon = addon;
        this.identifier = identifier;

        setBlockName(addon.getDescription().getIdentifier() + ".tile.block." + identifier);
        setBlockTextureName(Game.MOD_ID.toLowerCase() + ":" + addon.getDescription().getIdentifier() + "/" + identifier);
        addon.getGame().getLanguages().put(addon, Languages.ENGLISH_AMERICAN, "tile.block." + identifier + ".name", displayName);

        if (!isDoubleSlab) {
            if (showInCreativeTab) {
                setCreativeTab(addon.getGame().getTabs());
            }

            singleSlab = this;
            doubleSlab = new CustomSlab(addon, identifier, displayName, showInCreativeTab, true);
            register();
        }
    }

    private void register() {
        // Register our slabs, doubleSlab will be registered with '_double' appended at the end.
        //GameRegistry.registerBlock(singleSlab, CustomSlabItem.class, addon.getDescription().getIdentifier() + "_" +  identifier);
        //GameRegistry.registerBlock(doubleSlab, CustomSlabItem.class, addon.getDescription().getIdentifier() + "_" +  identifier + "_double");
    }

    @Override
    public final String getLocalizedName() {
        return I18n.format(getUnlocalizedName() + ".name");
    }

    @Override
    public final String getUnlocalizedName() {
        return addon.getDescription().getIdentifier() + ".tile.block." + identifier;
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(item, 1, 0));
    }

    @Override
    public IIcon getIcon(int side, int type) {
        switch (side) {
            case 0:
                return bottomIcon;
            case 1:
                return topIcon;
            default:
                return sideIcon;
        }
    }

    @SideOnly (Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister icon) {
        bottomIcon = icon.registerIcon(getTextureName() + "_bottom");
        sideIcon = icon.registerIcon(getTextureName() + "_side");
        topIcon = icon.registerIcon(getTextureName() + "_top");
    }

    @SideOnly (Side.CLIENT)
    @Override
    public Item getItem(World world, int x, int y, int z) {
        return Item.getItemFromBlock(this);
    }

    @Override
    public String func_150002_b(int var1) {
        return getUnlocalizedName();
    }

    public CustomSlab getDoubleBlock() {
        return doubleSlab;
    }

    public final Addon getAddon() {
        return addon;
    }

    public final String getIdentifier() {
        return identifier;
    }

    private class CustomSlabItem extends ItemSlab {
        public CustomSlabItem(Block block) {
            super(block, singleSlab, doubleSlab, false);
            this.setMaxDamage(0);
            this.setHasSubtypes(false);
        }
    }
}
