package com.rtsmod.rts;

import com.mojang.logging.LogUtils;
import com.rtsmod.rts.commands.RTSCommands;
import com.rtsmod.rts.network.RTSNetwork;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

@Mod(RTSMod.MOD_ID)
public class RTSMod {
    public static final String MOD_ID = "rtsmod";
    public static final Logger LOGGER = LogUtils.getLogger();

    public RTSMod(IEventBus modBus) {
        modBus.addListener(this::onCommonSetup);
        modBus.addListener(this::onClientSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        RTSNetwork.register();
        LOGGER.info("RTS Mod: network channels registered");
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        LOGGER.info("RTS Mod: client setup complete");
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        RTSCommands.register(event.getDispatcher());
    }
}
