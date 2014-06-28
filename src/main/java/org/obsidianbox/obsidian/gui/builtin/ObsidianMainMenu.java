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
package org.obsidianbox.obsidian.gui.builtin;

import cpw.mods.fml.client.GuiModList;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.obsidianbox.magma.Game;
import org.obsidianbox.magma.util.RenderUtil;

public class ObsidianMainMenu extends GuiScreen {
    private static ResourceLocation gameLogo = new ResourceLocation(Game.MOD_ID.toLowerCase(), "textures/gui/" + Game.MOD_ID.toLowerCase() + ".png");
    private static final ObsidianBackground background = new ObsidianBackground();

    @Override
    public void initGui() {
        addButtons();
    }

    private void addButtons() {
        final String singleplayerText = I18n.format("menu.singleplayer");
        final String multiplayerText = I18n.format("menu.multiplayer");
        final String optionsText = I18n.format("menu.options");
        final String modsText = "Mods";
        final String addonsText = "Addons";
        final String quitText = I18n.format("menu.quit");

        final int normalFontColor = Integer.parseInt("E0E0E0", 16);
        final int hoverFontColor = Integer.parseInt("7767AE", 16);
        final int disabledFontColor = Integer.parseInt("5F5F60", 16);

        final ObsidianSimpleButton singlePlayerButton = new ObsidianSimpleButton(1, getSidebarCenterX(singleplayerText), 75, singleplayerText, normalFontColor, hoverFontColor, disabledFontColor);
        final ObsidianSimpleButton multiplayerButton = new ObsidianSimpleButton(2, getSidebarCenterX(multiplayerText), 92, multiplayerText, normalFontColor, hoverFontColor, disabledFontColor);
        final ObsidianSimpleButton optionsButton = new ObsidianSimpleButton(3, getSidebarCenterX(optionsText), 110, optionsText, normalFontColor, hoverFontColor, disabledFontColor);
        final ObsidianSimpleButton modsButton = new ObsidianSimpleButton(4, getSidebarCenterX(modsText), 128, modsText, normalFontColor, hoverFontColor, disabledFontColor);
        final ObsidianSimpleButton addonsButton = new ObsidianSimpleButton(5, getSidebarCenterX(addonsText), 145, addonsText, normalFontColor, hoverFontColor, disabledFontColor);
        final ObsidianSimpleButton quitButton = new ObsidianSimpleButton(6, getSidebarCenterX(quitText), height - 15, quitText, normalFontColor, Integer.parseInt("FF0000", 16), disabledFontColor);

        addonsButton.enabled = false;

        buttonList.add(singlePlayerButton);
        buttonList.add(multiplayerButton);
        buttonList.add(optionsButton);
        buttonList.add(modsButton);
        buttonList.add(addonsButton);
        buttonList.add(quitButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 1: // Singleplayer
                RenderUtil.MINECRAFT.displayGuiScreen(new GuiSelectWorld(this));
                break;
            case 2: // Multiplayer
                RenderUtil.MINECRAFT.displayGuiScreen(new GuiMultiplayer(this));
                break;
            case 3: // Options
                RenderUtil.MINECRAFT.displayGuiScreen(new GuiOptions(this, RenderUtil.MINECRAFT.gameSettings));
                break;
            case 4: // Mods
                RenderUtil.MINECRAFT.displayGuiScreen(new GuiModList(this));
                break;
            case 5: // Addons
                break;
            case 6: // Quit
                RenderUtil.MINECRAFT.shutdown();
        }
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        // Draw the background with overlay
        background.drawBackground(0, 0, width, height);

        // Draw sidebar gradients - draw two at full width and one at 1 pixel wide to get desired look
        drawGradientRect(width - 85, 0, width, height, Integer.MIN_VALUE, Integer.MIN_VALUE);
        drawGradientRect(width - 85, 0, width, height, Integer.MIN_VALUE, Integer.MIN_VALUE);
        drawGradientRect(width - 85, 0, width - 84, height, Integer.MIN_VALUE, Integer.MIN_VALUE);

        // Draw the Game logo
        GL11.glPushMatrix();
        mc.getTextureManager().bindTexture(gameLogo);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glTranslatef(width - 81, 0, 0);
        GL11.glScalef(0.50f, 0.50f, 1.0f);
        GL11.glTranslatef(-width + 81, 0, 0);
        GL11.glEnable(GL11.GL_BLEND);
        RenderUtil.create2DRectangleModal(width - 81, 0, 155, 151, 0);
        RenderUtil.TESSELLATOR.draw();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();

        // Draw the Copyright string
        GL11.glPushMatrix();
        GL11.glTranslatef(2f, height - 8f, 0.0f);
        GL11.glScalef(0.85f, 0.85f, 1.0f);
        GL11.glTranslatef(-2f, -(height - 8f), 0.0f);
        drawString(RenderUtil.MINECRAFT.fontRenderer, "Copyright Mojang AB. Do not distribute!", 2, height - 8, Integer.parseInt("FFFF00", 16));
        GL11.glPopMatrix();

        super.drawScreen(par1, par2, par3);
    }

    @Override
    protected void keyTyped(char par1, int par2) {
    }

    private int getSidebarCenterX(String text) {
        return ((width - (85 / 2)) - RenderUtil.MINECRAFT.fontRenderer.getStringWidth(text)) + (RenderUtil.MINECRAFT.fontRenderer.getStringWidth(text) / 2);
    }
}
