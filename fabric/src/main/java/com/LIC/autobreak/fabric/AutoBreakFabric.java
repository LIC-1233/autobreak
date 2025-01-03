package com.LIC.autobreak.fabric;

import com.LIC.autobreak.AutoBreak;
import net.fabricmc.api.ModInitializer;

public final class AutoBreakFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        new AutoBreak();
    }
}
