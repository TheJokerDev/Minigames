package com.j0keer.minigames.registries;

import com.j0keer.minigames.Minigames;
import com.j0keer.minigames.blockentity.MarkerBlockEntity;
import com.j0keer.minigames.blockentity.MineBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class EntityRegistries {
    public static final BlockEntityType<MineBlockEntity> MINE_BLOCK_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(Minigames.MOD_ID, "mine_block"),
            BlockEntityType.Builder.create(MineBlockEntity::new, BlockRegistries.MINE_BLOCK).build()
    );

    public static final BlockEntityType<MarkerBlockEntity> MARKER = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(Minigames.MOD_ID, "marker"),
            BlockEntityType.Builder.create(
                    MarkerBlockEntity::new,
                    BlockRegistries.WHITE_MARKER,
                    BlockRegistries.ORANGE_MARKER,
                    BlockRegistries.MAGENTA_MARKER,
                    BlockRegistries.LIGHT_BLUE_MARKER,
                    BlockRegistries.YELLOW_MARKER,
                    BlockRegistries.LIME_MARKER,
                    BlockRegistries.PINK_MARKER,
                    BlockRegistries.GRAY_MARKER,
                    BlockRegistries.LIGHT_GRAY_MARKER,
                    BlockRegistries.CYAN_MARKER,
                    BlockRegistries.PURPLE_MARKER,
                    BlockRegistries.BLUE_MARKER,
                    BlockRegistries.BROWN_MARKER,
                    BlockRegistries.GREEN_MARKER,
                    BlockRegistries.RED_MARKER,
                    BlockRegistries.BLACK_MARKER
            ).build()
    );

    public static void registerModEntities() {
    }
}
