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

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Contains all {@link Form}s on the GUI and handles transferring focus status to the correct form.
 */
@SideOnly (Side.CLIENT)
public interface Stack {
    /**
     * Adds the {@link Form} to the stack for showing to the user
     *
     * @param form Form to be added
     */
    public void show(Form form);

    /**
     * Hides the {@link Form}. The form will retain its state and all assets will remain on the GPU.
     *
     * @param form The form to hide
     */
    public void hide(Form form);

    /**
     * @param formName The name of the form to hide
     * @see #hide(Form)
     */
    public void hide(String formName);

    /**
     * @param uuid The uuid of the form to hide
     * @see #hide(Form)
     */
    public void hide(UUID uuid);

    /**
     * Closes the {@link Form}. The state of the form will return to defaults and all assets disposed of from the GPU.
     *
     * @param form The form to close
     */
    public void close(Form form);

    /**
     * @param formName The name of the form to close
     * @see #close(Form)
     */
    public void close(String formName);

    /**
     * @param uuid The uuid of the form to close
     * @see #close(Form)
     */
    public void close(UUID uuid);
}
