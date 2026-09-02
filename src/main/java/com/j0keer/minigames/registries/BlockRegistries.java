package com.j0keer.minigames.registries;

import com.j0keer.minigames.Minigames;

import com.j0keer.minigames.blocks.MarkerBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

public class BlockRegistries {

    public static final Block WHITE_MARKER;
    public static final Block ORANGE_MARKER;
    public static final Block MAGENTA_MARKER;
    public static final Block LIGHT_BLUE_MARKER;
    public static final Block YELLOW_MARKER;
    public static final Block LIME_MARKER;
    public static final Block PINK_MARKER;
    public static final Block GRAY_MARKER;
    public static final Block LIGHT_GRAY_MARKER;
    public static final Block CYAN_MARKER;
    public static final Block PURPLE_MARKER;
    public static final Block BLUE_MARKER;
    public static final Block BROWN_MARKER;
    public static final Block GREEN_MARKER;
    public static final Block RED_MARKER;
    public static final Block BLACK_MARKER;

    static {
        WHITE_MARKER = register("white_banner", new MarkerBlock(DyeColor.WHITE, AbstractBlock.Settings.create().mapColor(MapColor.OAK_TAN).solid().instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0F).sounds(BlockSoundGroup.WOOD).burnable()));
        ORANGE_MARKER = register("orange_banner", new MarkerBlock(DyeColor.ORANGE, AbstractBlock.Settings.create().mapColor(MapColor.OAK_TAN).solid().instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0F).sounds(BlockSoundGroup.WOOD).burnable()));
        MAGENTA_MARKER = register("magenta_banner", new MarkerBlock(DyeColor.MAGENTA, AbstractBlock.Settings.create().mapColor(MapColor.OAK_TAN).solid().instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0F).sounds(BlockSoundGroup.WOOD).burnable()));
        LIGHT_BLUE_MARKER = register("light_blue_banner", new MarkerBlock(DyeColor.LIGHT_BLUE, AbstractBlock.Settings.create().mapColor(MapColor.OAK_TAN).solid().instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0F).sounds(BlockSoundGroup.WOOD).burnable()));
        YELLOW_MARKER = register("yellow_banner", new MarkerBlock(DyeColor.YELLOW, AbstractBlock.Settings.create().mapColor(MapColor.OAK_TAN).solid().instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0F).sounds(BlockSoundGroup.WOOD).burnable()));
        LIME_MARKER = register("lime_banner", new MarkerBlock(DyeColor.LIME, AbstractBlock.Settings.create().mapColor(MapColor.OAK_TAN).solid().instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0F).sounds(BlockSoundGroup.WOOD).burnable()));
        PINK_MARKER = register("pink_banner", new MarkerBlock(DyeColor.PINK, AbstractBlock.Settings.create().mapColor(MapColor.OAK_TAN).solid().instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0F).sounds(BlockSoundGroup.WOOD).burnable()));
        GRAY_MARKER = register("gray_banner", new MarkerBlock(DyeColor.GRAY, AbstractBlock.Settings.create().mapColor(MapColor.OAK_TAN).solid().instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0F).sounds(BlockSoundGroup.WOOD).burnable()));
        LIGHT_GRAY_MARKER = register("light_gray_banner", new MarkerBlock(DyeColor.LIGHT_GRAY, AbstractBlock.Settings.create().mapColor(MapColor.OAK_TAN).solid().instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0F).sounds(BlockSoundGroup.WOOD).burnable()));
        CYAN_MARKER = register("cyan_banner", new MarkerBlock(DyeColor.CYAN, AbstractBlock.Settings.create().mapColor(MapColor.OAK_TAN).solid().instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0F).sounds(BlockSoundGroup.WOOD).burnable()));
        PURPLE_MARKER = register("purple_banner", new MarkerBlock(DyeColor.PURPLE, AbstractBlock.Settings.create().mapColor(MapColor.OAK_TAN).solid().instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0F).sounds(BlockSoundGroup.WOOD).burnable()));
        BLUE_MARKER = register("blue_banner", new MarkerBlock(DyeColor.BLUE, AbstractBlock.Settings.create().mapColor(MapColor.OAK_TAN).solid().instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0F).sounds(BlockSoundGroup.WOOD).burnable()));
        BROWN_MARKER = register("brown_banner", new MarkerBlock(DyeColor.BROWN, AbstractBlock.Settings.create().mapColor(MapColor.OAK_TAN).solid().instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0F).sounds(BlockSoundGroup.WOOD).burnable()));
        GREEN_MARKER = register("green_banner", new MarkerBlock(DyeColor.GREEN, AbstractBlock.Settings.create().mapColor(MapColor.OAK_TAN).solid().instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0F).sounds(BlockSoundGroup.WOOD).burnable()));
        RED_MARKER = register("red_banner", new MarkerBlock(DyeColor.RED, AbstractBlock.Settings.create().mapColor(MapColor.OAK_TAN).solid().instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0F).sounds(BlockSoundGroup.WOOD).burnable()));
        BLACK_MARKER = register("black_banner", new MarkerBlock(DyeColor.BLACK, AbstractBlock.Settings.create().mapColor(MapColor.OAK_TAN).solid().instrument(NoteBlockInstrument.BASS).noCollision().strength(1.0F).sounds(BlockSoundGroup.WOOD).burnable()));
    }

    private static <T extends Block> T registerBlock(String name, T block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(Minigames.MOD_ID, name), block);
    }

    private static MarkerBlock register(String name, MarkerBlock block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(Minigames.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, Identifier.of(Minigames.MOD_ID, name), new BlockItem(block, new Item.Settings()));
    }

    public static void registerModBlocks() {
    }
}
