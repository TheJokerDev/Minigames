package com.j0keer.minigames.registries;

import com.j0keer.minigames.Minigames;

import java.util.EnumMap;
import java.util.Map;

import com.j0keer.minigames.blocks.MarkerBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

public class BlockRegistries {

    public static final Map<DyeColor, MarkerBlock> MARKERS = new EnumMap<>(DyeColor.class);

    static {
        for (DyeColor color : DyeColor.values()) {
            String name = color.getName() + "_marker";
            MarkerBlock standing = registerMarker(name, new MarkerBlock(color, AbstractBlock.Settings.create()
                    .mapColor(MapColor.WHITE)
                    .strength(1.0f)
                    .sounds(BlockSoundGroup.WOOD)));
            MARKERS.put(color, standing);
        }
    }

    private static <T extends Block> T registerBlock(String name, T block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(Minigames.MOD_ID, name), block);
    }

    private static MarkerBlock registerMarker(String name, MarkerBlock block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(Minigames.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, Identifier.of(Minigames.MOD_ID, name), new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks() {
    }
}
