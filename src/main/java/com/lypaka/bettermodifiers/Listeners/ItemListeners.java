package com.lypaka.bettermodifiers.Listeners;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemListeners {

    public static List<UUID> playersDroppedModifier = new ArrayList<>();

    @SubscribeEvent
    public void onDrop (ItemTossEvent event) {

        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        ItemStack item = event.getEntityItem().getItem();
        if (item.getOrCreateTag().contains("ModifierItem")) {

            playersDroppedModifier.add(player.getUUID());

        }

    }

    @SubscribeEvent
    public void onPickup (PlayerEvent.ItemPickupEvent event) {

        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        if (event.getStack().getOrCreateTag().contains("ModifierItem")) {

            playersDroppedModifier.removeIf(e -> e.toString().equalsIgnoreCase(player.getUUID().toString()));

        }

    }

}
