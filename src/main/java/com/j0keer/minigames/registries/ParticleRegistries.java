package com.j0keer.minigames.registries;

import com.j0keer.minigames.Constants;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ParticleRegistries {
    public static SimpleParticleType DEATH_PARTICLE;

    public static void register() {
        DEATH_PARTICLE = Registry.register(Registries.PARTICLE_TYPE, Identifier.of(Constants.MOD_ID, "death_particle"), FabricParticleTypes.simple(true));
    }
}
