package com.j0keer.minigames.registries;

import com.j0keer.minigames.Minigames;
import com.j0keer.minigames.blockentity.MarkerBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class EntityRegistries {
    public static final BlockEntityType<MarkerBlockEntity> MARKER = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(Minigames.MOD_ID, "marker"),
            BlockEntityType.Builder.create(MarkerBlockEntity::new, BlockRegistries.WHITE_MARKER).build()
    );

    public static void registerModEntities() {
    }
}
