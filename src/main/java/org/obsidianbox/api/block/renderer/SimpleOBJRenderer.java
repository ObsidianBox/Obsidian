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
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.*;
import org.obsidianbox.api.Game;
import org.obsidianbox.api.addon.Addon;

public class SimpleOBJRenderer extends BlockRenderer {
    protected final Map<Block, ResourceLocation> textures = new HashMap<>();

    /**
     * TODO: Support this
     */
    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if (registered.contains(block)) {
            GL11.glPushMatrix();
            GL11.glTranslated(x, y, z);
            Minecraft.getMinecraft().renderEngine.bindTexture(textures.get(block));
            models.get(block).renderAll();
            GL11.glPopMatrix();
        }
        // TODO Investigate meaning of return value
        return true;
    }

    /**
     * TODO: Support this
     */
    @Override
    public final boolean shouldRender3DInInventory(int modelId) {
        return false;
    }

    @Override
    public final void put(Addon addon, String identifier, Block block) {
        super.put(addon, identifier, block);
        textures.put(block, new ResourceLocation(addon.getDescription().getIdentifier() + ":textures/blocks/" + identifier + ".png"));
    }
}
