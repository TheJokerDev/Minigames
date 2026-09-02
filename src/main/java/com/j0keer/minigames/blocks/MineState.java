package com.j0keer.minigames.blocks;

import net.minecraft.text.Text;
import net.minecraft.util.StringIdentifiable;

public enum MineState implements StringIdentifiable {
    HIDDEN("hidden", null),
    FREE("free", "§a❤"),
    EXPLODED("exploded", "§c☠"),
    ONE("one", "§e1"),
    TWO("two", "§e2"),
    THREE("three", "§63"),
    FOUR("four", "§64"),
    FIVE("five", "§c5"),
    SIX("six", "§c6"),
    SEVEN("seven", "§47"),
    EIGHT("eight", "§48");

    private final String name;
    private final Text displayText;

    MineState(String name, String displayText) {
        this.name = name;
        this.displayText = displayText == null ? null : Text.literal(displayText);
    }

    @Override
    public String asString() {
        return this.name;
    }

    public Text getDisplayText() {
        return this.displayText;
    }

    public static MineState byName(String name) {
        if (name == null) return HIDDEN;
        for (MineState state : values()) {
            if (state.name.equalsIgnoreCase(name)) return state;
        }
        return HIDDEN;
    }

    public static MineState fromNumber(int count) {
        if (count <= 0) return FREE;
        if (count == 1) return ONE;
        if (count == 2) return TWO;
        if (count == 3) return THREE;
        if (count == 4) return FOUR;
        if (count == 5) return FIVE;
        if (count == 6) return SIX;
        if (count == 7) return SEVEN;
        return EIGHT;
    }
}
