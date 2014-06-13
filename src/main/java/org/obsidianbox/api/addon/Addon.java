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
package org.obsidianbox.api.addon;

import java.nio.file.Path;

import org.apache.logging.log4j.Logger;
import org.obsidianbox.api.Game;

/**
 * An addon is like a Mod or a Plugin (from Bukkit). It is the bridge between external code and Obsidian's framework. <p> The power of addons lie in the ability to use Game API and Forge without
 * needing to make a new Mod.
 */
public abstract class Addon {
    private Game game;
    private AddonDescription description;
    private Logger logger;
    private Path dataPath;
    private boolean initialized = false;
    private boolean enabled = false;

    protected Addon() {
    }

    public void onInitialize() {
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public final boolean isInitialized() {
        return initialized;
    }

    public final boolean isEnabled() {
        return enabled;
    }

    public final Game getGame() {
        return game;
    }

    public final AddonDescription getDescription() {
        return description;
    }

    public final Logger getLogger() {
        return logger;
    }

    public final Path getDataFolder() {
        return dataPath;
    }

    @Override
    public final int hashCode() {
        return description.hashCode();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Addon)) {
            return false;
        }

        final Addon addon = (Addon) o;

        return description.equals(addon.description);
    }

    @Override
    public final String toString() {
        return "Addon{" +
                "game=" + game +
                ", description=" + description +
                ", logger=" + logger +
                ", dataPath=" + dataPath +
                ", initialized= " + initialized +
                ", enabled=" + enabled +
                '}';
    }
}
