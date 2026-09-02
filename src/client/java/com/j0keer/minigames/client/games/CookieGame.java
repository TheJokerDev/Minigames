package com.j0keer.minigames.client.games;

import com.j0keer.minigames.enums.CookieType;
import com.j0keer.minigames.client.screen.CookieScreen;
import net.minecraft.client.MinecraftClient;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CookieGame {
    public static final Map<CookieType, CookieScreen> screens = new HashMap<>();

    public static void register() {
        Arrays.stream(CookieType.values()).forEach(type -> screens.put(type, new CookieScreen(type)));
    }

    public static void startGame(String data) {
        CookieType type;
        try {
            type = CookieType.valueOf(data.toUpperCase());
        } catch (IllegalArgumentException e) {
            type = CookieType.CHORIPAN;
        }
        var screen = screens.computeIfAbsent(type, CookieScreen::new);
        MinecraftClient.getInstance().execute(() -> MinecraftClient.getInstance().setScreen(screen));
    }

    public static void resetGame(String data) {
        CookieType type;
        try {
            type = CookieType.valueOf(data.toUpperCase());
        } catch (IllegalArgumentException e) {
            type = CookieType.CHORIPAN;
        }
        screens.put(type, new CookieScreen(type));
    }
}
