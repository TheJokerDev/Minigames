package com.j0keer.minigames.networking.handlers;

import com.j0keer.minigames.enums.CookieType;
import com.j0keer.minigames.items.CookieItem;
import com.j0keer.minigames.networking.CookieGamePayload;
import com.j0keer.minigames.registries.ItemRegistries;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

public class CookieGameServerHandler {
    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(CookieGamePayload.ID, (payload, context) -> {
            context.server().execute(() -> {
                CookieType type = CookieType.valueOf(payload.data().toUpperCase());
                var action = payload.action().toLowerCase();
                switch (action) {
                    case "broken", "completed" -> handlePacket(context.player(), type, action.equals("completed"));
                }
            });
        });
    }

    private static void handlePacket(ServerPlayerEntity player, CookieType type, boolean completed) {
        var replacement = ItemRegistries.COOKIE_ITEMS.get(type.name().toLowerCase() + "_" + (completed ? "completed" : "broken"));
        for (int slot = 0; slot < player.getInventory().size(); slot++) {
            ItemStack stack = player.getInventory().getStack(slot);
            if (stack.getItem() instanceof CookieItem cookieItem && cookieItem.getType() == type && cookieItem.getState() != (completed ? com.j0keer.minigames.enums.CookieState.COMPLETED : com.j0keer.minigames.enums.CookieState.BROKEN)) {
                player.getInventory().setStack(slot, new ItemStack(replacement, stack.getCount()));
            }
        }
        player.playerScreenHandler.sendContentUpdates();
    }
}
