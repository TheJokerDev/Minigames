package com.j0keer.minigames.items;

import com.j0keer.minigames.enums.CookieType;
import net.minecraft.item.Item;

public class CookieItem extends Item {
    private final CookieType type;
    private final boolean broken;

    public CookieItem(CookieType type, boolean broken) {
        super(new Settings().maxCount(1));

        this.type = type;
        this.broken = broken;
    }

    public boolean isBroken() {
        return broken;
    }

    public CookieType getType() {
        return type;
    }
}
