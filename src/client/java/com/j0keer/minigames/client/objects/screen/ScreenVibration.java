package com.j0keer.minigames.client.objects.screen;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ScreenVibration {
    private float strength;
    private int duration;
    private int currentTick;

    public ScreenVibration() {
        this.strength = 0;
        this.duration = 0;
        this.currentTick = 0;
    }

    public void startVibration(float strength, int duration) {
        this.strength = strength;
        this.duration = duration;
        this.currentTick = 0;
    }

    public void update() {
        if (currentTick < duration) {
            currentTick++;
        }
    }

    public float getX() {
        if (currentTick >= duration) {
            return 0;
        }
        return (ThreadLocalRandom.current().nextFloat() * 2 - 1) * strength;
    }

    public float getY() {
        if (currentTick >= duration) {
            return 0;
        }
        return (ThreadLocalRandom.current().nextFloat() * 2 - 1) * strength;
    }
}
