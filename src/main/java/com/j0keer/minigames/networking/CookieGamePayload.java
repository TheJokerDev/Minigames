package com.j0keer.minigames.networking;

import com.j0keer.minigames.Constants;
import com.mojang.serialization.Codec;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record CookieGamePayload(String action, String data) implements CustomPayload {
    public static final Id<CookieGamePayload> ID = new Id<>(Identifier.of(Constants.MOD_ID, "cookie"));

    public static final PacketCodec<RegistryByteBuf, CookieGamePayload> CODEC = CustomPayload.codecOf(
            CookieGamePayload::write, CookieGamePayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return null;
    }

    private CookieGamePayload(PacketByteBuf buf) {
        this(buf.readString(), buf.readString());
    }

    private void write(PacketByteBuf buf) {
        buf.writeString(action);
        buf.writeString(data);
    }
}
