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
package org.obsidianbox.obsidian.resource;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;
import org.obsidianbox.magma.Game;
import org.obsidianbox.magma.addon.Addon;

public class AddonResourcePack implements IResourcePack {
    private final Addon addon;
    private final Set<String> domains = new HashSet<>();

    public AddonResourcePack(Addon addon) {
        this.addon = addon;
        domains.add(addon.getDescription().getIdentifier());
    }

    @Override
    public InputStream getInputStream(ResourceLocation resourceLocation) {
        return getResourceStream(resourceLocation);
    }

    private InputStream getResourceStream(ResourceLocation resourceLocation) {
        // TODO Check this
        return addon.getClass().getResourceAsStream(String.format("/assets/%s/%s", addon.getDescription().getIdentifier(), resourceLocation.getResourcePath()));
    }

    @Override
    public boolean resourceExists(ResourceLocation resourceLocation) {
        return getResourceStream(resourceLocation) != null;
    }

    @Override
    public Set getResourceDomains() {
        return domains;
    }

    @Override
    public IMetadataSection getPackMetadata(IMetadataSerializer var1, String var2) {
        return null;
    }

    @Override
    public BufferedImage getPackImage() {
        return null;
    }

    @Override
    public String getPackName() {
        return addon.getDescription().getIdentifier();
    }
}
