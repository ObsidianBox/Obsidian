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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class AddonClassLoader extends URLClassLoader {
    private static Set<AddonClassLoader> LOADERS = new HashSet<>();
    private static final Map<String, Class<?>> NAMES_BY_CLASSES = new HashMap<>();
    private static Method addURLMethod;
    private final URLClassLoader forge;

    static {
        try {
            addURLMethod = URLClassLoader.class.getMethod("addURL", URL.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public AddonClassLoader(URLClassLoader forge, URL addonMainClassURL) {
        super(new URL[0], forge);
        this.forge = forge;
        addURL(addonMainClassURL);
        LOADERS.add(this);
    }

    public static Class<?> findAddonClass(String name) throws ClassNotFoundException {
        for (AddonClassLoader loader : LOADERS) {
            final Class<?> clazz = loader.findClass(name);
            if (clazz != null) {
                return clazz;
            }
        }
        throw new ClassNotFoundException("Class [" + name + "] was not found");
    }

    @Override
    protected void addURL(URL url) {
        super.addURL(url);

        try {
            addURLMethod.setAccessible(true);
            addURLMethod.invoke(forge, url);
            addURLMethod.setAccessible(false);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return findClass(name, true);
    }

    protected Class<?> findClass(String name, boolean checkOtherAddons) throws ClassNotFoundException {
        Class<?> result;
        boolean isForgeClass = false;
        try {
            result = super.findClass(name);
            isForgeClass = true;
        } catch (ClassNotFoundException ignored) {
            result = NAMES_BY_CLASSES.get(name);
            if (result == null && checkOtherAddons) {
                result = findClassByName(name);
            }
        }
        if (result != null && !isForgeClass) {
            NAMES_BY_CLASSES.put(name, result);
            return result;
        } else {
            throw new ClassNotFoundException(name);
        }
    }

    public Class<?> findClassByName(final String name) throws ClassNotFoundException {
        for (AddonClassLoader loader : LOADERS) {
            if (loader == this) {
                continue;
            }
            Class<?> clazz = loader.findClass(name, false);
            if (clazz != null) {
                return clazz;
            }
        }
        return null;
    }
}
