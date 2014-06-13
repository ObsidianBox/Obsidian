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
package org.obsidianbox.api.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.imageio.ImageIO;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.BufferUtils;

public class RenderUtil {
    public static final Tessellator TESSELLATOR = Tessellator.instance;
    public static final Minecraft MINECRAFT = FMLClientHandler.instance().getClient();

    public static void create2DRectangleModal(double x, double y, double width, double height, double zLevel) {
        TESSELLATOR.startDrawingQuads();
        TESSELLATOR.addVertexWithUV(x + 0, y + height, zLevel, 0, 1);
        TESSELLATOR.addVertexWithUV(x + width, y + height, zLevel, 1, 1);
        TESSELLATOR.addVertexWithUV(x + width, y + 0, zLevel, 1, 0);
        TESSELLATOR.addVertexWithUV(x + 0, y + 0, zLevel, 0, 0);
    }

    public static ByteBuffer createImageBufferFrom(ResourceLocation location, boolean alpha) {
        ByteBuffer buffer = null;
        if (!Files.isDirectory(Paths.get(location.getResourcePath()))) {
            try {
                final BufferedImage image = ImageIO.read(MINECRAFT.getResourceManager().getResource(location).getInputStream());
                final int[] pixels = new int[image.getWidth() * image.getHeight()];
                image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
                buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * ((alpha) ? 4 : 3));

                for (int y = 0; y < image.getHeight(); y++) {
                    for (int x = 0; x < image.getWidth(); x++) {
                        int pixel = pixels[y * image.getWidth() + x];
                        buffer.put((byte) ((pixel >> 16) & 0xFF));
                        buffer.put((byte) ((pixel >> 8) & 0xFF));
                        buffer.put((byte) (pixel & 0xFF));
                        buffer.put((byte) ((pixel >> 24) & 0xFF));
                    }
                }

                buffer.flip();
            } catch (IOException ignore) {
            }
        }
        return buffer;
    }
}