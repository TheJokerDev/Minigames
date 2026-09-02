package com.j0keer.minigames.registries;

import com.j0keer.minigames.Minigames;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ItemGroupRegistries {
    public static final ItemGroup MINESWEEPER = register("minesweeper",
            new ItemStack(ItemRegistries.WHITE_MARKER),
            Items.WOODEN_AXE,
            BlockRegistries.MINE_BLOCK,
            ItemRegistries.WHITE_MARKER,
            ItemRegistries.ORANGE_MARKER,
            ItemRegistries.MAGENTA_MARKER,
            ItemRegistries.LIGHT_BLUE_MARKER,
            ItemRegistries.YELLOW_MARKER,
            ItemRegistries.LIME_MARKER,
            ItemRegistries.PINK_MARKER,
            ItemRegistries.GRAY_MARKER,
            ItemRegistries.LIGHT_GRAY_MARKER,
            ItemRegistries.CYAN_MARKER,
            ItemRegistries.PURPLE_MARKER,
            ItemRegistries.BLUE_MARKER,
            ItemRegistries.BROWN_MARKER,
            ItemRegistries.GREEN_MARKER,
            ItemRegistries.RED_MARKER,
            ItemRegistries.BLACK_MARKER
    );

    public static final ItemGroup COOKIE = register("cookie",
            new ItemStack(ItemRegistries.COOKIE_ITEMS.values().stream().findFirst().get()),
            ItemRegistries.COOKIE_ITEMS.values().toArray(new ItemConvertible[0])
    );


    private static ItemGroup register(String id, ItemStack icon, ItemConvertible... items) {
        return Registry.register(
                Registries.ITEM_GROUP,
                Identifier.of(Minigames.MOD_ID, id),
                FabricItemGroup.builder()
                        .icon(() -> icon)
                        .displayName(Text.translatable("itemGroup.minigames." + id))
                        .entries((context, entries) -> {
                            for (ItemConvertible item : items) {
                                entries.add(item);
                            }
                        })
                        .build()
        );
    }

    public static void registerModItemGroups() {
    }
}
