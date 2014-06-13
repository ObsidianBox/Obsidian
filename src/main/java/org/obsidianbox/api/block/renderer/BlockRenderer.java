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
package org.obsidianbox.api.block.renderer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.obsidianbox.api.Game;
import org.obsidianbox.api.addon.Addon;

public abstract class BlockRenderer implements ISimpleBlockRenderingHandler {
    protected final Set<Block> registered = new HashSet<>();
    protected final Map<Block, IModelCustom> models = new HashMap<>();
    // ID
    protected final int renderID;

    public BlockRenderer() {
        renderID = RenderingRegistry.getNextAvailableRenderId();
    }

    @Override
    public final int getRenderId() {
        return renderID;
    }

    /**
     * TODO: Add interface to tie all custom content together
     */
    public void put(Addon addon, String identifier, Block block) {
        registered.add(block);
        models.put(block, AdvancedModelLoader.loadModel(new ResourceLocation(Game.MOD_ID.toLowerCase() + ":models/blocks/" + addon.getDescription().getIdentifier() + "/" + identifier + ".obj")));
    }
}
