package com.j0keer.minigames.items;

import com.j0keer.minigames.enums.CookieState;
import com.j0keer.minigames.enums.CookieType;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.item.Item;

public class CookieItem extends Item {
    private final CookieType type;
    private final CookieState state;

    public CookieItem(CookieType type, CookieState state) {
        super(new Settings().maxCount(1));

        this.type = type;
        this.state = state;
    }

    public CookieItem(CookieType type, CookieState state, boolean food) {
        super(new Settings().maxCount(1).food(FoodComponents.GOLDEN_CARROT));

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
