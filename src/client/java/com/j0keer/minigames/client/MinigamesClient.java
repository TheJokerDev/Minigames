package com.j0keer.minigames.client;

import com.j0keer.minigames.registries.BlockRegistries;
import com.j0keer.minigames.registries.EntityRegistries;
import com.j0keer.minigames.registries.ItemRegistries;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.item.Item;
import net.minecraft.world.biome.GrassColors;

public class MinigamesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRendererFactories.register(EntityRegistries.MARKER, MarkerBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(EntityRegistries.MINE_BLOCK_ENTITY, MineBlockEntityRenderer::new);

        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistries.MINE_BLOCK, RenderLayer.getCutoutMipped());

        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
            if (tintIndex == 0) return world != null && pos != null ? BiomeColors.getGrassColor(world, pos) : GrassColors.getDefaultColor();
            return -1;
        }, BlockRegistries.MINE_BLOCK);

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            if (tintIndex == 0) return GrassColors.getDefaultColor();
            return -1;
        }, BlockRegistries.MINE_BLOCK);

        MarkerItemRenderer markerItemRenderer = new MarkerItemRenderer();
        Item[] markers = {
                ItemRegistries.WHITE_MARKER, ItemRegistries.ORANGE_MARKER, ItemRegistries.MAGENTA_MARKER,
                ItemRegistries.LIGHT_BLUE_MARKER, ItemRegistries.YELLOW_MARKER, ItemRegistries.LIME_MARKER,
                ItemRegistries.PINK_MARKER, ItemRegistries.GRAY_MARKER, ItemRegistries.LIGHT_GRAY_MARKER,
                ItemRegistries.CYAN_MARKER, ItemRegistries.PURPLE_MARKER, ItemRegistries.BLUE_MARKER,
                ItemRegistries.BROWN_MARKER, ItemRegistries.GREEN_MARKER, ItemRegistries.RED_MARKER,
                ItemRegistries.BLACK_MARKER
        };
        for (Item marker : markers) BuiltinItemRendererRegistry.INSTANCE.register(marker, markerItemRenderer);
    }
}
