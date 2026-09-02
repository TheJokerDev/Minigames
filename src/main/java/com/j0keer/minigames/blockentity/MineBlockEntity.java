package com.j0keer.minigames.blockentity;

import com.j0keer.minigames.blocks.MineBlock;
import com.j0keer.minigames.blocks.MineState;
import com.j0keer.minigames.registries.EntityRegistries;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;

public class MineBlockEntity extends BlockEntity {
    private MineState state = MineState.HIDDEN;

    public MineBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistries.MINE_BLOCK_ENTITY, pos, state);
        if (state.contains(MineBlock.STATE)) {
            this.state = state.get(MineBlock.STATE);
        }
    }

    public MineState getState() {
        BlockState blockState = getCachedState();
        if (blockState.contains(MineBlock.STATE)) return blockState.get(MineBlock.STATE);
        return this.state;
    }

    public void setState(MineState state) {
        if (state == null) return;
        this.state = state;
        markDirty();
        if (world != null && !world.isClient()) {
            BlockState currentState = getCachedState();
            if (currentState.contains(MineBlock.STATE)) {
                world.setBlockState(pos, currentState.with(MineBlock.STATE, state), Block.NOTIFY_ALL);
            }
            world.updateListeners(pos, currentState, currentState, Block.NOTIFY_ALL);
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        nbt.putString("state", getState().asString());
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        if (nbt.contains("state")) {
            this.state = MineState.byName(nbt.getString("state"));
        }
    }

    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }
}
