package com.j0keer.minigames.networking;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class PacketManager {
    public static void register() {
        PayloadTypeRegistry.playS2C().register(CookieGamePayload.ID, CookieGamePayload.CODEC);
    }
}
