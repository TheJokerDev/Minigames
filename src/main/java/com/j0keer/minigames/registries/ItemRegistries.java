package com.j0keer.minigames.registries;

import com.j0keer.minigames.Minigames;
import com.j0keer.minigames.enums.CookieState;
import com.j0keer.minigames.enums.CookieType;
import com.j0keer.minigames.items.CookieBoxItem;
import com.j0keer.minigames.items.CookieItem;
import com.j0keer.minigames.items.MarkerItem;

import java.util.*;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ItemRegistries {
    public static final Item SELECTION_WAND;
    public static final Item WHITE_MARKER;
    public static final Item ORANGE_MARKER;
    public static final Item MAGENTA_MARKER;
    public static final Item LIGHT_BLUE_MARKER;
    public static final Item YELLOW_MARKER;
    public static final Item LIME_MARKER;
    public static final Item PINK_MARKER;
    public static final Item GRAY_MARKER;
    public static final Item LIGHT_GRAY_MARKER;
    public static final Item CYAN_MARKER;
    public static final Item PURPLE_MARKER;
    public static final Item BLUE_MARKER;
    public static final Item BROWN_MARKER;
    public static final Item GREEN_MARKER;
    public static final Item RED_MARKER;
    public static final Item BLACK_MARKER;

    public static final HashMap<String, Item> COOKIE_ITEMS = new HashMap<>();
    public static final Item COOKIE_BOX;

    static {
        SELECTION_WAND = register("selection_wand", new Item((new Item.Settings()).maxCount(1)));
        WHITE_MARKER = register("white_marker", new MarkerItem(BlockRegistries.WHITE_MARKER, (new Item.Settings()).maxCount(16)));
        ORANGE_MARKER = register("orange_marker", new MarkerItem(BlockRegistries.ORANGE_MARKER, (new Item.Settings()).maxCount(16)));
        MAGENTA_MARKER = register("magenta_marker", new MarkerItem(BlockRegistries.MAGENTA_MARKER, (new Item.Settings()).maxCount(16)));
        LIGHT_BLUE_MARKER = register("light_blue_marker", new MarkerItem(BlockRegistries.LIGHT_BLUE_MARKER, (new Item.Settings()).maxCount(16)));
        YELLOW_MARKER = register("yellow_marker", new MarkerItem(BlockRegistries.YELLOW_MARKER, (new Item.Settings()).maxCount(16)));
        LIME_MARKER = register("lime_marker", new MarkerItem(BlockRegistries.LIME_MARKER, (new Item.Settings()).maxCount(16)));
        PINK_MARKER = register("pink_marker", new MarkerItem(BlockRegistries.PINK_MARKER, (new Item.Settings()).maxCount(16)));
        GRAY_MARKER = register("gray_marker", new MarkerItem(BlockRegistries.GRAY_MARKER, (new Item.Settings()).maxCount(16)));
        LIGHT_GRAY_MARKER = register("light_gray_marker", new MarkerItem(BlockRegistries.LIGHT_GRAY_MARKER, (new Item.Settings()).maxCount(16)));
        CYAN_MARKER = register("cyan_marker", new MarkerItem(BlockRegistries.CYAN_MARKER, (new Item.Settings()).maxCount(16)));
        PURPLE_MARKER = register("purple_marker", new MarkerItem(BlockRegistries.PURPLE_MARKER, (new Item.Settings()).maxCount(16)));
        BLUE_MARKER = register("blue_marker", new MarkerItem(BlockRegistries.BLUE_MARKER, (new Item.Settings()).maxCount(16)));
        BROWN_MARKER = register("brown_marker", new MarkerItem(BlockRegistries.BROWN_MARKER, (new Item.Settings()).maxCount(16)));
        GREEN_MARKER = register("green_marker", new MarkerItem(BlockRegistries.GREEN_MARKER, (new Item.Settings()).maxCount(16)));
        RED_MARKER = register("red_marker", new MarkerItem(BlockRegistries.RED_MARKER, (new Item.Settings()).maxCount(16)));
        BLACK_MARKER = register("black_marker", new MarkerItem(BlockRegistries.BLACK_MARKER, (new Item.Settings()).maxCount(16)));

        for (CookieType value : CookieType.values()) {
            for (CookieState state : CookieState.values()) {
                COOKIE_ITEMS.put(value.name().toLowerCase() + "_" + state.name().toLowerCase(), register(value.name().toLowerCase() + "_cookie_" + state.name().toLowerCase(), state == CookieState.COMPLETED ? new CookieItem(value, state, true) : new CookieItem(value, state)));
            }
        }
        COOKIE_BOX = register("cookie_box", new CookieBoxItem());
    }

    private static Item register(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Minigames.MOD_ID, name), item);
    }

    public static void registerModItems() {
    }
}
