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
                    .icon(() -> new ItemStack(ItemRegistries.WHITE_MARKER))
                    .displayName(Text.translatable("itemGroup.minigames.minigames_group"))
                    .entries((context, entries) -> {
                        entries.add(ItemRegistries.WHITE_MARKER);
                        entries.add(ItemRegistries.ORANGE_MARKER);
                        entries.add(ItemRegistries.MAGENTA_MARKER);
                        entries.add(ItemRegistries.LIGHT_BLUE_MARKER);
                        entries.add(ItemRegistries.YELLOW_MARKER);
                        entries.add(ItemRegistries.LIME_MARKER);
                        entries.add(ItemRegistries.PINK_MARKER);
                        entries.add(ItemRegistries.GRAY_MARKER);
                        entries.add(ItemRegistries.LIGHT_GRAY_MARKER);
                        entries.add(ItemRegistries.CYAN_MARKER);
                        entries.add(ItemRegistries.PURPLE_MARKER);
                        entries.add(ItemRegistries.BLUE_MARKER);
                        entries.add(ItemRegistries.BROWN_MARKER);
                        entries.add(ItemRegistries.GREEN_MARKER);
                        entries.add(ItemRegistries.RED_MARKER);
                        entries.add(ItemRegistries.BLACK_MARKER);
                    })
                    .build()
    );

    public static void registerModItemGroups() {
    }
}
