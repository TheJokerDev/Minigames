package com.j0keer.minigames.blocks;

import com.google.common.collect.Maps;
import com.j0keer.minigames.registries.BlockRegistries;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Map;

import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationPropertyHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class MarkerBlock extends AbstractMarkerBlock {
    public static final MapCodec<MarkerBlock> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(DyeColor.CODEC.fieldOf("color").forGetter(AbstractMarkerBlock::getColor), createSettingsCodec()).apply(instance, MarkerBlock::new));
    public static final IntProperty ROTATION;
    private static final Map<DyeColor, Block> COLORED_BANNERS;
    private static final VoxelShape SHAPE;

    public MapCodec<MarkerBlock> getCodec() {
        return CODEC;
    }

    public MarkerBlock(DyeColor dyeColor, AbstractBlock.Settings settings) {
        super(dyeColor, settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(ROTATION, 0));
        COLORED_BANNERS.put(dyeColor, this);
    }

    protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return world.getBlockState(pos.down()).isSolid();
    }

    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(ROTATION, RotationPropertyHelper.fromYaw(ctx.getPlayerYaw() + 180.0F));
    }

    protected BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return direction == Direction.DOWN && !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(ROTATION, rotation.rotate(state.get(ROTATION), 16));
    }

    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.with(ROTATION, mirror.mirror(state.get(ROTATION), 16));
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ROTATION);
    }

    public static Block getForColor(DyeColor color) {
        return COLORED_BANNERS.getOrDefault(color, BlockRegistries.WHITE_MARKER);
    }

    static {
        ROTATION = Properties.ROTATION;
        COLORED_BANNERS = Maps.newHashMap();
        SHAPE = Block.createCuboidShape(4.0F, 0.0F, 4.0F, 12.0F, 16.0F, 12.0F);
    }
}
