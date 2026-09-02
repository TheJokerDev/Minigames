package com.j0keer.minigames.registries;

import com.j0keer.minigames.Minigames;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ItemGroupRegistries {
    public static final ItemGroup MINIGAMES_GROUP = Registry.register(
            Registries.ITEM_GROUP,
            Identifier.of(Minigames.MOD_ID, "minigames_group"),
            FabricItemGroup.builder()
                    .icon(() -> new ItemStack(ItemRegistries.MARKER))
                    .displayName(Text.translatable("itemGroup.minigames.minigames_group"))
                    .entries((context, entries) -> {
                        entries.add(ItemRegistries.MARKER);
                    })
                    .build()
    );

    public static void registerModItemGroups() {
    }
}
