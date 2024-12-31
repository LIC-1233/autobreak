package com.LIC.autobreak.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import org.lwjgl.glfw.GLFW;

public class ModKeyBindings {
    public static final KeyMapping autoBreakKeyMap1 = new KeyMapping(
            "key.autobreak.key1",
            KeyConflictContext.IN_GAME,
            KeyModifier.SHIFT, // Default mapping is on the keyboard
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_G, // Default mouse input is the left mouse button
            "key.categories.autobreak.autobreakcategory" // Mapping will be in the new example category
    );
}
