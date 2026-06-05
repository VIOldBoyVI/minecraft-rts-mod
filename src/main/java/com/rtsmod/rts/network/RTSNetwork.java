package com.rtsmod.rts.network;

import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.SimpleChannel;

public class RTSNetwork {
    public static final SimpleChannel CHANNEL = ChannelBuilder
        .named(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("rtsmod", "main"))
        .networkProtocolVersion(1)
        .simpleChannel();

    public static void register() {
        // add packet registrations here when adding client-side HUD
    }
}
