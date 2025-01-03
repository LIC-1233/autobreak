package com.LIC.autobreak;

import com.LIC.autobreak.client.AutoBreakClient;
import com.LIC.autobreak.net.AutoBreakNet;
import com.LIC.autobreak.net.KeyPressedPacket;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.utils.EnvExecutor;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

import static com.LIC.autobreak.client.AutoBreakClient.keyBinding;

public final class AutoBreak {
    public static AutoBreak instance;

    public static final String MOD_ID = "autobreak";
    public static final Logger LOGGER = LogManager.getLogger();

    private boolean isBreakingBlock;
    private int tempBlockDroppedXp;
    private ItemCollection tempBlockDropsList;

    public AutoBreak() {
        instance = this;

        EnvExecutor.getEnvSpecific(() -> AutoBreakClient::new, () -> {
            return null;
        });
        AutoBreakNet.init();
        LifecycleEvent.SERVER_BEFORE_START.register(this::serverStarting);
        EntityEvent.ADD.register(this::entityJoinedWorld);
        ClientTickEvent.CLIENT_POST.register(this::startBlockBreak);
    }

    public void startBlockBreak(Minecraft minecraft) {
        if (Minecraft.getInstance().player == null) {
            return;
        }
        while (keyBinding.consumeClick()) {
            new KeyPressedPacket(true).sendToServer();
        }
    }

    public EventResult entityJoinedWorld(Entity entity, Level level) {
        // Other mods may have already intercepted this event to do similar absorption;
        //  the only way to be sure if the entity is still valid is to check if it's alive,
        //  and hope other mods killed the entity if they've absorbed it.
        if (entity.isAlive()) {
            if (isBreakingBlock && entity instanceof ItemEntity item) {
                if (!item.getItem().isEmpty()) {
                    tempBlockDropsList.add(item.getItem());
                    item.setItem(ItemStack.EMPTY);
                }
                return EventResult.interruptFalse();
            } else if (isBreakingBlock && entity instanceof ExperienceOrb orb) {
                tempBlockDroppedXp += orb.getValue();
                entity.kill();
                return EventResult.interruptFalse();
            }
        }
        return EventResult.pass();
    }

    public void setKeyPressed(ServerPlayer player) {
        try (Level world = player.level()) {
            BlockPos playerPos = player.blockPosition();
            ChunkPos chunkPos = new ChunkPos(playerPos);
            TagKey<Block> tag = TagKey.create(Registries.BLOCK,new ResourceLocation("forge:ores"));
            int startX = chunkPos.x * 16;
            int startZ = chunkPos.z * 16;
            isBreakingBlock = true;
            tempBlockDropsList = new ItemCollection();
            tempBlockDroppedXp = 0;
            for (int x = 0; x < 16; x++) {
                for (int y = 0; y < world.getMaxBuildHeight(); y++) {
                    for (int z = 0; z < 16; z++) {
                        BlockPos blockPos = new BlockPos(startX + x, y, startZ + z);
                        BlockState blockState = world.getBlockState(blockPos);
                        // 在这里处理每个方块
                        if (!blockState.isAir()){
                            if (blockState.is(tag)){
                                LOGGER.info("Block at {} is an ore.", blockPos);
                                player.gameMode.destroyBlock(blockPos);
                            }
                        }
                    }
                }
            }
            isBreakingBlock = false;
            tempBlockDropsList.drop(player.level(), playerPos);
            if (tempBlockDroppedXp > 0) {
                player.level().addFreshEntity(new ExperienceOrb(player.level(), playerPos.getX() + 0.5D, playerPos.getY() + 0.5D, playerPos.getZ() + 0.5D, tempBlockDroppedXp));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void serverStarting(MinecraftServer server) {
    }
}
