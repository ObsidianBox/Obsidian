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

import java.util.Random;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.client.renderer.IconFlipped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.obsidianbox.api.Game;
import org.obsidianbox.api.Materials;
import org.obsidianbox.api.addon.Addon;
import org.obsidianbox.api.lang.Languages;

public class CustomDoor extends BlockDoor {
    private final Addon addon;
    private final String identifier;
    private boolean isPoweredOnly;
    private CustomDoorItem associatedItem;
    private IIcon bottomFlippedIcon, bottomIcon, topFlippedIcon, topIcon;

    public CustomDoor(Addon addon, String identifier, String displayName, boolean isPoweredOnly, boolean showInCreativeTab) {
        super(Materials.CUSTOM_DOOR);
        this.addon = addon;
        this.identifier = identifier;
        this.associatedItem = new CustomDoorItem(addon, identifier, displayName, this, showInCreativeTab);
        this.isPoweredOnly = isPoweredOnly;

        setBlockName(addon.getDescription().getIdentifier() + ".tile.block." + identifier);
        setBlockTextureName(Game.MOD_ID.toLowerCase() + ":" + addon.getDescription().getIdentifier() + "/" + identifier);
        addon.getGame().getLanguages().put(addon, Languages.ENGLISH_AMERICAN, "tile.block." + identifier + ".name", displayName);
        GameRegistry.registerBlock(this, addon.getDescription().getIdentifier() + "_" + identifier + "_block");
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
    public IIcon getIcon(int side, int type) {
        return bottomIcon;
    }

    @Override
    public IIcon getIcon(IBlockAccess blockAccess, int x, int y, int z, int val1) {
        if (val1 != 1 && val1 != 0) {
            int i1 = this.func_150012_g(blockAccess, x, y, z);
            int i2 = i1 & 3;
            boolean flag = (i1 & 4) != 0;
            boolean flag1 = false;
            boolean flag2 = (i1 & 8) != 0;
            if (flag) {
                if (i2 == 0 && val1 == 2) {
                    flag1 = true;
                } else if (i2 == 1 && val1 == 5) {
                    flag1 = true;
                } else if (i2 == 2 && val1 == 3) {
                    flag1 = true;
                } else if (i2 == 3 && val1 == 4) {
                    flag1 = true;
                }
            } else {
                if (i2 == 0 && val1 == 5) {
                    flag1 = true;
                } else if (i2 == 1 && val1 == 3) {
                    flag1 = true;
                } else if (i2 == 2 && val1 == 4) {
                    flag1 = true;
                } else if (i2 == 3 && val1 == 2) {
                    flag1 = true;
                }
                if ((i1 & 16) != 0) {
                    flag1 = !flag1;
                }
            }
            return flag2 ? (flag1 ? topFlippedIcon : topIcon) : (flag1 ? bottomFlippedIcon : bottomIcon);
        } else {
            return topIcon;
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int val1, float val2, float val3, float val4) {
        return !isPoweredOnly && super.onBlockActivated(world, x, y, z, player, val1, val2, val3, val4);
    }

    @Override
    public Item getItemDropped(int val1, Random random, int val2) {
        return (val1 & 8) != 0 ? null : associatedItem;
    }

    @Override
    public void registerBlockIcons(IIconRegister icon) {
        bottomIcon = icon.registerIcon(getTextureName() + "_lower");
        topIcon = icon.registerIcon(getTextureName() + "_upper");
        bottomFlippedIcon = new IconFlipped(bottomIcon, true, false);
        topFlippedIcon = new IconFlipped(topIcon, true, false);
    }

    /**
     * Gets the item associated with this block
     *
     * @return The associated item
     */
    public Item getItem() {
        return associatedItem;
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
        if (!(o instanceof CustomDoor)) {
            return false;
        }

        final CustomDoor that = (CustomDoor) o;

        return addon.equals(that.addon) && identifier.equals(that.identifier);
    }

    @Override
    public int hashCode() {
        int result = addon.hashCode();
        result = 31 * result + identifier.hashCode();
        return result;
    }

    private class CustomDoorItem extends ItemDoor {
        private final Addon addon;
        private final String identifier;
        private Block associatedBlock;

        private CustomDoorItem(Addon addon, String identifier, String displayName, CustomDoor associatedBlock, boolean showInCreativeTab) {
            super(Materials.CUSTOM_DOOR);
            this.addon = addon;
            this.identifier = identifier;
            this.associatedBlock = associatedBlock;

            setTextureName(Game.MOD_ID.toLowerCase() + ":" + addon.getDescription().getIdentifier() + "/" + identifier);
            addon.getGame().getLanguages().put(addon, Languages.ENGLISH_AMERICAN, "item." + identifier + ".name", displayName);
            if (showInCreativeTab) {
                setCreativeTab(addon.getGame().getTabs());
            }
            GameRegistry.registerItem(this, addon.getDescription().getIdentifier() + "_" + identifier + "_item");
        }

        @Override
        public String getUnlocalizedName() {
            return addon.getDescription().getIdentifier() + ".item." + identifier;
        }

        @Override
        public String getItemStackDisplayName(ItemStack stack) {
            return I18n.format(getUnlocalizedName() + ".name");
        }

        @Override
        public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int val1, float val2, float val3, float val4) {
            if (val1 != 1) {
                return false;
            } else {
                ++y;
                if (player.canPlayerEdit(x, y, z, val1, stack) && player.canPlayerEdit(x, y + 1, z, val1, stack)) {
                    if (!associatedBlock.canPlaceBlockAt(world, x, y, z)) {
                        return false;
                    } else {
                        int i1 = MathHelper.floor_double((double) ((player.rotationYaw + 180.0F) * 4.0F / 360.0F) - 0.5D) & 3;
                        placeDoorBlock(world, x, y, z, i1, associatedBlock);
                        --stack.stackSize;
                        return true;
                    }
                } else {
                    return false;
                }
            }
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
            if (!(o instanceof CustomDoorItem)) {
                return false;
            }

            final CustomDoorItem that = (CustomDoorItem) o;

            return addon.equals(that.addon) && identifier.equals(that.identifier);
        }

        @Override
        public int hashCode() {
            int result = addon.hashCode();
            result = 31 * result + identifier.hashCode();
            return result;
        }
    }
}
