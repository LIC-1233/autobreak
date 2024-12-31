package com.LIC.autobreak.client;

import com.LIC.autobreak.AutoBreak;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.logging.LogUtils;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;

@Mod.EventBusSubscriber(modid = AutoBreak.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModHandler {

    private static final Logger LOGGER = LogUtils.getLogger();
    public static final Lazy<KeyMapping> autoBreakKeyMap1 = Lazy.of(() -> new KeyMapping(
            "key.autobreak.key1",
            KeyConflictContext.IN_GAME,
            KeyModifier.SHIFT, // Default mapping is on the keyboard
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_G, // Default mouse input is the left mouse button
            "key.categories.autobreak.autobreakcategory" // Mapping will be in the new example category
    ));

    @SubscribeEvent
    public static void clientSetup(RegisterKeyMappingsEvent event) {
        event.register(autoBreakKeyMap1.get());
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {
        // Some client setup code
        LOGGER.info("HELLO FROM CLIENT SETUP");
        LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }
}
