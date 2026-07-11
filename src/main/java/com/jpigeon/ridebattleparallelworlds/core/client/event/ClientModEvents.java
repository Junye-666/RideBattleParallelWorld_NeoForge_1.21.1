package com.jpigeon.ridebattleparallelworlds.core.client.event;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.block.ModBlockEntities;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.entity.ModEntities;
import com.jpigeon.rideevolutionlib.compat.util.GeoEntityRenderUtil;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = RideBattleParallelWorlds.MODID, value = Dist.CLIENT)
public class ClientModEvents {
    @SubscribeEvent
    static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        // SkillProjectile的渲染器，使用原版投掷物渲染器
        event.registerEntityRenderer(
                ModEntities.SKILL_PROJECTILE.get(),
                context -> new ThrownItemRenderer<>(
                        context, 1.0f, true)
        );
        event.registerEntityRenderer(
                ModEntities.DECADE_SPECIAL_EFFECT.get(),
                context -> GeoEntityRenderUtil.createEffectRenderer(context, RideBattleParallelWorlds.MODID, "decade", "decade_special_effect")
        );
        event.registerEntityRenderer(
                ModEntities.AGITO_KICK_EFFECT.get(),
                context -> GeoEntityRenderUtil.createEffectRenderer(context, RideBattleParallelWorlds.MODID, "agito", "agito_kick_effect")
        );
        event.registerBlockEntityRenderer(
                ModBlockEntities.KUUGA_COFFIN_BE.get(),
                context -> GeoEntityRenderUtil.createBlockRenderer(context, RideBattleParallelWorlds.MODID, "kuuga", "kuuga_coffin")
        );
    }
}
