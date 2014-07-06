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
package org.obsidianbox.obsidian.addon.client;

import java.nio.file.Path;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourcePack;
import org.obsidianbox.magma.addon.InvalidAddonException;
import org.obsidianbox.magma.addon.InvalidDescriptionException;
import org.obsidianbox.obsidian.addon.CommonAddonManager;
import org.obsidianbox.obsidian.resource.AddonResourcePack;
import org.obsidianbox.magma.Game;
import org.obsidianbox.magma.addon.Addon;
import org.obsidianbox.obsidian.util.ReflectionEntry;

//TODO Override parent methods and show GUI acceptance screens
@SideOnly(Side.CLIENT)
public final class ClientAddonManager extends CommonAddonManager {
    public ClientAddonManager(Game game) {
        super(game);
    }

    @Override
    public Addon loadAddon(Path path) throws InvalidAddonException, InvalidDescriptionException {
        final Addon addon =  super.loadAddon(path);
        final AddonResourcePack pack = new AddonResourcePack(addon);
        injectAddonPack(pack);
        game.getLogger().info("Injected ResourcePack for addon [" + addon.getDescription().getIdentifier() + "]");
        return addon;
    }

    public void injectAddonPack(AddonResourcePack pack) {
        final List<IResourcePack> defaultPacks = ReflectionEntry.Fields.MINECRAFT_DEFAULT_RESOURCE_PACKS.get(Minecraft.getMinecraft());
        defaultPacks.add(pack);
    }
}