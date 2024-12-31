package com.LIC.autobreak.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class BreakBlockPacket {
    private final BlockPos blockPos;

    public BreakBlockPacket(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public static void encode(BreakBlockPacket packet, FriendlyByteBuf buffer) {
        buffer.writeBlockPos(packet.blockPos);
    }

    public static BreakBlockPacket decode(FriendlyByteBuf buffer) {
        return new BreakBlockPacket(buffer.readBlockPos());
    }

    public static void handle(BreakBlockPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            // 获取服务器玩家
            ServerPlayer player = context.getSender();
            if (player != null) {
                Level level = player.level;
                    BlockPos pos = packet.blockPos;
                    BlockState blockState = level.getBlockState(pos);

                    // 检查方块是否存在
                    if (!blockState.isAir()) {
                        // 使用游戏模式破坏方块
                        boolean destroyed = player.gameMode.destroyBlock(pos);
                        if (destroyed) {
                            // 生成掉落物在玩家位置
                            //Block.dropResources(blockState, level, player.blockPosition(), null, player, player.getMainHandItem());
                        }
                    }

            }
        });
        context.setPacketHandled(true);
    }
}
