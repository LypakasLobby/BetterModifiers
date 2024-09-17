package com.lypaka.bettermodifiers.Listeners;

import com.lypaka.bettermodifiers.BetterModifiers;
import com.lypaka.bettermodifiers.Modifiers.ModifierHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

@Mod.EventBusSubscriber(modid = BetterModifiers.MOD_ID)
public class ServerStartedListener {

    @SubscribeEvent
    public static void onServerStarted (FMLServerStartedEvent event) throws ObjectMappingException {

        ModifierHandler.loadModifiers();

        MinecraftForge.EVENT_BUS.register(new InteractListener());
        MinecraftForge.EVENT_BUS.register(new ItemListeners());

    }

}
