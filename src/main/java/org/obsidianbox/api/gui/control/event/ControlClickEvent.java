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
import org.obsidianbox.api.gui.action.ClickActions;

/**
 * Callback when a control is clicked by the user.
 */
@Cancelable
@SideOnly (Side.CLIENT)
public class ControlClickEvent extends ControlEvent {
    private final int x;
    private final int y;
    private final ClickActions type;
    private final Keyboard[] keys;

    public ControlClickEvent(Control control, int x, int y, ClickActions type, Keyboard... keys) {
        super(control);
        this.x = x;
        this.y = y;
        this.type = type;
        this.keys = keys;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public ClickActions getType() {
        return type;
    }

    public Keyboard[] getKeys() {
        return keys;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + x;
        result = 31 * result + y;
        result = 31 * result + type.hashCode();
        result = 31 * result + Arrays.hashCode(keys);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ControlClickEvent)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        final ControlClickEvent that = (ControlClickEvent) o;

        return x == that.x && y == that.y && Arrays.equals(keys, that.keys) && type == that.type;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " {" +
                "control= " + getControl() +
                ", x=" + x +
                ", y=" + y +
                ", type=" + type +
                ", keys=" + Arrays.toString(keys) +
                '}';
    }
}
