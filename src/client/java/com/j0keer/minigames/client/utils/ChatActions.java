package com.j0keer.minigames.client.utils;

import com.j0keer.minigames.registries.ParticleRegistries;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class ChatActions {

    public static boolean onChat(@NotNull String text) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (text.startsWith("[death]")){
            String name = text.replace("[death]", "");

            HashMap<String, AbstractClientPlayerEntity> players = new HashMap<>();
            assert mc.world != null;
            mc.world.getPlayers().forEach(p -> players.put(p.getName().getString(), p));
            if (!players.containsKey(name)) return true;
            AbstractClientPlayerEntity player = players.get(name);
            if (name.isEmpty() || player == null) return true;
            int times = 20;
            for (int i = 0; i<times; i++) {
                mc.particleManager.addEmitter(player, ParticleRegistries.DEATH_PARTICLE, 1);
            }
            return true;
        }
        return false;
    }
}
