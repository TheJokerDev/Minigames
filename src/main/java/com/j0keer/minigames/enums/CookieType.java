package com.j0keer.minigames.enums;

import com.j0keer.minigames.registries.ItemRegistries;
import net.minecraft.item.ItemConvertible;
import net.minecraft.util.Identifier;

public enum CookieType {
    SOCCER_BALL("minigames:textures/item/soccer_ball_cookie_%s.png"),
    CHORIPAN("minigames:textures/item/choripan_cookie_%s.png"),
    FERNET("minigames:textures/item/fernet_cookie_%s.png"),
    MATE("minigames:textures/item/mate_cookie_%s.png");

    String texture;
    String selected;

    CookieType(String texture) {
        this.texture = texture;
    }

    public CookieType getNormal() {
        this.selected = texture.formatted("normal");
        return this;
    }

    public CookieType getCompleted() {
        this.selected = texture.formatted("completed");
        return this;
    }

    public CookieType getBroken() {
        this.selected = texture.formatted("broken");
        return this;
    }

    public Identifier getID() {
        return Identifier.of(selected);
    }

    public ItemConvertible getCookieItem() {
        return switch (this) {
            case SOCCER_BALL, CHORIPAN, FERNET, MATE -> ItemRegistries.COOKIE_ITEMS.get(this.name().toLowerCase() + "_normal");
        };
    }
}
