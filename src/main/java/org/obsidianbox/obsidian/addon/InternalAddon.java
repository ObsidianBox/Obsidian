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

import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.obsidianbox.magma.Game;
import org.obsidianbox.magma.addon.Addon;
import org.obsidianbox.magma.addon.AddonDescription;
import org.obsidianbox.magma.addon.AddonMode;
import org.obsidianbox.magma.block.CustomBlock;
import org.obsidianbox.magma.block.CustomMovingBlock;
import org.obsidianbox.magma.block.CustomSlab;
import org.obsidianbox.magma.block.RenderingType;
import org.obsidianbox.magma.message.MessagePipeline;
import org.obsidianbox.magma.item.CustomArmor;
import org.obsidianbox.magma.item.CustomAxe;
import org.obsidianbox.magma.item.CustomFood;
import org.obsidianbox.magma.item.CustomItem;
import org.obsidianbox.magma.item.CustomPickaxe;
import org.obsidianbox.magma.item.CustomSpade;
import org.obsidianbox.magma.item.CustomSword;
import org.obsidianbox.obsidian.message.builtin.AddFileMessage;
import org.obsidianbox.obsidian.message.builtin.AddonListMessage;
import org.obsidianbox.obsidian.message.builtin.DownloadLinkMessage;

public final class InternalAddon extends Addon {
    private CustomItem obsidianEmblem;

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
        pipeline.register(this, AddFileMessage.class);
        pipeline.register(this, AddonListMessage.class);
        pipeline.register(this, DownloadLinkMessage.class);

        /* Remove before release */
        new CustomMovingBlock(this, "0b", "0b (Black)", true);
        new CustomMovingBlock(this, "0w", "0w (White)", true);
        new CustomMovingBlock(this, "1b", "1b (Black)", true);
        new CustomMovingBlock(this, "1w", "1w (White)", true);
        new CustomMovingBlock(this, "2b", "2b (Black)", true);
        new CustomMovingBlock(this, "2w", "2w (White)", true);
        new CustomMovingBlock(this, "3b", "3b (Black)", true);
        new CustomMovingBlock(this, "3w", "3w (White)", true);
        new CustomMovingBlock(this, "4b", "4b (Black)", true);
        new CustomMovingBlock(this, "4w", "4w (White)", true);
        new CustomMovingBlock(this, "5b", "5b (Black)", true);
        new CustomMovingBlock(this, "5w", "5w (White)", true);
        new CustomMovingBlock(this, "6b", "6b (Black)", true);
        new CustomMovingBlock(this, "6w", "6w (White)", true);
        new CustomMovingBlock(this, "7b", "7b (Black)", true);
        new CustomMovingBlock(this, "7w", "7w (White)", true);
        new CustomMovingBlock(this, "8b", "8b (Black)", true);
        new CustomMovingBlock(this, "8w", "8w (White)", true);
        new CustomMovingBlock(this, "9b", "9b (Black)", true);
        new CustomMovingBlock(this, "9w", "9w (White)", true);

        new CustomSword(this, "custom_blade", "Custom Blade", Item.ToolMaterial.EMERALD, true);
        new CustomAxe(this, "custom_axe", "Custom Axe", Item.ToolMaterial.EMERALD, true);
        new CustomPickaxe(this, "custom_pickaxe", "Custom Pickaxe", Item.ToolMaterial.EMERALD, true);
        new CustomSpade(this, "custom_shovel", "Custom Shovel", Item.ToolMaterial.EMERALD, true);

        final ItemArmor.ArmorMaterial customArmorMaterial = EnumHelper.addArmorMaterial("Custom", 100, new int[] {2, 3, 2, 2}, 15);
        new CustomArmor(this, "custom_helmet", "Custom Helmet", customArmorMaterial, CustomArmor.ArmorType.HEAD, true);
        new CustomArmor(this, "custom_chestplate", "Custom Chestplate", customArmorMaterial, CustomArmor.ArmorType.TORSO, true);
        new CustomArmor(this, "custom_leggings", "Custom Leggings", customArmorMaterial, CustomArmor.ArmorType.LEGS, true);
        new CustomArmor(this, "custom_boots", "Custom Boots", customArmorMaterial, CustomArmor.ArmorType.FEET, true);

        new CustomSlab(this, "custom_slab","Custom Slab", true);

        new CustomBlock(this, "custom_renderer", "Custom Renderer", true, RenderingType.OBJ);

        obsidianEmblem = new CustomItem(this, "obsidian_emblem", "Obsidian Emblem", true);
    }

    public CustomItem getTabIcon() {
        return obsidianEmblem;
    }
}
