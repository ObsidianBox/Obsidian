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

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.*;

import org.obsidianbox.magma.util.RenderUtil;

public class ObsidianSimpleButton extends GuiButton {
    private int fontColor, normalFontColor, hoverFontColor, disabledFontColor, buttonWidth, buttonHeight;
    private FontRenderer fontRenderer = RenderUtil.MINECRAFT.fontRenderer;

    public ObsidianSimpleButton(int id, int x, int y, String text, int normalFontColor, int hoverFontColor, int disabledFontColor) {
        this(id, x, y, text);
        this.normalFontColor = normalFontColor;
        this.hoverFontColor = hoverFontColor;
        this.disabledFontColor = disabledFontColor;
    }

    public ObsidianSimpleButton(int id, int x, int y, String text) {
        super(id, x, y, text);
    }

    public ObsidianSimpleButton(int id, int x, int y, int width, int height, String text, int normalFontColor, int hoverFontColor, int disabledFontColor) {
        this(id, x, y, width, height, text);
        this.normalFontColor = normalFontColor;
        this.hoverFontColor = hoverFontColor;
        this.disabledFontColor = disabledFontColor;
    }

    public ObsidianSimpleButton(int id, int x, int y, int width, int height, String text) {
        super(id, x, y, width, height, text);
    }

    @Override
    public void drawButton(Minecraft mc, int x, int y) {
        if (this.visible) {
            fontColor = normalFontColor;
            buttonWidth = fontRenderer.getStringWidth(displayString);
            buttonHeight = fontRenderer.FONT_HEIGHT;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            field_146123_n = x >= xPosition && y >= yPosition && x < xPosition + buttonWidth && y < yPosition + buttonHeight;
            mouseDragged(mc, x, y);

            if (!enabled) {
                fontColor = disabledFontColor;
            } else if (field_146123_n) {
                fontColor = hoverFontColor;
            }

            drawCenteredString(fontRenderer, displayString, xPosition + buttonWidth / 2, yPosition + (buttonHeight - 8) / 2, fontColor);
        }
    }

    @Override
    public boolean mousePressed(Minecraft mc, int x, int y) {
        return enabled && func_146115_a() && x >= xPosition && y >= yPosition && x < xPosition + buttonWidth && y < yPosition + buttonHeight;
    }
}
