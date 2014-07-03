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
package org.obsidianbox.obsidian.addon;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URLClassLoader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.obsidianbox.magma.Game;
import org.obsidianbox.magma.util.FileUtils;
import org.obsidianbox.obsidian.resource.CommonFileSystem;
import org.obsidianbox.obsidian.util.map.SerializableHashMap;
import org.obsidianbox.magma.addon.Addon;
import org.obsidianbox.magma.addon.AddonDescription;
import org.obsidianbox.magma.addon.AddonManager;
import org.obsidianbox.magma.addon.InvalidAddonException;
import org.obsidianbox.magma.addon.InvalidDescriptionException;

public class CommonAddonManager implements AddonManager {
    private static final String ADDON_JSON = "addon.info";
    protected final Game game;
    private final Map<Addon, AddonClassLoader> loaders = new HashMap<>();
    private final Set<Addon> addons = new HashSet<>();
    private final SerializableHashMap addonMD5s = new SerializableHashMap();
    private final Addon internal;

    public CommonAddonManager(Game game) {
        this.game = game;
        this.internal = new InternalAddon(game);
        addons.add(internal);
    }

    @Override
    public Addon getAddon(String identifier) {
        if (isOfficialAddon(identifier)) {
            throw new IllegalStateException("Official addons are restricted to access only by the mod.");
        }
        if (identifier != null && !identifier.isEmpty()) {
            for (Addon addon : addons) {
                if (addon.getDescription().getIdentifier().equalsIgnoreCase(identifier)) {
                    return addon;
                }
            }
        }
        return null;
    }

    @Override
    public Collection<Addon> getAddons() {
        return Collections.unmodifiableCollection(addons);
    }

    @Override
    public Addon loadAddon(Path path) throws InvalidAddonException, InvalidDescriptionException {
        final AddonDescription description = create(path);

        if (!description.isValidMode(game.getSide())) {
            throw new InvalidAddonException("Attempted to load " + path + " but this can only be done on the " + game.getSide().name().toLowerCase() + ".");
        }

        final Path dataPath = Paths.get(path.getParent().toString(), description.getIdentifier());
        try {
            final AddonClassLoader loader = new AddonClassLoader(game, (URLClassLoader) MinecraftForge.class.getClassLoader(), this);
            loader.addURL(path.toUri().toURL());
            final Class<?> addonMain = Class.forName(description.getMain(), true, loader);
            final Class<? extends Addon> addonClass = addonMain.asSubclass(Addon.class);
            final Constructor<? extends Addon> constructor = addonClass.getConstructor();
            final Addon addon = constructor.newInstance();

            final Field gameField = addonClass.getSuperclass().getDeclaredField("game");
            gameField.setAccessible(true);
            gameField.set(addon, game);
            gameField.setAccessible(false);

            final Field descriptionField = addonClass.getSuperclass().getDeclaredField("description");
            descriptionField.setAccessible(true);
            descriptionField.set(addon, description);
            descriptionField.setAccessible(false);

            final Field loggerField = addonClass.getSuperclass().getDeclaredField("logger");
            loggerField.setAccessible(true);
            loggerField.set(addon, LogManager.getLogger(description.getName()));
            loggerField.setAccessible(false);

            final Field dataPathField = addonClass.getSuperclass().getDeclaredField("dataPath");
            dataPathField.setAccessible(true);
            dataPathField.set(addon, dataPath);
            dataPathField.setAccessible(false);

            loader.setAddon(addon);
            loaders.put(addon, loader);
            addons.add(addon);

            addonMD5s.put(description.getIdentifier(), FileUtils.getMD5Checksum(path));

            if (game.getSide().isServer()) {
                ((CommonFileSystem) game.getFileSystem()).onAddonLoad(addon, path);
            }
            return addon;
        } catch (Exception e) {
            throw new InvalidAddonException(e);
        }
    }

    @Override
    public Collection<Addon> loadAddons(Path path) {
        if (!Files.isDirectory(path)) {
            throw new IllegalArgumentException("Path " + path + " is not a directory!");
        }

        final DirectoryStream<Path> stream;
        try {
            stream = Files.newDirectoryStream(path, new DirectoryStream.Filter<Path>() {
                @Override
                public boolean accept(Path entry) {
                    String fname = entry.getFileName().toString();
                    return !Files.isDirectory(entry) && fname.endsWith(".jar");
                }
            });
        } catch (IOException e) {
            throw new RuntimeException("IO error occurred while traversing addons dir", e);
        }

        for (Path jar : stream) {
            try {
                loadAddon(jar);
            } catch (Exception e) {
                game.getLogger().fatal("Unable to load [" + jar.getFileName() + "] in directory [" + path + "]", e);
            }
        }
        return Collections.unmodifiableCollection(addons);
    }

    @Override
    public void initialize(Addon addon) {
        if (addon.isEnabled()) {
            throw new IllegalStateException("Cannot initialize addon [" + addon.getDescription().getName() + "], it has already been initialized and enabled!");
        }
        game.getLogger().info("Initializing [" + addon.getDescription().getName() + " " + addon.getDescription().getVersion() + "]...");

        try {
            addon.onInitialize();
            final Field initializedField = addon.getClass().getSuperclass().getDeclaredField("initialized");
            initializedField.setAccessible(true);
            initializedField.setBoolean(addon, true);
            initializedField.setAccessible(false);
            game.getLogger().info("[" + addon.getDescription().getName() + "] initialized");
        } catch (Throwable t) {
            game.getLogger().fatal("Exception caught while initializing addon [" + addon.getDescription().getName() + "]", t);
        }
    }

    @Override
    public void enable(Addon addon) {
        if (!addon.isInitialized()) {
            throw new IllegalStateException("Cannot enable addon [" + addon.getDescription().getName() + "], it has not been initialized!");
        }
        if (addon.isEnabled()) {
            throw new IllegalStateException("Cannot enable addon [" + addon.getDescription().getName() + "], it has already been enabled!");
        }
        game.getLogger().info("Enabling [" + addon.getDescription().getName() + " " + addon.getDescription().getVersion() + "]...");

        try {
            addon.onEnable();
            final Field enableField = addon.getClass().getSuperclass().getDeclaredField("enabled");
            enableField.setAccessible(true);
            enableField.setBoolean(addon, true);
            enableField.setAccessible(false);
            game.getLogger().info("[" + addon.getDescription().getName() + "] enabled");
        } catch (Throwable t) {
            game.getLogger().fatal("Exception caught while enabling addon [" + addon.getDescription().getName() + "]", t);
        }
    }

    @Override
    public void disable(Addon addon) {
        if (!addon.isEnabled()) {
            throw new IllegalStateException("Cannot disable addon [" + addon.getDescription().getName() + "], it has never been enabled!");
        }
        game.getLogger().info("Disabling [" + addon.getDescription().getName() + " " + addon.getDescription().getVersion() + "]...");

        try {
            addon.onDisable();
            final Field enableField = addon.getClass().getSuperclass().getDeclaredField("enabled");
            enableField.setAccessible(true);
            enableField.setBoolean(addon, false);
            enableField.setAccessible(false);
            game.getLogger().info("[" + addon.getDescription().getName() + "] disabled");
        } catch (Throwable t) {
            game.getLogger().fatal("Exception caught while disabling addon [" + addon.getDescription().getName() + "]", t);
        }
    }

    public static AddonDescription create(Path path) throws InvalidAddonException, InvalidDescriptionException {
        if (!Files.exists(path)) {
            throw new InvalidAddonException("Attempt to create addon description for file " + path.getFileName() + " but it does not exist!");
        }

        final AddonDescription description;
        try (final JarFile jar = new JarFile(path.toFile())) {
            final JarEntry entry = jar.getJarEntry(ADDON_JSON);

            if (entry == null) {
                throw new InvalidDescriptionException("Attempt to create an addon description failed because " + ADDON_JSON + " was not found in " + jar.getName());
            }

            final GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(AddonDescription.class, new AddonDescriptionJsonDeserializer());
            final Gson gson = builder.create();
            description = gson.fromJson(new InputStreamReader(jar.getInputStream(entry)), AddonDescription.class);
        } catch (IOException e) {
            throw new InvalidAddonException(e);
        }
        return description;
    }

    public boolean isOfficialAddon(String identifier) {
        return "internal".equalsIgnoreCase(identifier);
    }

    public void initialize() {
        for (Addon addon : addons) {
            initialize(addon);
        }
    }

    public void disable() {
        for (Addon addon : addons) {
            disable(addon);
        }
    }

    public void enable() {
        for (Addon addon : addons) {
            enable(addon);
        }
    }

    public Addon getInternalAddon() {
        return internal;
    }

    public Class<?> getClassByName(final String name, final AddonClassLoader commonLoader) {
        for (Map.Entry<Addon, AddonClassLoader> entry : loaders.entrySet()) {
            if (entry.getValue() == commonLoader) {
                continue;
            }
            try {
                Class<?> clazz = entry.getValue().findClass(name, false);
                if (clazz != null) {
                    return clazz;
                }
            } catch (ClassNotFoundException ignored) {
            }
        }
        return null;
    }

    public SerializableHashMap getAddonMD5s() {
        return addonMD5s;
    }
}
