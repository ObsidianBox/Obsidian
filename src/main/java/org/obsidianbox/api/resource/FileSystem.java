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
package org.obsidianbox.api.resource;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Set;
import java.util.Map;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.obsidianbox.api.addon.Addon;

public interface FileSystem {
    /**
     * Caches a {@link java.nio.file.Path} to a file on the server's filesystem for sending to clients.
     *
     * This method is only available during the INITIALIZATION phase of an {@link org.obsidianbox.api.addon.Addon}.
     *
     * @param addon The addon caching the file
     * @param path The path to the file being cached
     * @throws FileNotFoundException If the path resolves to a file not found
     */
    @SideOnly (Side.SERVER)
    public void cache(Addon addon, Path path) throws FileNotFoundException;

    /**
     * Caches an {@link java.net.URL} for clients to download. The server only tracks the link, it is not instructed to download the file. The client does this instead.
     *
     * This method is only available during the INITIALIZATION phase of an {@link org.obsidianbox.api.addon.Addon}.
     *
     * @param addon The addon caching the URL
     * @param url The URL being cached
     * @throws UnreachableURLException If the server cannot contact the URL
     */
    @SideOnly (Side.SERVER)
    public void cache(Addon addon, URL url) throws UnreachableURLException;

    /**
     * Gets all cached {@link java.nio.file.Path}s for the {@link org.obsidianbox.api.addon.Addon}.
     *
     * The returned set will not be modifiable.
     *
     * @param addon Addon to lookup
     */
    @SideOnly (Side.SERVER)
    public Set<Path> getPathCache(Addon addon);

    /**
     * Gets all cached {@link java.net.URL}s for the {@link org.obsidianbox.api.addon.Addon}.
     *
     * The returned set will not be modifiable.
     *
     * @param addon Addon to lookup
     */
    @SideOnly (Side.SERVER)
    public Set<URL> getURLCache(Addon addon);

    /**
     * Gets a {@link java.io.File} based on the registered name.
     *
     * Internally this is the filename of the file when sent from the server.
     *
     * @param addon The addon which provided the file
     * @param name The identifier for the file
     * @return The file or null if not found
     */
    @SideOnly (Side.CLIENT)
    public File get(Addon addon, String name);

    /**
     * Gets a {@link java.util.Set} of {@link java.io.File}s associated with this addon.
     *
     * The set will be unmodifiable.
     *
     * @param addon The addon which provided the files
     * @return The collection of files
     */
    @SideOnly (Side.CLIENT)
    public Set<File> getAllFor(Addon addon);

    /**
     * Gets the entire filesystem {@link java.util.Map} of {@link org.obsidianbox.api.addon.Addon}s and their {@link java.io.File}s.
     *
     * This map will be unmodifiable.
     *
     * @return The map of addon files
     */
    @SideOnly (Side.CLIENT)
    public Map<Addon, Set<File>> getAll();
}
