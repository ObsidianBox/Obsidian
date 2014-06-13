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

import cpw.mods.fml.relauncher.Side;

public final class AddonDescription {
    private final String identifier;
    private final String name;
    private final String version;
    private final AddonMode mode;
    private final String mainClassName;

    public AddonDescription(String identifier, String name, String version, AddonMode mode, String mainClassName) {
        this.identifier = identifier;
        this.name = name;
        this.version = version;
        this.mode = mode;
        this.mainClassName = mainClassName;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public AddonMode getMode() { return mode; }

    public String getMain() {
        return mainClassName;
    }

    public boolean isValidMode(Side side) {
        switch (mode) {
            case CLIENT:
                if (side.isClient()) {
                    return true;
                }
                break;
            case SERVER:
                if (side.isServer()) {
                    return true;
                }
                break;
            case BOTH:
                return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return identifier.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final AddonDescription that = (AddonDescription) o;

        return identifier.equals(that.identifier);
    }

    @Override
    public String toString() {
        return "AddonDescription{" +
                "identifier='" + identifier + '\'' +
                ", name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", mode=" + mode +
                ", mainClassName='" + mainClassName + '\'' +
                '}';
    }
}
