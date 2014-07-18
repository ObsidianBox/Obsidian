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
package org.obsidianbox.obsidian;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import org.obsidianbox.magma.gui.Form;
import org.obsidianbox.magma.gui.Stack;

@SideOnly(Side.CLIENT)
public class ClientStack implements Stack {
    private final Set<Form> forms = new LinkedHashSet<>();
    private Form focusedForm;
    private final DummyGuiScreen screen = new DummyGuiScreen(this);

    @Override
    public void show(Form form) {
        if (form != null && forms.add(form)) {
            showDummyIfNeeded();
        }
    }

    @Override
    public void hide(Form form) {
        if (forms.remove(form)) {
            form.onHide();
        }
        closeDummyIfNeeded();
    }

    @Override
    public void hide(String formName) {
        final Iterator<Form> iterator = forms.iterator();
        while (iterator.hasNext()) {
            final Form form = iterator.next();
            if (form.getName().equals(formName)) {
                form.onHide();
                iterator.remove();
            }
        }
        closeDummyIfNeeded();
    }

    @Override
    public void hide(UUID uuid) {
        final Iterator<Form> iterator = forms.iterator();
        while (iterator.hasNext()) {
            final Form form = iterator.next();
            if (form.getUUID().equals(uuid)) {
                form.onHide();
                iterator.remove();
            }
        }
        closeDummyIfNeeded();
    }

    @Override
    public void close(Form form) {
        if (forms.remove(form)) {
            form.onClose();
        }
        closeDummyIfNeeded();
    }

    @Override
    public void close(String formName) {
        final Iterator<Form> iterator = forms.iterator();
        while (iterator.hasNext()) {
            final Form form = iterator.next();
            if (form.getName().equals(formName)) {
                form.onClose();
                iterator.remove();
            }
        }
        closeDummyIfNeeded();
    }

    @Override
    public void close(UUID uuid) {
        final Iterator<Form> iterator = forms.iterator();
        while (iterator.hasNext()) {
            final Form form = iterator.next();
            if (form.getUUID().equals(uuid)) {
                form.onClose();
                iterator.remove();
            }
        }
        closeDummyIfNeeded();
    }

    private void showDummyIfNeeded() {
        if (!forms.isEmpty()) {
            if (Minecraft.getMinecraft().currentScreen == null) {
                Minecraft.getMinecraft().displayGuiScreen(screen);
            }
        }
    }

    private void closeDummyIfNeeded() {
        if (forms.isEmpty()) {
            Minecraft.getMinecraft().displayGuiScreen(null);
        }
    }

    private boolean anyPaused() {
        for (Form form : forms) {
            if (form.pausesGame()) {
                return true;
            }
        }
        return false;
    }

    public static class DummyGuiScreen extends GuiScreen {
        ClientStack stack;

        public DummyGuiScreen(ClientStack stack) {
            this.stack = stack;
        }

        @Override
        public boolean doesGuiPauseGame() {
            return stack.anyPaused();
        }
    }
}