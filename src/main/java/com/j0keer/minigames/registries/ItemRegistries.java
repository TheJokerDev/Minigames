package com.j0keer.minigames.registries;

import com.j0keer.minigames.Minigames;
import com.j0keer.minigames.items.MarkerItem;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

public class ItemRegistries {
    public static final Map<DyeColor, Item> MARKERS = new EnumMap<>(DyeColor.class);
    public static final Item MARKER = registerItem("white_marker", new MarkerItem(DyeColor.WHITE, new Item.Settings().maxCount(16)));

    static {
        MARKERS.put(DyeColor.WHITE, MARKER);
        for (DyeColor color : DyeColor.values()) {
            if (color == DyeColor.WHITE) {
                continue;
            }
            MARKERS.put(color, registerItem(color.getName() + "_marker", new MarkerItem(color, new Item.Settings().maxCount(16))));
        }
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Minigames.MOD_ID, name), item);
    }

    public static void registerModItems() {
    }
}
