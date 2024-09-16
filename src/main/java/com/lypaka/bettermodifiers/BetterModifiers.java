package com.lypaka.bettermodifiers;

import com.lypaka.lypakautils.ConfigurationLoaders.BasicConfigManager;
import com.lypaka.lypakautils.ConfigurationLoaders.ComplexConfigManager;
import com.lypaka.lypakautils.ConfigurationLoaders.ConfigUtils;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("bettermodifiers")
public class BetterModifiers {

    public static final String MOD_ID = "bettermodifiers";
    public static final String MOD_NAME = "BetterModifiers";
    public static Logger logger = LogManager.getLogger(MOD_NAME);
    public static BasicConfigManager configManager;
    public static ComplexConfigManager modifierConfigManager;

    public BetterModifiers() throws IOException, ObjectMappingException {

        Path dir = ConfigUtils.checkDir(Paths.get("./config/bettermodifiers"));
        String[] files = new String[]{"bettermodifiers.conf"};
        configManager = new BasicConfigManager(files, dir, BetterModifiers.class, MOD_NAME, MOD_ID, logger);
        configManager.init();
        ConfigGetters.load();
        modifierConfigManager = new ComplexConfigManager(ConfigGetters.modifiers, "modifiers", "modifierTemplate.conf", dir, BetterModifiers.class, MOD_NAME, MOD_ID, logger);
        modifierConfigManager.init();

    }

}
