package com.j0keer.minigames.items;

import com.j0keer.minigames.enums.CookieType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class CookieBoxItem extends Item {
    public CookieBoxItem() {
        super(new Settings().maxCount(1));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        var type = CookieType.valueOf(CookieType.values()[(int) (Math.random() * CookieType.values().length)].name());
        user.getInventory().offerOrDrop(new ItemStack(type.getCookieItem()));
        user.getInventory().removeOne(user.getStackInHand(hand));
        return super.use(world, user, hand);
    }
}
