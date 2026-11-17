package com.jpigeon.ridebattleparallelworlds.client.handler;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.client.render.ClientMirrorColorCache;
import com.jpigeon.ridebattleparallelworlds.common.registry.worldgen.dimension.mirror.MirrorConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@EventBusSubscriber(modid = RideBattleParallelWorlds.MODID, value = Dist.CLIENT)
public class MirrorColorHandler {

    @SubscribeEvent
    public static void onRegisterBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register(new MirrorGrassColor(),
                Blocks.GRASS_BLOCK, Blocks.FERN,
                Blocks.SHORT_GRASS, Blocks.TALL_GRASS, Blocks.LARGE_FERN,
                Blocks.POTTED_FERN,
                Blocks.ACACIA_LEAVES, Blocks.AZALEA_LEAVES, Blocks.BIRCH_LEAVES,
                Blocks.DARK_OAK_LEAVES, Blocks.FLOWERING_AZALEA_LEAVES, Blocks.JUNGLE_LEAVES,
                Blocks.OAK_LEAVES, Blocks.SPRUCE_LEAVES, Blocks.MANGROVE_LEAVES

        );
    }

    private static class MirrorGrassColor implements BlockColor {
        private static final int DEFAULT_GRASS_COLOR = 0x91BD59;

        @Override
        public int getColor(@NotNull BlockState state,
                            @Nullable BlockAndTintGetter getter,
                            @Nullable BlockPos pos,
                            int tintIndex) {
            ClientLevel level = Minecraft.getInstance().level;

            // 镜世界：全用入口处那一个颜色
            if (level != null && MirrorConstants.isMirror(level)) {
                int color = ClientMirrorColorCache.getGrassColor();

                if (color == -1 || (color & 0xFFFFFF) == 0) {
                    return DEFAULT_GRASS_COLOR;
                }

                return color;
            }

            // 主世界：复现原版逻辑
            if (getter != null && pos != null) {
                return BiomeColors.getAverageGrassColor(getter, pos);
            }
            return DEFAULT_GRASS_COLOR;
        }
    }
}