package com.j0keer.minigames.networking;

import com.j0keer.minigames.networking.handlers.CookieGameServerHandler;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class PacketManager {
    public static void register() {
        PayloadTypeRegistry.playS2C().register(CookieGamePayload.ID, CookieGamePayload.CODEC);
        PayloadTypeRegistry.playC2S().register(CookieGamePayload.ID, CookieGamePayload.CODEC);

        CookieGameServerHandler.register();
    }
}
