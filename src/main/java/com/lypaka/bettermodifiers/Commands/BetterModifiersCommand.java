package com.lypaka.bettermodifiers.Commands;

import com.lypaka.bettermodifiers.BetterModifiers;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber(modid = BetterModifiers.MOD_ID)
public class BetterModifiersCommand {

    public static final List<String> ALIASES = Arrays.asList("bettermodifiers", "bmods", "modifiers");

    @SubscribeEvent
    public static void onCommandRegistration (RegisterCommandsEvent event) {

        new GiveCommand(event.getDispatcher());
        new ReloadCommand(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());

    }

}
