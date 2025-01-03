package com.LIC.autobreak.forge;

import com.LIC.autobreak.AutoBreak;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(AutoBreak.MOD_ID)
public final class AutoBreakForge {
    public AutoBreakForge() {
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(AutoBreak.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        // Run our common setup.
        new AutoBreak();
    }
}
