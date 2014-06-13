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
package org.obsidianbox.mod.gui.builtin;

import java.util.Calendar;
import java.util.Random;

import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.*;
import org.obsidianbox.api.Game;
import org.obsidianbox.api.util.RenderUtil;

public class ObsidianBackground extends Gui {
    private static final Random RANDOM = new Random();
    private final ResourceLocation backgroundLocation = getResourceByTimeOfDay();

    private ResourceLocation getResourceByTimeOfDay() {
        final int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        final String symbolicName;

        if (hour < 12) {
            symbolicName = "day";
        } else if (hour < 20) {
            symbolicName = "evening";
        } else {
            symbolicName = "night";
        }
        return new ResourceLocation(Game.MOD_ID.toLowerCase(), "textures/gui/internal/background/" + symbolicName + "/background_" + (RANDOM.nextInt(5 - 1) + 1) + ".jpg");
    }

    public void drawBackground(int x, int y, int width, int height) {
        RenderUtil.MINECRAFT.getTextureManager().bindTexture(backgroundLocation);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        RenderUtil.create2DRectangleModal(x, y, width, height, 0);
        RenderUtil.TESSELLATOR.draw();
    }
}
