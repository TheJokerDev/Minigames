package com.j0keer.minigames.items;

import com.j0keer.minigames.enums.CookieState;
import com.j0keer.minigames.enums.CookieType;
import com.j0keer.minigames.networking.CookieGamePayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class CookieItem extends Item {
    private final CookieType type;
    private final CookieState state;

    public CookieItem(CookieType type, CookieState state) {
        super(new Settings().maxCount(1));

        this.type = type;
        this.state = state;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        if (state != CookieState.COMPLETED && user instanceof ServerPlayerEntity player) {
            ServerPlayNetworking.send(player, new CookieGamePayload("open", type.name() + "," + state.name()));
        }

        return super.use(world, user, hand);
    }

    public CookieItem(CookieType type, CookieState state, boolean food) {
        super(new Settings().maxCount(1).food(FoodComponents.ENCHANTED_GOLDEN_APPLE));

        this.type = type;
        this.state = state;
    }

    public CookieState getState() {
        return state;
    }

    public CookieType getType() {
        return type;
    }
}
