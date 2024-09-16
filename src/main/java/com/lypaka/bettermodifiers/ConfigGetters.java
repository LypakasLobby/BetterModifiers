package com.lypaka.bettermodifiers;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.List;

public class ConfigGetters {

    public static List<String> modifiers;

    public static void load() throws ObjectMappingException {

        modifiers = BetterModifiers.configManager.getConfigNode(0, "Modifiers").getList(TypeToken.of(String.class));

    }

}
