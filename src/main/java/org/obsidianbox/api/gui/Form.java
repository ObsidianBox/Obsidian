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

import java.util.UUID;

import org.obsidianbox.api.addon.Addon;

/**
 * Represents an interact-able element of the GUI that displays a collection of controls within. <p> Can be thought of as a typical window a user can interact with on a modern operating system.
 */
public abstract class Form {
    private final Addon addon;
    private final String name;
    private final UUID uuid;
    private final Container root;
    private int x, y, width, height;
    private Anchor anchor = Anchor.NONE;
    private String title;
    private boolean pause;

    public Form(Addon addon, String name, int x, int y, int width, int height, String title) {
        this.addon = addon;
        this.name = name;
        uuid = UUID.randomUUID();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.title = title;
        root = new Container(this, x, y, width, height);
    }

    public final Addon getAddon() {
        return addon;
    }

    public final String getName() {
        return name;
    }

    public final UUID getUUID() {
        return uuid;
    }

    public int getX() {
        return x;
    }

    public Form setX(int x) {
        this.x = x;
        updateDimensions();
        return this;
    }

    /**
     * Should be called whenever a form is repositioned/resized and the root {@link Container} should be as well.
     */
    private void updateDimensions() {
        // TODO Account for size of title of Form (if it has one)
        root.setX(x);
        root.setY(y);
        root.setWidth(width);
        root.setHeight(height);
    }

    public int getY() {
        return y;
    }

    public Form setY(int y) {
        this.y = y;
        updateDimensions();
        return this;
    }

    public int getWidth() {
        return width;
    }

    public Form setWidth(int width) {
        this.width = width;
        updateDimensions();
        return this;
    }

    public int getHeight() {
        return height;
    }

    public Form setHeight(int height) {
        this.height = height;
        updateDimensions();
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Form setTitle(String title) {
        this.title = title;
        return this;
    }

    public Anchor getAnchor() {
        return anchor;
    }

    public Form setAnchor(Anchor anchor) {
        this.anchor = anchor;
        return this;
    }

    public boolean pausesGame() {
        return pause;
    }

    public Form shouldPause(boolean pause) {
        this.pause = pause;
        return this;
    }

    protected final Container getRoot() {
        return root;
    }

    public void add(Control control) {
        root.add(control);
    }

    public void onShow() {

    }

    public void onHide() {

    }

    public void onClose() {

    }
}
