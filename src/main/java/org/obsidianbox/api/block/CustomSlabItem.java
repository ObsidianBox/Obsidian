package org.obsidianbox.api.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemSlab;

public class CustomSlabItem extends ItemSlab {
    public CustomSlabItem(Block block) {
        super(block, (CustomSlab) block, ((CustomSlab) block).getDoubleBlock(),false);
    }
}