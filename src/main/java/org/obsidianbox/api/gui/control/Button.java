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

import com.flowpowered.math.vector.Vector2f;
import org.obsidianbox.api.gui.Form;
import org.obsidianbox.api.renderer.Colors;

import org.spout.renderer.api.data.UniformHolder;
import org.spout.renderer.api.model.Model;
import org.spout.renderer.api.model.StringModel;

public class Button extends LabelBase {
    private final List<Model> models = new LinkedList<>();
    private final UniformHolder backgroundUniforms = new UniformHolder();

    public Button(Form form, String name, int x, int y, int width, int height) {
        this(form, name, x, y, "", width, height);
    }

    public Button(Form form, String name, int x, int y, String text, int width, int height) {
        this(form, name, x, y, text, form.getAddon().getGame().getGuiRenderer().getFont(form.getAddon(), "ubuntu-regular"), width, height);
    }

    public Button(Form form, String name, int x, int y, String text, Font font, int width, int height) {
        super(form, name, x, y, text, font);

        final Model backgroundModel;
        final StringModel stringModel;
        try {
            backgroundModel = form.getAddon().getGame().getGuiRenderer().createColoredPlane(form.getAddon(), "basic", backgroundUniforms, Colors.BLUE.getValue(), new Vector2f(width, height));
            stringModel = form.getAddon().getGame().getGuiRenderer().createString(form.getAddon(), "font", text, font, StringModel.AntiAliasing.ON, Font.PLAIN, 12);
        } catch (Exception e) {
            // Should NEVER get here as font is provided by the mod
            throw new RuntimeException(e);
        }

        models.add(backgroundModel);
        models.add(stringModel);
    }

    @Override
    public List<Model> getModels() {
        return models;
    }
}
