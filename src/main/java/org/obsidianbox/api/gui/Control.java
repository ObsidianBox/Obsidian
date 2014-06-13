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

import java.util.List;
import java.util.UUID;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.obsidianbox.api.gui.control.event.ControlClickEvent;
import org.obsidianbox.api.gui.control.event.ControlEnableEvent;
import org.obsidianbox.api.gui.control.event.ControlFocusEvent;
import org.obsidianbox.api.gui.control.event.ControlHoverEvent;
import org.obsidianbox.api.gui.control.event.ControlVisibilityEvent;

import org.spout.renderer.api.model.Model;

@SideOnly (Side.CLIENT)
public abstract class Control {
    private final Form form;
    private final UUID uuid;
    private final String name;
    private int x, y, width, height;
    private boolean enabled = true, visible = true;
    private Anchor anchor = Anchor.NONE;
    private Container container;

    public Control(Form form, String name, int x, int y) {
        this(form, name, x, y, 0, 0);
    }

    public Control(Form form, String name, int x, int y, int width, int height) {
        this.form = form;
        this.name = name;
        uuid = UUID.randomUUID();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public final Form getForm() {
        return form;
    }

    public final UUID getUUID() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public Control setX(int x) {
        this.x = x;
        return this;
    }

    public int getY() {
        return y;
    }

    public Control setY(int y) {
        this.y = y;
        return this;
    }

    public int getWidth() {
        return width;
    }

    public Control setWidth(int width) {
        this.width = width;
        return this;
    }

    public int getHeight() {
        return height;
    }

    public Control setHeight(int height) {
        this.height = height;
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Anchor getAnchor() {
        return anchor;
    }

    public Control setAnchor(Anchor anchor) {
        this.anchor = anchor;
        return this;
    }

    public Container getContainer() {
        return container;
    }

    protected void setContainer(Container container) {
        this.container = container;
    }

    public abstract List<Model> getModels();

    /**
     * Callback received when {@link org.obsidianbox.api.gui.control.event.ControlClickEvent} is fired and not cancelled.
     *
     * @param event The event that was fired
     */
    public void onClicked(ControlClickEvent event) {

    }

    /**
     * Callback received when {@link org.obsidianbox.api.gui.control.event.ControlEnableEvent} is fired and not cancelled.
     *
     * @param event The event that was fired
     */
    public void onEnabled(ControlEnableEvent event) {

    }

    /**
     * Callback received when {@link org.obsidianbox.api.gui.control.event.ControlFocusEvent} is fired and not cancelled.
     *
     * @param event The event that was fired
     */
    public void onFocused(ControlFocusEvent event) {

    }

    /**
     * Callback received when {@link org.obsidianbox.api.gui.control.event.ControlHoverEvent} is fired and not cancelled.
     *
     * @param event The event that was fired
     */
    public void onHovered(ControlHoverEvent event) {

    }

    /**
     * Callback received when {@link org.obsidianbox.api.gui.control.event.ControlVisibilityEvent} is fired and not cancelled.
     *
     * @param event The event that was fired
     */
    public void onVisible(ControlVisibilityEvent event) {

    }

    /**
     * Called before each render by the {@link org.obsidianbox.api.renderer.Renderer}.
     *
     * @param dt The delta time since the last rendering (in milliseconds)
     */
    public void onRender(float dt) {

    }

    /**
     * Called after each render call by the {@link org.obsidianbox.api.renderer.Renderer}.
     */
    public void onPostRender() {

    }

    @Override
    public final int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Control)) {
            return false;
        }

        final Control control = (Control) o;

        return uuid.equals(control.uuid);
    }
}
