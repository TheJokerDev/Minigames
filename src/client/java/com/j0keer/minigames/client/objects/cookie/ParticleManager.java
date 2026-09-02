package com.j0keer.minigames.client.objects.cookie;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ParticleManager {
    private final List<Particle> particles = new ArrayList<>();
    public void addParticle(int x, int y, int color) {
        particles.add(new Particle(x, y, color));
    }

    public void update() {
        Iterator<Particle> iterator = particles.iterator();
        while (iterator.hasNext()) {
            Particle particle = iterator.next();
            particle.update();

            if (particle.isOutOfScreen()) {
                iterator.remove();
            }
        }
    }

    public void render(DrawContext context, float delta) {
        for (Particle particle : particles) {
            particle.render(context, delta);
        }
    }

    private static class Particle {
        private float x;
        private float y;
        private float preX;
        private float preY;
        private final int color;
        private final float velocityX;
        private final float velocityY;
        private int lifetime;
        private final int size;

        public Particle(int x, int y, int color) {
            this.x = x;
            this.y = y;
            preX = x;
            preY = y;
            this.color = color;
            this.velocityX = (new Random().nextFloat() * 2 - 1) * 0.3f;
            this.velocityY = 1.5f + new Random().nextFloat() * 0.5f;
            this.lifetime = 60 + new Random().nextInt(20);
            this.size = ThreadLocalRandom.current().nextInt(1, 6);
        }

        public void update() {
            preX = x;
            preY = y;
            x += velocityX;
            y += velocityY;
            lifetime--;
        }

        public boolean isOutOfScreen() {
            return y > 480 || lifetime <= 0;
        }

        public void render(DrawContext context, float delta) {
            int drawX = (int) MathHelper.lerp(delta, preX, x);
            int drawY = (int) MathHelper.lerp(delta, preY, y);
            context.fill(drawX, drawY, drawX + size, drawY + size, color);
        }
    }
}
