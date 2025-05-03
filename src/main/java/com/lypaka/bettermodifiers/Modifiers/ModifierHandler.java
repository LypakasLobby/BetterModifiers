package com.lypaka.bettermodifiers.Modifiers;

import com.google.common.reflect.TypeToken;
import com.lypaka.bettermodifiers.BetterModifiers;
import com.lypaka.bettermodifiers.ConfigGetters;
import com.lypaka.bettermodifiers.Listeners.ItemListeners;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.MiscHandlers.PermissionHandler;
import com.pixelmonmod.pixelmon.api.battles.attack.AttackRegistry;
import com.pixelmonmod.pixelmon.api.command.PixelmonCommandUtils;
import com.pixelmonmod.pixelmon.api.pokemon.LearnMoveController;
import com.pixelmonmod.pixelmon.api.pokemon.Nature;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.ribbon.RibbonRegistry;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.command.impl.TeachCommand;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TextFormatting;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModifierHandler {

    public static Map<String, Modifier> modifierMap;

    public static Modifier getFromPlayerHand (ServerPlayerEntity player) {

        Modifier modifier = null;
        String id = player.getItemInHand(Hand.MAIN_HAND).getItem().getRegistryName().toString();
        for (Map.Entry<String, Modifier> entry : modifierMap.entrySet()) {

            Modifier m = entry.getValue();
            if (m.getItemID().equalsIgnoreCase(id)) {

                if (player.getItemInHand(Hand.MAIN_HAND).getOrCreateTag().contains("ModifierItem")) {

                    modifier = m;
                    break;

                }

            }

        }

        return modifier;

    }

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

                if (s.contains("form:")) {

                    String form = s.split("form:")[1].replace(" form:", "").replace(" ", "");
                    String pokemonSpecies = s.split("form:")[0].replace(" form:", "").replace(" ", "");
                    if (pokemon.getForm().getName().equalsIgnoreCase(form) && pokemon.getSpecies().getName().equalsIgnoreCase(pokemonSpecies)) {

                        isAllowed = true;
                        break;

                    }

                } else {

                    if (s.equalsIgnoreCase(pokemon.getSpecies().getName())) {

                        isAllowed = true;
                        break;

                    }

                }

            }

        } else {

            isAllowed = true;

        }

        return canUse && isAllowed;

    }

    public static void processModifier (ServerPlayerEntity player, Pokemon pokemon, Modifier modifier) {

        if (ItemListeners.playersDroppedModifier.contains(player.getUUID())) {

            player.sendMessage(FancyText.getFormattedText("&eDetected potentially sketchy stuff from you dropping a modifier while trying to use it, canceling!"), player.getUUID());
            return;

        }
        List<String> functions = modifier.getFunctions();
        for (String f : functions) {

            if (f.contains("teach ")) {

                String[] split = f.split("teach ");
                String move = split[1];
                if (Attack.hasAttack(move)) {

                    Attack attack = new Attack(move);
                    if (pokemon.getMoveset().size() >= 4) {

                        LearnMoveController.sendLearnMove(player, pokemon.getUUID(), attack.getActualMove());

                    } else {

                        pokemon.getMoveset().add(attack);

                    }

                }

            }
            if (f.contains("tag ")) {

                String[] split = f.split("tag ");
                String tag = split[1];
                if (tag.equalsIgnoreCase("rainbow")) {

                    String rainbowTag = "Rainbow" + pokemon.getSpecies().getName();
                    pokemon.getPersistentData().putBoolean(rainbowTag, true);

                }

            }
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
                if (value.equalsIgnoreCase("tradeable")) {

                    pokemon.removeFlag("untradeable");
                    if (pokemon.isUntradeable()) {

                        pokemon.addFlag("tradeable");

                    }

                } else if (value.equalsIgnoreCase("untradeable")) {

                    pokemon.removeFlag("tradeable");
                    pokemon.addFlag("untradeable");

                }
                if (value.equalsIgnoreCase("catchable")) {

                    pokemon.removeFlag("uncatchable");
                    if (pokemon.isUncatchable()) {

                        pokemon.addFlag("catchable");

                    }

                } else if (value.equalsIgnoreCase("uncatchable")) {

                    pokemon.removeFlag("catchable");
                    pokemon.addFlag("uncatchable");

                }
                if (value.equalsIgnoreCase("battleable")) {

                    pokemon.removeFlag("unbattleable");
                    if (pokemon.isUncatchable()) {

                        pokemon.addFlag("battleable");

                    }

                } else if (value.equalsIgnoreCase("unbattleable")) {

                    pokemon.removeFlag("battleable");
                    pokemon.addFlag("unbattleable");

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
            if (f.contains("mark ")) {

                String[] split = f.split("mark ");
                String value = split[1];
                pokemon.addRibbon(RibbonRegistry.getRibbon(value.toLowerCase()).getValueUnsafe());

            }
            if (f.contains("flag ")) {

                String[] split = f.split("mark ");
                String value = split[1];
                pokemon.addFlag(value);

            }

        }

        player.getItemInHand(Hand.MAIN_HAND).setCount(player.getItemInHand(Hand.MAIN_HAND).getCount() - 1);
        player.sendMessage(FancyText.getFormattedText("&aSuccessfully used the modifier on your " + pokemon.getSpecies().getName()), player.getUUID());
        ItemListeners.playersDroppedModifier.removeIf(e -> e.toString().equalsIgnoreCase(player.getUUID().toString()));

    }

}
