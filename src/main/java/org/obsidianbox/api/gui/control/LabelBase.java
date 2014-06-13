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

import org.obsidianbox.api.gui.Control;
import org.obsidianbox.api.gui.Form;

public abstract class LabelBase extends Control {
    private String text;
    private Font font;

    public LabelBase(Form form, String name, int x, int y, String text, Font font) {
        super(form, name, x, y);
        setText(text);
        setFont(font);
    }

    protected LabelBase(Form form, String name, int x, int y, int width, int height, String text, Font font) {
        super(form, name, x, y, width, height);
        setText(text);
        setFont(font);
    }

    public String getText() {
        return text;
    }

    public LabelBase setText(String text) {
        this.text = text;
        return this;
    }

    public Font getFont() {
        return font;
    }

    public LabelBase setFont(Font font) {
        this.font = font;
        return this;
    }
}
