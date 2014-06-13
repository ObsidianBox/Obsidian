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
package org.obsidianbox.api;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.obsidianbox.api.addon.AddonManager;
import org.obsidianbox.api.lang.LanguageRegistry;
import org.obsidianbox.api.message.MessagePipeline;
import org.obsidianbox.api.renderer.Renderer;
import org.obsidianbox.api.resource.FileSystem;

public final class Game {
    public static final String MOD_ID = "Obsidian";
    private static final Logger LOGGER = LogManager.getLogger("Obsidian");
    private final Side side;
    private AddonManager addonManager;
    private FileSystem fileSystem;
    private LanguageRegistry languages;
    private MessagePipeline messagePipeline;
    private Renderer guiRenderer;
    private CreativeTabs tabs;

    public Game(Side side) {
        this.side = side;
    }

    public Side getSide() {
        return side;
    }

    public Logger getLogger() {
        return LOGGER;
    }

    public AddonManager getAddonManager() {
        return addonManager;
    }

    public void setAddonManager(AddonManager addonManager) {
        if (this.addonManager != null) {
            throw new IllegalStateException("Setting the addon manager again is not allowed!");
        }
        if (addonManager == null) {
            throw new IllegalArgumentException("Setting a null addon manager instance is not allowed!");
        }
        this.addonManager = addonManager;
    }

    public FileSystem getFileSystem() {
        return fileSystem;
    }

    public void setFileSystem(FileSystem fileSystem) {
        if (this.fileSystem != null) {
            throw new IllegalStateException("Setting the filesystem again is not allowed!");
        }
        if (fileSystem == null) {
            throw new IllegalArgumentException("Setting a null filesystem instance is not allowed!");
        }
        this.fileSystem = fileSystem;
    }

    public LanguageRegistry getLanguages() {
        return languages;
    }

    public void setLanguages(LanguageRegistry languages) {
        if (this.languages != null) {
            throw new IllegalStateException("Setting the language registry again is not allowed!");
        }
        if (languages == null) {
            throw new IllegalStateException("Setting a null language registry is not allowed!");
        }
        this.languages = languages;
    }

    public MessagePipeline getPipeline() {
        return messagePipeline;
    }

    public void setPipeline(MessagePipeline messagePipeline) {
        if (this.messagePipeline != null) {
            throw new IllegalStateException("Setting the message pipeline again is not allowed!");
        }
        if (messagePipeline == null) {
            throw new IllegalArgumentException("Setting a null message pipeline instance is not allowed!");
        }
        this.messagePipeline = messagePipeline;
    }

    @SideOnly (Side.CLIENT)
    public Renderer getGuiRenderer() {
        return guiRenderer;
    }

    @SideOnly (Side.CLIENT)
    public void setGuiRenderer(Renderer guiRenderer) {
        if (this.guiRenderer != null) {
            throw new IllegalStateException("Setting the gui renderer again is not allowed!");
        }
        if (guiRenderer == null) {
            throw new IllegalArgumentException("Setting a null gui renderer instance is not allowed!");
        }
        this.guiRenderer = guiRenderer;
    }

    public CreativeTabs getTabs() {
        return tabs;
    }

    public void setTabs(CreativeTabs tabs) {
        if (this.tabs != null) {
            throw new IllegalStateException("Setting the tab again is not allowed!");
        }
        if (tabs == null) {
            throw new IllegalArgumentException("Setting a null tab instance is not allowed!");
        }
        this.tabs = tabs;
    }
}
