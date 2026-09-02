package com.j0keer.minigames.items;

import com.j0keer.minigames.blocks.AbstractMarkerBlock;
import com.j0keer.minigames.registries.BlockRegistries;
import net.minecraft.block.AbstractBannerBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BannerPatternsComponent;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Direction;
import org.apache.commons.lang3.Validate;

import java.util.List;

public class MarkerItem extends VerticallyAttachableBlockItem {
    public MarkerItem(Block bannerBlock, Item.Settings settings) {
        super(bannerBlock, bannerBlock, settings, Direction.DOWN);
        Validate.isInstanceOf(AbstractMarkerBlock.class, bannerBlock);
    }

    public DyeColor getColor() {
        return ((AbstractMarkerBlock)this.getBlock()).getColor();
    }
}
