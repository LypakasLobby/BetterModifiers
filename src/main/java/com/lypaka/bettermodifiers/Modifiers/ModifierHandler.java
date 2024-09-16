package com.lypaka.bettermodifiers.Modifiers;

import com.google.common.reflect.TypeToken;
import com.lypaka.bettermodifiers.BetterModifiers;
import com.lypaka.bettermodifiers.ConfigGetters;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.MiscHandlers.PermissionHandler;
import com.pixelmonmod.pixelmon.api.pokemon.Nature;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Hand;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModifierHandler {

    public static Map<String, Modifier> modifierMap;

    public static void loadModifiers() throws ObjectMappingException {

        modifierMap = new HashMap<>();
        for (int i = 0; i < ConfigGetters.modifiers.size(); i++) {

            String fileName = ConfigGetters.modifiers.get(i);
            List<String> species = BetterModifiers.modifierConfigManager.getConfigNode(i, "Allowed-Species").getList(TypeToken.of(String.class));
            String itemID = BetterModifiers.modifierConfigManager.getConfigNode(i, "Item", "Display-ID").getString();
            String itemName = BetterModifiers.modifierConfigManager.getConfigNode(i, "Item", "Display-Name").getString();
            List<String> itemLore = BetterModifiers.modifierConfigManager.getConfigNode(i, "Item", "Lore").getList(TypeToken.of(String.class));
            List<String> functions = BetterModifiers.modifierConfigManager.getConfigNode(i, "Functions").getList(TypeToken.of(String.class));
            List<String> permissions = BetterModifiers.modifierConfigManager.getConfigNode(i, "Permissions").getList(TypeToken.of(String.class));

            Modifier modifier = new Modifier(fileName, species, itemID, itemName, itemLore, functions, permissions);
            modifierMap.put(fileName, modifier);

        }

    }

    public static boolean canUse (ServerPlayerEntity player, Pokemon pokemon, Modifier modifier) {

        boolean canUse = true;
        List<String> permissions = modifier.getPermissions();
        for (String p : permissions) {

            if (!PermissionHandler.hasPermission(player, p)) {

                canUse = false;
                break;

            }

        }

        boolean isAllowed = false;
        if (!modifier.allowsAnySpecies()) {

            List<String> species = modifier.getSpecies();
            for (String s : species) {

                if (s.equalsIgnoreCase(pokemon.getSpecies().getName())) {

                    isAllowed = true;
                    break;

                }

            }

        } else {

            isAllowed = true;

        }

        return canUse && isAllowed;

    }

    public static void processModifier (ServerPlayerEntity player, Pokemon pokemon, Modifier modifier) {

        List<String> functions = modifier.getFunctions();
        for (String f : functions) {

            if (f.contains("apply ")) {

                String[] split = f.split("apply ");
                String texture = split[1];
                pokemon.setPalette(texture);

            }
            if (f.contains("make ")) {

                String[] split = f.split("make ");
                String value = split[1];
                if (value.equalsIgnoreCase("shiny")) {

                    pokemon.setShiny(true);

                } else if (value.equalsIgnoreCase("unshiny")) {

                    pokemon.setShiny(false);

                }

            }
            if (f.contains("growth ")) {

                String[] split = f.split("growth ");
                String value = split[1];
                pokemon.setGrowth(EnumGrowth.getGrowthFromString(value));

            }
            if (f.contains("nature ")) {

                String[] split = f.split("nature ");
                String value = split[1];
                pokemon.setNature(Nature.natureFromString(value));

            }
            if (f.contains("level ")) {

                String[] split = f.split("level ");
                int value = Integer.parseInt(split[1]);
                pokemon.setLevel(value);
                pokemon.setLevelNum(value);

            }
            if (f.contains("form ")) {

                String[] split = f.split("form ");
                String form = split[1];
                pokemon.setForm(form);

            }

        }

        player.getItemInHand(Hand.MAIN_HAND).setCount(player.getItemInHand(Hand.MAIN_HAND).getCount() - 1);
        player.sendMessage(FancyText.getFormattedText("&aSuccessfully used the modifier on your " + pokemon.getSpecies().getName()), player.getUUID());

    }

}
