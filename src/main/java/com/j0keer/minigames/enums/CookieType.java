package com.j0keer.minigames.enums;

import net.minecraft.util.Identifier;

public enum CookieType {
    SOCCER_BALL("minigames:textures/cookies/%s/soccer_ball.png"),
    CHORIPAN("minigames:textures/cookies/%s/choripan.png"),
    FERNET("minigames:textures/cookies/%s/fernet.png"),
    MATE("minigames:textures/cookies/%s/mate.png"),;

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
}
