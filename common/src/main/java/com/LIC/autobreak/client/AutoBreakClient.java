package com.LIC.autobreak.client;

import com.LIC.autobreak.AutoBreak;
import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.minecraft.client.KeyMapping;

public class AutoBreakClient {
    public static KeyMapping keyBinding;

    public AutoBreakClient() {
        AutoBreak.LOGGER.info("KeyBinding");

        KeyMappingRegistry.register(keyBinding = new KeyMapping(
                "key.autobreak.key1",
                InputConstants.Type.KEYSYM,
                InputConstants.KEY_G,
                "category.autobreak.category1"
        ));
    }
}
