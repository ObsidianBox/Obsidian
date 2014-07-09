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
package org.obsidianbox.obsidian.addon;

import java.lang.reflect.Field;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.obsidianbox.magma.Game;
import org.obsidianbox.magma.addon.Addon;
import org.obsidianbox.magma.addon.AddonDescription;
import org.obsidianbox.magma.addon.AddonMode;
import org.obsidianbox.magma.block.SimpleBlock;
import org.obsidianbox.magma.block.SimpleFlower;
import org.obsidianbox.magma.block.SimpleMovingBlock;
import org.obsidianbox.magma.block.SimplePressurePlate;
import org.obsidianbox.magma.block.SimpleSlab;
import org.obsidianbox.magma.block.SimpleTrapDoor;
import org.obsidianbox.magma.block.fluid.SimpleBlockFluid;
import org.obsidianbox.magma.block.renderer.SimpleBlockOBJRenderer;
import org.obsidianbox.magma.item.SimpleArmor;
import org.obsidianbox.magma.item.SimpleAxe;
import org.obsidianbox.magma.item.SimpleBow;
import org.obsidianbox.magma.item.SimpleItem;
import org.obsidianbox.magma.item.SimplePickaxe;
import org.obsidianbox.magma.item.SimpleSpade;
import org.obsidianbox.magma.item.SimpleSword;
import org.obsidianbox.magma.message.MessagePipeline;
import org.obsidianbox.obsidian.message.builtin.AddFileMessage;
import org.obsidianbox.obsidian.message.builtin.AddonListMessage;
import org.obsidianbox.obsidian.message.builtin.DownloadLinkMessage;

public final class InternalAddon extends Addon {
    private SimpleItem obsidianEmblem;

    public InternalAddon(Game game) {
        final Field gameField;
        try {
            gameField = Addon.class.getDeclaredField("game");
            gameField.setAccessible(true);
            gameField.set(this, game);
            gameField.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        final AddonDescription description = new AddonDescription("internal", Game.MOD_ID, "1.0-SNAPSHOT", AddonMode.BOTH, "org.obsidianbox.obsidian.addon.InternalAddon");

        final Field descriptionField;
        try {
            descriptionField = Addon.class.getDeclaredField("description");
            descriptionField.setAccessible(true);
            descriptionField.set(this, description);
            descriptionField.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        final Logger logger = LogManager.getLogger("Internal");

        final Field loggerField;
        try {
            loggerField = Addon.class.getDeclaredField("logger");
            loggerField.setAccessible(true);
            loggerField.set(this, logger);
            loggerField.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInitialize() {
        final MessagePipeline pipeline = getGame().getPipeline();
        pipeline.register(this, AddFileMessage.class, null);
        pipeline.register(this, AddonListMessage.class, AddonListMessage.class);
        pipeline.register(this, DownloadLinkMessage.class, null);

        obsidianEmblem = new SimpleItem(this, "obsidian_emblem", "Obsidian Emblem", true);

        // TODO Remove before release, simply for testing Magma
        testAlphaContent();
    }

    public SimpleItem getTabIcon() {
        return obsidianEmblem;
    }

    private void testAlphaContent() {
        final Material customMovingMaterial = new Material(MapColor.brownColor);
        new SimpleMovingBlock(this, "0b", "0b (Black)", customMovingMaterial, true);
        new SimpleMovingBlock(this, "0w", "0w (White)", customMovingMaterial, true);
        new SimpleMovingBlock(this, "1b", "1b (Black)", customMovingMaterial, true);
        new SimpleMovingBlock(this, "1w", "1w (White)", customMovingMaterial, true);
        new SimpleMovingBlock(this, "2b", "2b (Black)", customMovingMaterial, true);
        new SimpleMovingBlock(this, "2w", "2w (White)", customMovingMaterial, true);
        new SimpleMovingBlock(this, "3b", "3b (Black)", customMovingMaterial, true);
        new SimpleMovingBlock(this, "3w", "3w (White)", customMovingMaterial, true);
        new SimpleMovingBlock(this, "4b", "4b (Black)", customMovingMaterial, true);
        new SimpleMovingBlock(this, "4w", "4w (White)", customMovingMaterial, true);
        new SimpleMovingBlock(this, "5b", "5b (Black)", customMovingMaterial, true);
        new SimpleMovingBlock(this, "5w", "5w (White)", customMovingMaterial, true);
        new SimpleMovingBlock(this, "6b", "6b (Black)", customMovingMaterial, true);
        new SimpleMovingBlock(this, "6w", "6w (White)", customMovingMaterial, true);
        new SimpleMovingBlock(this, "7b", "7b (Black)", customMovingMaterial, true);
        new SimpleMovingBlock(this, "7w", "7w (White)", customMovingMaterial, true);
        new SimpleMovingBlock(this, "8b", "8b (Black)", customMovingMaterial, true);
        new SimpleMovingBlock(this, "8w", "8w (White)", customMovingMaterial, true);
        new SimpleMovingBlock(this, "9b", "9b (Black)", customMovingMaterial, true);
        new SimpleMovingBlock(this, "9w", "9w (White)", customMovingMaterial, true);

        new SimpleSword(this, "custom_blade", "Custom Blade", Item.ToolMaterial.EMERALD, true);
        new SimpleAxe(this, "custom_axe", "Custom Axe", Item.ToolMaterial.EMERALD, true);
        new SimplePickaxe(this, "custom_pickaxe", "Custom Pickaxe", Item.ToolMaterial.EMERALD, true);
        new SimpleSpade(this, "custom_shovel", "Custom Shovel", Item.ToolMaterial.EMERALD, true);

        final ItemArmor.ArmorMaterial customArmorMaterial = EnumHelper.addArmorMaterial("Custom", 100, new int[] {2, 3, 2, 2}, 15);
        new SimpleArmor(this, "custom_helmet", "Custom Helmet", customArmorMaterial, SimpleArmor.ArmorType.HEAD, true);
        new SimpleArmor(this, "custom_chestplate", "Custom Chestplate", customArmorMaterial, SimpleArmor.ArmorType.TORSO, true);
        new SimpleArmor(this, "custom_leggings", "Custom Leggings", customArmorMaterial, SimpleArmor.ArmorType.LEGS, true);
        new SimpleArmor(this, "custom_boots", "Custom Boots", customArmorMaterial, SimpleArmor.ArmorType.FEET, true);

        final Material customSlabMaterial = new Material(MapColor.blueColor);
        new SimpleSlab(this, "custom_slab", "Custom Slab", customSlabMaterial, true);

        final Material customBlockMaterial = new Material(MapColor.clayColor);
        final SimpleBlock block = new SimpleBlock(this, "custom_renderer", "Custom Renderer", customBlockMaterial, true);
        if (getGame().getSide().isClient()) {
            setupBlockRenderer(block);
        }

        final Material customFluidMaterial = new Material(MapColor.diamondColor);
        new SimpleBlockFluid(this, "custom_fluid", "Custom Fluid", customFluidMaterial, true);

        final Material customPressurePlate = new Material(MapColor.adobeColor);
        new SimplePressurePlate(this, "custom_plate", "Custom Plate", customPressurePlate, true, BlockPressurePlate.Sensitivity.everything);

        new SimpleFlower(this, "custom_flower", "Custom Flower", true);

        final Material customTrapDoor = new Material(MapColor.woodColor);
        new SimpleTrapDoor(this, "custom_trapdoor", "Custom Trapdoor", customTrapDoor, true);

        SimpleBow bow = new SimpleBow(this, "simple_bow", "Simple Bow", true, 1);
    }

    @SideOnly (Side.CLIENT)
    private void setupBlockRenderer(SimpleBlock block) {
        final SimpleBlockOBJRenderer renderer = new SimpleBlockOBJRenderer(this, RenderingRegistry.getNextAvailableRenderId(), block);
        RenderingRegistry.registerBlockHandler(renderer);
    }
}
