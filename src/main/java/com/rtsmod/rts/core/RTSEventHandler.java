package com.rtsmod.rts.core;

import com.rtsmod.rts.RTSMod;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RTSMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RTSEventHandler {

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            RTSPlayer.remove(player.getUUID());
        }
    }
}
