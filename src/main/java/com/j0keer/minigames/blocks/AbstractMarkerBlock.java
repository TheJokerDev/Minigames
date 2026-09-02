//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.j0keer.minigames.blocks;

import com.j0keer.minigames.blockentity.MarkerBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

public abstract class AbstractMarkerBlock extends BlockWithEntity {
    private final DyeColor color;

    protected AbstractMarkerBlock(DyeColor color, AbstractBlock.Settings settings) {
        super(settings);
        this.color = color;
    }

    protected abstract MapCodec<? extends AbstractMarkerBlock> getCodec();

    public boolean canMobSpawnInside(BlockState state) {
        return true;
    }

    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new MarkerBlockEntity(pos, state, this.color);
    }

    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
        BlockEntity var5 = world.getBlockEntity(pos);
        if (var5 instanceof MarkerBlockEntity markerBlockEntity) {
            return markerBlockEntity.getPickStack();
        } else {
            return super.getPickStack(world, pos, state);
        }
    }

    public DyeColor getColor() {
        return this.color;
    }
}
