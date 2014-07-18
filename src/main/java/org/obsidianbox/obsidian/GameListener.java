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
package org.obsidianbox.obsidian;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.opengl.*;

import org.obsidianbox.magma.Game;
import org.obsidianbox.magma.util.RenderUtil;
import org.obsidianbox.obsidian.gui.builtin.ObsidianMainMenu;
import org.obsidianbox.obsidian.message.builtin.AddonListMessage;

public class GameListener {
    private final Game game;
    private final ResourceLocation alphaResource;

    public GameListener(Game game) {
        this.game = game;
        alphaResource = new ResourceLocation(Game.MOD_ID.toLowerCase(), "textures/gui/watermark.png");
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onClientRenderOverlay(RenderGameOverlayEvent event) {
        if (event.type == RenderGameOverlayEvent.ElementType.ALL) {
            game.getGuiRenderer().render();
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onClientRenderWorldTick(TickEvent.RenderTickEvent event) {
        // End of tick and in-game
        if (event.phase == TickEvent.Phase.END && RenderUtil.MINECRAFT.currentScreen == null) {
            final ScaledResolution scaledResolution = new ScaledResolution(RenderUtil.MINECRAFT, RenderUtil.MINECRAFT.displayWidth, RenderUtil.MINECRAFT.displayHeight);
            final int scaledWidth = scaledResolution.getScaledWidth();
            final int scaledHeight = scaledResolution.getScaledHeight();

            // Draw Game logo
            GL11.glPushMatrix();
            RenderUtil.MINECRAFT.getTextureManager().bindTexture(alphaResource);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glTranslatef(scaledWidth - 47, scaledHeight - 45, 0);
            GL11.glScalef(0.50f, 0.50f, 1.0f);
            GL11.glTranslatef(-scaledWidth + 47, -scaledHeight + 45, 0);
            GL11.glEnable(GL11.GL_BLEND);
            RenderUtil.create2DRectangleModal(scaledWidth - 47, scaledHeight - 45, 100, 95, 0);
            RenderUtil.TESSELLATOR.draw();
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();

            // Draw milestone string
            GL11.glPushMatrix();
            GL11.glTranslatef(scaledWidth - 14, scaledHeight - 8, 0.0f);
            GL11.glScalef(0.50f, 0.50f, 1.0f);
            GL11.glTranslatef(-scaledWidth + 14, -scaledHeight + 8, 0.0f);
            RenderUtil.MINECRAFT.fontRenderer.drawString("Alpha", scaledWidth - 14, scaledHeight - 3, Integer.parseInt("7767AE", 16));
            GL11.glPopMatrix();
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onGuiOpen(GuiOpenEvent event) {
        // TODO Check config file, see if they want to use our menu
        if (event.gui instanceof GuiMainMenu) {
            event.gui = new ObsidianMainMenu();
        }
    }

    @SubscribeEvent
    @SideOnly(Side.SERVER)
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        game.getPipeline().sendTo(new AddonListMessage(), event.player);
    }
}
