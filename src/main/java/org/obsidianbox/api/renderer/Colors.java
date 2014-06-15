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
package org.obsidianbox.api.renderer;

public enum Colors {
    BLACK("#ff000000", new Color(0, 0, 0, 255)),
    BLUE("#ff0000ff", new Color(0, 0, 255, 255)),
    BROWN("#ffc19953", new Color(193, 153, 83, 255)),
    CRIMSON("#ffdc143c", new Color(220, 20, 60, 255)),
    CYAN("#ff00ffff", new Color(0, 255, 255, 255)),
    GOLD("#ffffd700", new Color(255, 215, 0, 255)),
    GRAY("#ff808080", new Color(128, 128, 128, 255)),
    GREEN("#ff008000", new Color(0, 128, 0, 255)),
    INDIGO("#ff4b0082", new Color(75, 0, 130, 255)),
    OBSIDIAN_BLACK_PRIMARY("#ff6c6c6d", new Color(108, 108, 109, 255)),
    OBSIDIAN_BLACK_SECONDARY("#ff303030", new Color(48, 48, 48, 255)),
    OBSIDIAN_BLUE_PRIMARY("#ff4d92b8", new Color(77, 146, 184, 255)),
    OBSIDIAN_BLUE_SECONDARY("#ff122352", new Color(18, 35, 82, 255)),
    OBSIDIAN_GREEN_PRIMARY("#ff4B9627", new Color(75, 150, 39, 255)),
    OBSIDIAN_GREEN_SECONDARY("#ff064900", new Color(6, 73, 0, 255)),
    OBSIDIAN_ORANGE_PRIMARY("#ffed7600", new Color(237, 118, 0, 255)),
    OBSIDIAN_ORANGE_SECONDARY("#ffa15000", new Color(161, 80, 0, 255)),
    OBSIDIAN_PURPLE_PRIMARY("#ff7767AE", new Color(119, 103, 174, 255)),
    OBSIDIAN_PURPLE_SECONDARY("#ff322A4F", new Color(50, 42, 79, 255)),
    OBSIDIAN_RED_PRIMARY("#ffd90c1a", new Color(217, 12, 26, 255)),
    OBSIDIAN_RED_SECONDARY("#ff62070F", new Color(98, 7, 15, 255)),
    OBSIDIAN_YELLOW_PRIMARY("#ffc0ba1d", new Color(192, 186, 29, 255)),
    OBSIDIAN_YELLOW_SECONDARY("#ff9fa500", new Color(159, 165, 0, 255)),
    ORANGE("#ffffa500", new Color(255, 165, 0, 255)),
    PINK("#ffffc0cb", new Color(255, 192, 203, 255)),
    PURPLE("#ff800080", new Color(128, 0, 128, 255)),
    RED("#ffff0000", new Color(255, 0, 0, 255)),
    SILVER("#ffc0c0c0", new Color(192, 192, 192, 255)),
    VIOLET("#ffee82ee", new Color(238, 130, 238, 255)),
    WHITE("#ffffffff", new Color(255, 255, 255, 255)),
    YELLOW("#ffffff00", new Color(255, 255, 0, 255));
    private final String hex;
    private final Color color;

    /**
     * @param hex The hex value of the color (#aarrggbb)
     * @param color {@link Color}
     */
    Colors(String hex, Color color) {
        this.hex = hex;
        this.color = color;
    }

    public String getHex() {
        return hex;
    }

    public Color getValue() {
        return color;
    }

    @Override
    public String toString() {
        return hex;
    }
}
