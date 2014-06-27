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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.obsidianbox.magma.Game;
import org.obsidianbox.magma.addon.Addon;
import org.obsidianbox.magma.addon.AddonManager;

public final class AddonClassLoader extends URLClassLoader {
    private static final Map<String, Addon> CLASSES_IN_ADDONS = new HashMap<>();
    private static final Set<AddonClassLoader> LOADERS = new HashSet<>();
    private final Map<String, Class<?>> namesByClasses = new HashMap<>();
    private final Game game;
    private final URLClassLoader forge;
    private final AddonManager manager;
    private Addon addon;

    public AddonClassLoader(Game game, URLClassLoader forge, AddonManager manager) {
        super(new URL[0], forge);
        this.game = game;
        this.forge = forge;
        this.manager = manager;
        LOADERS.add(this);
    }

    public static Addon getAddon(String className) {
        return CLASSES_IN_ADDONS.get(className);
    }

    public static Class<?> findAddonClass(String name) throws ClassNotFoundException {
        for (AddonClassLoader loader : LOADERS) {
            final Class<?> clazz = loader.findClass(name);
            if (clazz != null) {
                return clazz;
            }
        }
        throw new ClassNotFoundException("Class " + name + " was not found");
    }

    @Override
    protected void addURL(URL url) {
        super.addURL(url);

        Method method;
        try {
            method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] {URL.class});
            method.setAccessible(true);
            method.invoke(forge, url);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return findClass(name, true);
    }

    protected Class<?> findClass(String name, boolean checkOtherAddons) throws ClassNotFoundException {
        game.getLogger().info("Attempting to find class " + name);
        Class<?> result = namesByClasses.get(name);

        if (result == null) {
            game.getLogger().info("Class " + name + " has never been looked up before");
            try {
                result = super.findClass(name);
            } catch (ClassNotFoundException ignored) {
            }

            if (result == null && checkOtherAddons) {
                game.getLogger().info("Class " + name + " is not in the forge classloader");
                result = ((CommonAddonManager) manager).getClassByName(name, this);
            }

            if (result != null) {
                game.getLogger().info("Class " + name + " found!. Result: " + result + ", addon: " + addon);
                namesByClasses.put(name, result);
                CLASSES_IN_ADDONS.put(name, addon);
            } else {
                throw new ClassNotFoundException(name);
            }
        }

        return result;
    }

    protected void setAddon(Addon addon) {
        if (this.addon != null) {
            throw new IllegalStateException("Attempt to set the addon of an addon class loader twice!");
        }
        if (addon == null) {
            throw new IllegalStateException("Attempt to set addon of classloader to null!");
        }
        this.addon = addon;
        CLASSES_IN_ADDONS.put(addon.getClass().getName(), addon);
    }

    public Set<String> getClassNames() {
        return Collections.unmodifiableSet(namesByClasses.keySet());
    }

    public Collection<Class<?>> getClasses() {
        return Collections.unmodifiableCollection(namesByClasses.values());
    }
}
