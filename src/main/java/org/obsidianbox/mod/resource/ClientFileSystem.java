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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import org.obsidianbox.magma.addon.Addon;
import org.obsidianbox.magma.resource.UnreachableURLException;

public class ClientFileSystem extends CommonFileSystem {
    public static final Path ASSETS_PATH = Paths.get(BASE_PATH.toString(), "assets");
    public static final Path TEXTURES_PATH = Paths.get(ASSETS_PATH.toString(), "textures");
    public static final Path BLOCK_TEXTURES_PATH = Paths.get(TEXTURES_PATH.toString(), "blocks");
    public static final Path ITEM_TEXTURES_PATH = Paths.get(TEXTURES_PATH.toString(), "items");

    @Override
    public void cache(Addon addon, Path path) throws FileNotFoundException {
        throw new UnsupportedOperationException("The client is not allowed to cache files!");
    }

    @Override
    public void cache(Addon addon, URL url) throws UnreachableURLException {
        throw new UnsupportedOperationException("The client is not allowed to cache urls!");
    }

    @Override
    public Set<Path> getPathCache(Addon addon) {
        throw new UnsupportedOperationException("The client does not have a path cache!");
    }

    @Override
    public Set<URL> getURLCache(Addon addon) {
        throw new UnsupportedOperationException("The client does not have a url cache!");
    }

    @Override
    public void setupFileSystem() throws IOException {
        super.setupFileSystem();

        if (!Files.exists(BLOCK_TEXTURES_PATH)) {
            Files.createDirectories(BLOCK_TEXTURES_PATH);
        }
        if (!Files.exists(ITEM_TEXTURES_PATH)) {
            Files.createDirectories(ITEM_TEXTURES_PATH);
        }
    }
}
