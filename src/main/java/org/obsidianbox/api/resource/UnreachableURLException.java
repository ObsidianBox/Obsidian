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
package org.obsidianbox.api.resource;

import java.net.URL;

import cpw.mods.fml.relauncher.Side;
import org.obsidianbox.api.addon.Addon;

public class UnreachableURLException extends Exception {
    private static final long serialVersionUID = 1L;

    public UnreachableURLException(Addon addon, URL unreachable, Side side) {
        this(addon, unreachable, side, null);
    }

    public UnreachableURLException(Addon addon, URL unreachable, Side side, Throwable throwable) {
        super(addon.getDescription().getName() + " sent " + unreachable + " which cannot be reached by the " + side.name().toUpperCase() + "!", throwable);
    }

    public UnreachableURLException(String reason) {
        this(reason, null);
    }

    public UnreachableURLException(String reason, Throwable throwable) {
        super(reason, throwable);
    }
}
