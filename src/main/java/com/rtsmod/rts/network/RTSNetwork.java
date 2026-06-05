package com.rtsmod.rts.network;

import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class RTSNetwork {
    private static final String PROTOCOL = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
        new net.minecraft.resources.ResourceLocation("rtsmod", "main"),
        () -> PROTOCOL,
        PROTOCOL::equals,
        PROTOCOL::equals
    );

    private static int id = 0;

    public static void register() {
        // Placeholder: add packet registrations here when adding client-side HUD
    }

    public static int nextId() { return id++; }
}
