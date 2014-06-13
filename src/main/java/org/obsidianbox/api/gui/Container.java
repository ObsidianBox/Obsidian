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
package org.obsidianbox.api.gui;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.spout.renderer.api.model.Model;

public class Container extends Control {
    private final List<Model> models = new LinkedList<>();
    private final Set<Control> controls = new LinkedHashSet<>();

    protected Container(Form form, int x, int y, int width, int height) {
        this(form, form.getName() + "_container_root", x, y, width, height);
    }

    public Container(Form form, String name, int x, int y, int width, int height) {
        super(form, name, x, y, width, height);
    }

    @Override
    public List<Model> getModels() {
        return models;
    }

    public boolean add(Control control) {
        if (!(control instanceof Container) && control.getContainer() != null) {
            return false;
        }

        if (!controls.add(control)) {
            return false;
        }

        models.addAll(control.getModels());
        control.setContainer(this);
        return true;
    }

    public Set<Control> getAll() {
        return Collections.unmodifiableSet(controls);
    }
}
