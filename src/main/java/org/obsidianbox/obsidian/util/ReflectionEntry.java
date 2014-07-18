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
package org.obsidianbox.obsidian.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourcePack;

public final class ReflectionEntry<C, T> {
    private static boolean isDevEnvironment;
    private final Class<C> containingClass;
    private final String seargeName;
    private final String devName;
    private final String actualName;

    static {
        try {
            // TODO Does this exist on the server?
            Minecraft.class.getDeclaredField("running");
            isDevEnvironment = true;
        } catch (SecurityException | NoSuchFieldException ignore) {
            isDevEnvironment = false;
        }
    }

    public ReflectionEntry(Class<C> containingClass, String devName, String seargeName) {
        this.containingClass = containingClass;
        this.devName = devName;
        this.seargeName = seargeName;
        actualName = isDevEnvironment ? devName : seargeName;
    }

    public Class<C> getContainingClass() {
        return containingClass;
    }

    public String getDevName() {
        return devName;
    }

    public String getSeargeName() {
        return seargeName;
    }

    @SuppressWarnings("unchecked")
    public T get(C instance) {
        try {
            Field field = containingClass.getDeclaredField(actualName);
            field.setAccessible(true);
            return (T) field.get(instance);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public T set(C instance, T value) {
        try {
            Field field = containingClass.getDeclaredField(actualName);
            field.setAccessible(true);
            field.set(instance, value);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return value;
    }

    public T setFinal(C instance, T value) {
        try {
            Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);

            Field field = containingClass.getDeclaredField(actualName);
            modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.setAccessible(true);
            field.set(instance, value);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return value;
    }

    public static class Fields {
        @SideOnly(Side.CLIENT)
        public static final ReflectionEntry<Minecraft, List<IResourcePack>> MINECRAFT_DEFAULT_RESOURCE_PACKS = new ReflectionEntry<>(Minecraft.class, "defaultResourcePacks", "field_110449_ao");
    }
}
