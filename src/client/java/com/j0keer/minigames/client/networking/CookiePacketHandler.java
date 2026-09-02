package com.j0keer.minigames.client.networking;

import com.j0keer.minigames.client.games.CookieGame;
import com.j0keer.minigames.networking.CookieGamePayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class CookiePacketHandler {
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(CookieGamePayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                switch (payload.action().toLowerCase()) {
                    case "open" -> {
                        CookieGame.startGame(payload.data());
                    }
                    case "close" -> {
                        context.client().setScreen(null);
                    }
                    case "reset" -> {
                        CookieGame.resetGame(payload.data());
                    }
                }
            });
        });
    }
}
