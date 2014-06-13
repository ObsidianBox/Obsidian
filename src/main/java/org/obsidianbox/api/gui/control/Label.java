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
package org.obsidianbox.api.gui.control;

import java.awt.Font;
import java.util.LinkedList;
import java.util.List;

import org.obsidianbox.api.gui.Form;

import org.spout.renderer.api.model.Model;
import org.spout.renderer.api.model.StringModel;

public class Label extends LabelBase {
    private final List<Model> models = new LinkedList<>();

    public Label(Form form, String name, int x, int y) {
        this(form, name, x, y, "", 0);
    }

    public Label(Form form, String name, int x, int y, String text, int size) {
        this(form, name, x, y, text, form.getAddon().getGame().getGuiRenderer().getFont(form.getAddon(), "ubuntu-regular"), size);
    }

    public Label(Form form, String name, int x, int y, String text, Font font, int size) {
        super(form, name, x, y, text, font);
        final StringModel model;
        try {
            model = form.getAddon().getGame().getGuiRenderer().createString(form.getAddon(), "font", text, font, StringModel.AntiAliasing.ON, Font.PLAIN, size);
        } catch (Exception e) {
            // Should NEVER get here as font is provided by the mod
            throw new RuntimeException(e);
        }
        models.add(model);
    }

    @Override
    public Label setText(String text) {
        super.setText(text);
        ((StringModel) models.get(0)).setString(text);
        return this;
    }

    @Override
    public final List<Model> getModels() {
        return models;
    }
}
