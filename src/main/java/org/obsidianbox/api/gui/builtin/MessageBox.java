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
package org.obsidianbox.api.gui.builtin;

import org.obsidianbox.api.addon.Addon;
import org.obsidianbox.api.gui.Control;
import org.obsidianbox.api.gui.Form;

/**
 * Represents a typical confirmation dialog that gives feedback and asks for some in return
 */
public class MessageBox extends Form {
    private Form caller;
    private Buttons buttons;
    private Icon icon;
    private String content, title;
    /**
     * This constructor will not create a title or text body
     *
     * @param addon The addon creating the MessageBox
     */
    public MessageBox(Addon addon) {
        this(addon, null, null);
    }
    /**
     * This constructor will default to {@link MessageBox.Buttons#OK} as the button to use
     *
     * @param addon The addon creating the MessageBox
     * @param title The title to use
     * @param content The content to use
     */
    public MessageBox(Addon addon, String title, String content) {
        this(addon, title, content, Buttons.OK);
    }

    /**
     * This constructor will not set a caller
     *
     * @param addon The addon creating the MessageBox
     * @param title The title to use
     * @param content The content to use
     * @param buttons The {@link MessageBox.Buttons} to use
     */
    public MessageBox(Addon addon, String title, String content, Buttons buttons) {
        this(addon, title, content, buttons, null);
    }

    /**
     * This constructor will default to {@link MessageBox.Icon#NONE} as the icon
     *
     * @param addon The addon creating the MessageBox
     * @param title The title to use
     * @param content The content to use
     * @param buttons The {@link MessageBox.Buttons} to use
     * @param caller The caller of this MessageBox
     */
    public MessageBox(Addon addon, String title, String content, Buttons buttons, Form caller) {
        this(addon, title, content, buttons, Icon.NONE, caller);
    }

    /**
     * @param addon The addon creating the MessageBox
     * @param title The title to use
     * @param content The content to use
     * @param buttons The {@link MessageBox.Buttons} to use
     * @param icon The {@link MessageBox.Icon} to display
     * @param caller The caller of this MessageBox
     */
    public MessageBox(Addon addon, String title, String content, Buttons buttons, Icon icon, Form caller) {
        super(addon, caller == null ? "messagebox" : caller.getName() + "_messagebox", -1, -1, 200, 100, title);
        this.title = title;
        this.content = content;
        this.buttons = buttons;
        this.icon = icon;
        this.caller = caller;
    }

    /**
     * This constructor will not create a title.
     *
     * @param addon The addon creating the MessageBox
     * @param content The content to use
     */
    public MessageBox(Addon addon, String content) {
        this(addon, null, content);
    }

    /**
     * Gets the MessageBox's caller
     *
     * @return The caller of the MessageBox
     */
    public Form getCaller() {
        return caller;
    }

    /**
     * Sets the {@link MessageBox}'s caller <p> If no caller is set then the top form will be focused on when {@link MessageBox} is
     * closed.
     *
     * @param caller The {@link org.obsidianbox.api.gui.Form} that this {@link MessageBox} will focus to when closed
     */
    public MessageBox setCaller(Form caller) {
        this.caller = caller;
        return this;
    }

    /**
     * Gets the MessageBox's title.
     *
     * @return The title of the MessageBox
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the MessageBox's title. <p> If the passed in title is null or empty, no {@link org.spout.renderer.api.model.StringModel} will be created or rendered.
     *
     * @param title The string to set the title as
     * @return The MessageBox
     */
    @Override
    public MessageBox setTitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    public void add(Control control) {
        throw new IllegalStateException("You cannot add controls to a MessageBox!");
    }

    /**
     * Gets the MessageBox's message body content.
     *
     * @return The content of the MessageBox
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the MessageBox's content, which is the message body. <p> If the passed in content is null or empty, no {@link org.spout.renderer.api.model.StringModel} will be created or rendered.
     *
     * @param content The string to set the content as
     * @return The MessageBox
     */
    public MessageBox setContent(String content) {
        this.content = content;
        return this;
    }

    /**
     * Gets the MessageBox's buttons.
     *
     * @return The {@link MessageBox.Buttons} of the MessageBox
     */
    public Buttons getButtons() {
        return buttons;
    }

    /**
     * Sets the MessageBox's buttons.
     *
     * @param buttons The {@link MessageBox.Buttons} to use
     * @return The MessageBox
     */
    public MessageBox setButtons(Buttons buttons) {
        this.buttons = buttons;
        return this;
    }

    /**
     * Gets the MessageBox's icon.
     *
     * @return The {@link MessageBox.Icon} of the MessageBox
     */
    public Icon getIcon() {
        return icon;
    }

    /**
     * Sets the MessageBox's icon.
     *
     * @param icon The {@link MessageBox.Icon} to use
     * @return The MessageBox
     */
    public MessageBox setIcon(Icon icon) {
        this.icon = icon;
        return this;
    }

    public enum Buttons {
        ABORT_RETRY_IGNORE,
        OK,
        OK_CANCEL,
        RETRY_CANCEL,
        YES_NO,
        YES_NO_CANCEL;
    }

    public enum Icon {
        ASTERIK,
        ERROR,
        EXCLAMATION,
        HAND,
        INFORMATION,
        NONE,
        QUESTION,
        STOP,
        WARNING
    }
}
