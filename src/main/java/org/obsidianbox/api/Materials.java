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

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class Materials {
    public static final CustomBlockMaterial CUSTOM_BLOCK = new CustomBlockMaterial();
    public static final CustomDoorMaterial CUSTOM_DOOR = new CustomDoorMaterial();
    public static final CustomFenceMaterial CUSTOM_FENCE = new CustomFenceMaterial();
    public static final CustomFenceGateMaterial CUSTOM_FENCE_GATE = new CustomFenceGateMaterial();
    public static final CustomMovingMaterial CUSTOM_MOVING = new CustomMovingMaterial();
    public static final CustomPlantMaterial CUSTOM_PLANT = new CustomPlantMaterial();
    public static final CustomSlabMaterial CUSTOM_SLAB = new CustomSlabMaterial();
    public static final CustomStairMaterial CUSTOM_STAIR = new CustomStairMaterial();
    public static final CustomWallMaterial CUSTOM_WALL = new CustomWallMaterial();

    public static class CustomBlockMaterial extends Material {
        public CustomBlockMaterial() {
            super(MapColor.clayColor);
        }
    }

    public static class CustomDoorMaterial extends Material {
        public CustomDoorMaterial() {
            super(MapColor.clayColor);
        }
    }

    public static class CustomMovingMaterial extends Material {
        public CustomMovingMaterial() {
            super(MapColor.clayColor);
        }
    }

    public static class CustomFenceMaterial extends Material {
        public CustomFenceMaterial() {
            super(MapColor.clayColor);
        }
    }

    public static class CustomFenceGateMaterial extends Material {
        public CustomFenceGateMaterial() {
            super(MapColor.clayColor);
        }
    }

    public static class CustomPlantMaterial extends Material {
        public CustomPlantMaterial() {
            super(MapColor.clayColor);
        }
    }

    public static class CustomSlabMaterial extends Material {
        public CustomSlabMaterial() {
            super(MapColor.clayColor);
        }
    }

    public static class CustomStairMaterial extends Material {
        public CustomStairMaterial() {
            super(MapColor.clayColor);
        }
    }

    public static class CustomWallMaterial extends Material {
        public CustomWallMaterial() {
            super(MapColor.clayColor);
        }
    }
}