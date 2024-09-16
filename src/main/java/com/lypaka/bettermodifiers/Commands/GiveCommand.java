package com.lypaka.bettermodifiers.Commands;

import com.lypaka.bettermodifiers.ConfigGetters;
import com.lypaka.bettermodifiers.Modifiers.Modifier;
import com.lypaka.bettermodifiers.Modifiers.ModifierHandler;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.MiscHandlers.PermissionHandler;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;

public class GiveCommand {

    public GiveCommand (CommandDispatcher<CommandSource> dispatcher) {

        for (String a : BetterModifiersCommand.ALIASES) {

            dispatcher.register(
                    Commands.literal(a)
                            .then(
                                    Commands.literal("give")
                                            .then(
                                                    Commands.argument("player", EntityArgument.players())
                                                            .then(
                                                                    Commands.argument("modifier", StringArgumentType.word())
                                                                            .suggests(
                                                                                    (context, builder) -> ISuggestionProvider.suggest(ConfigGetters.modifiers, builder)
                                                                            )
                                                                            .then(
                                                                                    Commands.argument("amount", IntegerArgumentType.integer(1))
                                                                                            .executes(c -> {

                                                                                                if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                                                                                    ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                                                                                    if (!PermissionHandler.hasPermission(player, "bettermodifiers.command.admin")) {

                                                                                                        player.sendMessage(FancyText.getFormattedText("&cYou don't have permission to use this command!"), player.getUUID());
                                                                                                        return 0;

                                                                                                    }

                                                                                                }

                                                                                                ServerPlayerEntity target = EntityArgument.getPlayer(c, "player");
                                                                                                Modifier modifier = ModifierHandler.modifierMap.get(StringArgumentType.getString(c, "modifier"));
                                                                                                int amount = IntegerArgumentType.getInteger(c, "amount");

                                                                                                target.addItem(modifier.getItem(amount));
                                                                                                c.getSource().sendSuccess(FancyText.getFormattedText("&aSuccessfully gave " + target.getName().getString() + " " + amount + " of Modifier: " + modifier.getName().replace(".conf", "")), true);
                                                                                                return 1;

                                                                                            })
                                                                            )
                                                            )
                                            )
                            )
            );

        }

    }

}
