package com.j0keer.minigames;

import com.j0keer.minigames.registries.BlockRegistries;
import com.j0keer.minigames.registries.EntityRegistries;
import com.j0keer.minigames.registries.ItemGroupRegistries;
import com.j0keer.minigames.registries.ItemRegistries;
import net.fabricmc.api.ModInitializer;

public class Minigames implements ModInitializer {
    public static final String MOD_ID = "minigames";

    @Override
    public void onInitialize() {
        BlockRegistries.registerModBlocks();
        ItemRegistries.registerModItems();
        EntityRegistries.registerModEntities();
        ItemGroupRegistries.registerModItemGroups();
    }
}
