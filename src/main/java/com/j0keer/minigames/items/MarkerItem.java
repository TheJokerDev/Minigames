package com.j0keer.minigames.items;

import com.j0keer.minigames.registries.BlockRegistries;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;

public class MarkerItem extends BlockItem {
    private final DyeColor color;

    public MarkerItem(DyeColor color, Settings settings) {
        super(BlockRegistries.MARKERS.get(color), settings);
        this.color = color;
    }

    @Override
    public ActionResult place(ItemPlacementContext context) {
        return super.place(context);
    }
}
