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
package org.obsidianbox.api.gui.control.event;

import java.util.Arrays;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.obsidianbox.api.gui.Control;
import org.obsidianbox.api.gui.action.HoverActions;

@Cancelable
@SideOnly (Side.CLIENT)
public class ControlHoverEvent extends ControlEvent {
    private final int x;
    private final int y;
    private final HoverActions action;
    private final Keyboard[] keys;

    public ControlHoverEvent(Control control, int x, int y, HoverActions action, Keyboard... keys) {
        super(control);
        this.x = x;
        this.y = y;
        this.action = action;
        this.keys = keys;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public HoverActions getAction() {
        return action;
    }

    public Keyboard[] getKeys() {
        return keys;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + x;
        result = 31 * result + y;
        result = 31 * result + action.hashCode();
        result = 31 * result + Arrays.hashCode(keys);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        final ControlHoverEvent that = (ControlHoverEvent) o;

        return x == that.x && y == that.y && Arrays.equals(keys, that.keys) && action == that.action;
    }

    @Override
    public String toString() {
        return "ControlHoveredEvent{" +
                getControl() +
                ", x=" + x +
                ", y=" + y +
                ", action=" + action +
                ", keys=" + Arrays.toString(keys) +
                '}';
    }
}
