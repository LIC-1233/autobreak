package com.LIC.autobreak.net;

import com.LIC.autobreak.AutoBreak;
import dev.architectury.networking.simple.MessageType;
import dev.architectury.networking.simple.SimpleNetworkManager;

public interface AutoBreakNet {
    SimpleNetworkManager NET = SimpleNetworkManager.create(AutoBreak.MOD_ID);
    MessageType KEY_PRESSED = NET.registerC2S("key_pressed", KeyPressedPacket::new);

    static void init() {
    }
}
