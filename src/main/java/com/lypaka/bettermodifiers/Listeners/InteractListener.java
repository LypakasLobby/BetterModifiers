package com.lypaka.bettermodifiers.Listeners;

import com.lypaka.bettermodifiers.Modifiers.Modifier;
import com.lypaka.bettermodifiers.Modifiers.ModifierHandler;
import com.lypaka.lypakautils.FancyText;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

public class InteractListener {

    @SubscribeEvent
    public void onInteract (PlayerInteractEvent.EntityInteract event) {

        if (event.getSide() == LogicalSide.CLIENT) return;
        if (event.getHand() == Hand.OFF_HAND) return;

        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        if (event.getTarget() instanceof PixelmonEntity) {

            PixelmonEntity pixelmon = (PixelmonEntity) event.getTarget();
            if (pixelmon.hasOwner() && pixelmon.getOwnerUUID().toString().equalsIgnoreCase(player.getUUID().toString())) {

                Pokemon pokemon = pixelmon.getPokemon();
                ItemStack handItem = player.getItemInHand(Hand.MAIN_HAND);
                // A safety check I apparently need to fucking have?
                Modifier modifierCheck = ModifierHandler.getFromPlayerHand(player);
                if (modifierCheck == null) return;
                if (handItem.getOrCreateTag().contains("ModifierItem")) {

                    event.setCanceled(true);
                    String modifierName = handItem.getOrCreateTag().getString("ModifierName");
                    Modifier modifier = ModifierHandler.modifierMap.get(modifierName);

                    if (!ModifierHandler.canUse(player, pokemon, modifier)) {

                        player.sendMessage(FancyText.getFormattedText("&cYou're not allowed to use this modifier!"), player.getUUID());

                    } else {

                        ModifierHandler.processModifier(player, pokemon, modifier);

                    }

                }

            }

        }

    }

}
