package com.j0keer.minigames.client;

import com.j0keer.minigames.blockentity.MarkerBlockEntity;
import com.j0keer.minigames.registries.EntityRegistries;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class MinigamesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRendererFactories.register(EntityRegistries.MARKER, MarkerBlockEntityRenderer::new);
    }
}
