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
package org.obsidianbox.mod.resource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.obsidianbox.api.resource.FileSystem;
import org.obsidianbox.api.Game;
import org.obsidianbox.api.addon.Addon;
import org.obsidianbox.api.resource.UnreachableURLException;

public class CommonFileSystem implements FileSystem {
    public static final Path BASE_PATH = Paths.get(".");
    public static final Path MODS_PATH = Paths.get(BASE_PATH.toString(), "mods");
    public static final Path ADDONS_PATH = Paths.get(MODS_PATH.toString(), Game.MOD_ID.toLowerCase() + File.separator + "addons");
    private static final Map<Addon, File> ADDON_DATA_FILES = new HashMap<>();
    private static final Map<Addon, Set<Path>> ADDON_FILE_CACHES = new HashMap<>();
    private static final Map<Addon, Set<URL>> ADDON_URL_CACHES = new HashMap<>();
    private transient boolean locked = false;

    @Override
    public void cache(Addon addon, Path path) throws FileNotFoundException {
        if (addon == null) {
            throw new IllegalStateException("Cannot cache a path for a null addon instance!");
        }
        if (locked) {
            throw new IllegalStateException(addon.getDescription().getName() + " attempted to cache a file after INITIALIZATION phase! This is NOT ALLOWED.");
        }
        if (!Files.exists(path)) {
            throw new FileNotFoundException(addon.getDescription().getName() + " is trying to send a path [" + path + "] that does not exist!");
        }
        if (Files.isDirectory(path)) {
            throw new FileNotFoundException(addon.getDescription().getName() + " is trying to send a directory to the client, not a file!");
        }
        if (Files.isExecutable(path)) {
            throw new IllegalStateException(addon.getDescription().getName() + " is attempting to cache an executable file to send from your server! This is NOT ALLOWED.");
        }
        Set<Path> paths = ADDON_FILE_CACHES.get(addon);
        if (paths == null) {
            paths = new HashSet<>();
            ADDON_FILE_CACHES.put(addon, paths);
        }
        paths.add(path);
    }

    @Override
    public void cache(Addon addon, URL url) throws UnreachableURLException {
        if (addon == null) {
            throw new IllegalStateException("Cannot cache an URL for a null addon instance!");
        }
        if (locked) {
            throw new IllegalStateException(addon.getDescription().getName() + " attempted to cache an url after INITIALIZATION phase! This is NOT ALLOWED.");
        }
        if (url == null) {
            throw new IllegalStateException("Cannot cache a null URL instance for " + addon.getDescription().getName());
        }
        try {
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            int code = connection.getResponseCode();
            switch (code) {
                case 403:
                    throw new UnreachableURLException(addon.getDescription().getName() + " sent a url which is forbidden for the server to access it. This is NOT ALLOWED.");
                case 404:
                    throw new UnreachableURLException(addon, url, Side.SERVER);
            }
        } catch (IOException e) {
            throw new UnreachableURLException(addon, url, Side.SERVER, e);
        }
        Set<URL> urls = ADDON_URL_CACHES.get(addon);
        if (urls == null) {
            urls = new HashSet<>();
            ADDON_URL_CACHES.put(addon, urls);
        }
        urls.add(url);
    }

    @Override
    public Set<Path> getPathCache(Addon addon) {
        if (addon == null) {
            throw new IllegalStateException("Cannot return a list of paths for a null addon instance!");
        }
        final Set<Path> paths = ADDON_FILE_CACHES.get(addon);
        return paths == null ? new HashSet<Path>() : Collections.unmodifiableSet(paths);
    }

    @Override
    public Set<URL> getURLCache(Addon addon) {
        if (addon == null) {
            throw new IllegalStateException("Cannot return a list of urls for a null addon instance!");
        }
        final Set<URL> urls = ADDON_URL_CACHES.get(addon);
        return urls == null ? new HashSet<URL>() : Collections.unmodifiableSet(urls);
    }

    @Override
    public File get(Addon addon, String name) {
        throw new UnsupportedOperationException("Getting an individual file is not allowed in server mode!");
    }

    @Override
    public Set<File> getAllFor(Addon addon) {
        throw new UnsupportedOperationException("Getting a list of files is not allowed in server mode!");
    }

    @Override
    public Map<Addon, Set<File>> getAll() {
        throw new UnsupportedOperationException("Getting map of addon and their files is not allowed in server mode!");
    }

    public void setupFileSystem() throws IOException {
        if (!Files.exists(ADDONS_PATH)) {
            Files.createDirectories(ADDONS_PATH);
        }
    }

    public void lockFileSystem() {
        locked = true;
    }

    @SideOnly(Side.SERVER)
    public void onAddonLoad(Addon addon, Path addonRoot) {
        ADDON_DATA_FILES.put(addon, addonRoot.toFile());
    }

    @SideOnly(Side.SERVER)
    public File getAddonDataFile(Addon addon) {
        return ADDON_DATA_FILES.get(addon);
    }
}
