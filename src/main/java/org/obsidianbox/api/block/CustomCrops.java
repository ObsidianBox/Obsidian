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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.flowpowered.math.vector.Vector3f;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.BlockCrops;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.obsidianbox.api.Game;
import org.obsidianbox.api.addon.Addon;
import org.obsidianbox.api.lang.Languages;

public class CustomCrops extends BlockCrops {
    private final Addon addon;
    private final String identifier;
    private final Set<Integer> metaWithTexture = new HashSet<>();
    protected final Map<Integer, IIcon> metaIcons = new HashMap<>();
    private Item associatedItem;

    public CustomCrops(Addon addon, String identifier, String displayName, boolean tickRandomly, Vector3f minBounds, Vector3f maxBounds) {
        this.addon = addon;
        this.identifier = identifier;
        setBlockName(addon.getDescription().getIdentifier() + ".tile.block." + identifier);
        setBlockTextureName(addon.getDescription().getIdentifier()  + ":" + "crops/" + identifier);
        addon.getGame().getLanguages().put(addon, Languages.ENGLISH_AMERICAN, "tile.block." + identifier + ".name", displayName);
        setTickRandomly(tickRandomly);
        setBlockBounds(minBounds.getX(), minBounds.getY(), minBounds.getZ(), maxBounds.getX(), maxBounds.getY(), maxBounds.getZ());
        setHasTextureForMeta(0);
        GameRegistry.registerBlock(this, addon.getDescription().getIdentifier() + "_" + identifier);
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
    public void registerBlockIcons(IIconRegister register) {
        for (int meta : metaWithTexture) {
            if (meta == 0) {
                metaIcons.put(meta, register.registerIcon(getTextureName()));
                continue;
            }
            metaIcons.put(meta, register.registerIcon(getTextureName() + "_stage_" + meta));
        }
    }

    protected void setHasTextureForMeta(Integer... meta) {
        for (int m : meta) {
            if (m > 15) {
                continue;
            }
            metaWithTexture.add(m);
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
        if (!(o instanceof CustomCrops)) {
            return false;
        }

        final CustomCrops that = (CustomCrops) o;

        return addon.equals(that.addon) && identifier.equals(that.identifier);
    }

    @Override
    public int hashCode() {
        int result = addon.hashCode();
        result = 31 * result + identifier.hashCode();
        return result;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }

    /**
     * Render type 6 = Vanilla crops (render as wheat)
     */
    @Override
    public int getRenderType() {
        return 6;
    }

    @Override
    public Item getItem(World world, int x, int y, int z) {
        return associatedItem;
    }

    @Override
    public IIcon getIcon(IBlockAccess access, int x, int y, int z, int meta) {
        return metaIcons.get(meta);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    public void setAssociatedItem(Item item) {
        associatedItem = item;
    }
}
