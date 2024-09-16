package com.lypaka.bettermodifiers.Commands;

import com.lypaka.bettermodifiers.BetterModifiers;
import com.lypaka.bettermodifiers.ConfigGetters;
import com.lypaka.bettermodifiers.Modifiers.ModifierHandler;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.MiscHandlers.PermissionHandler;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class ReloadCommand {

    public ReloadCommand (CommandDispatcher<CommandSource> dispatcher) {

        for (String a : BetterModifiersCommand.ALIASES) {

            dispatcher.register(
                    Commands.literal(a)
                            .then(
                                    Commands.literal("reload")
                                            .executes(c -> {

                                                if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                                    ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                                    if (!PermissionHandler.hasPermission(player, "bettermodifiers.command.admin")) {

                                                        player.sendMessage(FancyText.getFormattedText("&cYou don't have permission to use this command!"), player.getUUID());
                                                        return 0;

                                                    }

                                                }

                                                try {

                                                    BetterModifiers.configManager.load();
                                                    ConfigGetters.load();
                                                    BetterModifiers.modifierConfigManager.setFileNames(ConfigGetters.modifiers);
                                                    BetterModifiers.modifierConfigManager.load();
                                                    ModifierHandler.loadModifiers();
                                                    c.getSource().sendSuccess(FancyText.getFormattedText("&aSuccessfully reloaded BetterModifiers!"), true);

                                                } catch (ObjectMappingException e) {

                                                    throw new RuntimeException(e);

                                                }

                                                return 0;

                                            })
                            )
            );

        }

    }

}
