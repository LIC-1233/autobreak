package com.LIC.autobreak.client;

import com.LIC.autobreak.AutoBreak;
import com.LIC.autobreak.network.BreakBlockPacket;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.Tags;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

import static com.LIC.autobreak.client.ClientModHandler.autoBreakKeyMap1;

@Mod.EventBusSubscriber(modid = AutoBreak.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeHandler {

    private static final Logger LOGGER = LogUtils.getLogger();

//    @SubscribeEvent
//    public static void onClientTick(TickEvent.ClientTickEvent event) {
//        if (event.phase == TickEvent.Phase.END) { // Only call code once as the tick event is called twice every tick
//            if (autoBreakKeyMap1.get().isDown()) {
//                autoBreakKeyMap1.get().consumeClick();
//                LOGGER.info("consumeClick Key was pressed");
//            }
//        }
//    }

    @SubscribeEvent
    public static void KeyInputs(InputEvent.Key event) {
        Minecraft MINECRAFT = Minecraft.getInstance();
        if (autoBreakKeyMap1.get().isDown() && MINECRAFT.isWindowActive()){
            if (MINECRAFT.player != null) {
                LOGGER.info("Player chunk Position: {}", MINECRAFT.player.chunkPosition());
                // 获取玩家所在的区块坐标
                BlockPos playerPos = MINECRAFT.player.blockPosition();
                ChunkPos chunkPos = new ChunkPos(playerPos);

                // 获取区块
                ClientLevel world = MINECRAFT.level;
                if (world != null) {
                    ChunkAccess chunk = world.getChunk(chunkPos.x, chunkPos.z);

                // 遍历区块中的所有方块
                for (int x = 0; x < 16; x++) {
                    for (int y = -66; y < 256; y++) { // 假设遍历整个Y轴范围
                        for (int z = 0; z < 16; z++) {
                            BlockPos blockPos = new BlockPos(chunkPos.x * 16 + x, y, chunkPos.z * 16 + z);
                            BlockState blockState = chunk.getBlockState(blockPos);

                            // 检查方块是否包含 forge:ores 标签
                            if (blockState.is(Tags.Blocks.ORES)) {
                                LOGGER.info("Found ore at: {}", blockPos);

                                // 发送网络消息到服务器
                                AutoBreak.sendToServer(new BreakBlockPacket(blockPos));
                            }
                        }
                    }
                }
                }

            }
        }
    }
}
