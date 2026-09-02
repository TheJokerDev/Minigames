package com.j0keer.minigames.client.games;

import com.j0keer.minigames.enums.CookieState;
import com.j0keer.minigames.enums.CookieType;
import com.j0keer.minigames.client.screen.CookieScreen;
import net.minecraft.client.MinecraftClient;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CookieGame {
    public static final Map<CookieType, CookieScreen> screens = new HashMap<>();

    public static void register() {
        //Arrays.stream(CookieType.values()).forEach(type -> screens.put(type, new CookieScreen(type)));
    }

    public static void startGame(String data) {
        CookieState state = CookieState.NORMAL;
        if (data.contains(",")) {
            var split = data.split(",");
            data = split[0];
            try {
                state = CookieState.valueOf(split[1].toUpperCase());
            } catch (IllegalArgumentException ignored) {
            }
        }
        CookieType type;
        try {
            type = CookieType.valueOf(data.toUpperCase());
        } catch (IllegalArgumentException e) {
            type = CookieType.CHORIPAN;
        }
        CookieState finalState = state;
        var screen = screens.computeIfAbsent(type, t -> new CookieScreen(t, finalState));
        if (screen.getState() != finalState) {
            screens.put(type, new CookieScreen(type, finalState));
        }
        CookieType finalType = type;
        MinecraftClient.getInstance().executeSync(() -> {
            if (screen.getState() != finalState) {
                screens.put(finalType, new CookieScreen(finalType, finalState));
            }
            MinecraftClient.getInstance().setScreen(screens.get(finalType));
        });
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
