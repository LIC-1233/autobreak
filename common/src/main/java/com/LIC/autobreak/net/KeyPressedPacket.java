package com.LIC.autobreak.net;

import com.LIC.autobreak.AutoBreak;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class KeyPressedPacket extends BaseC2SMessage {
    private final boolean pressed;

    public KeyPressedPacket(boolean p) {
        pressed = p;
    }
    public KeyPressedPacket(FriendlyByteBuf buf) {
        pressed = buf.readBoolean();
    }
    public void write(FriendlyByteBuf buf) {
        buf.writeBoolean(pressed);
    }
    @Override
    public MessageType getType() {
        return AutoBreakNet.KEY_PRESSED;
    }
    @Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> AutoBreak.instance.BreakKeyPressed((ServerPlayer) context.getPlayer()));
        AutoBreak.LOGGER.info("handled");
    }
}
