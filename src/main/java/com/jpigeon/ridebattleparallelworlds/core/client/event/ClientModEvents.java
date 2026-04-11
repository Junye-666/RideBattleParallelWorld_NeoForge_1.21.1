package com.jpigeon.ridebattleparallelworlds.core.client.event;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.block.ModBlockEntities;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.entity.ModEntities;
import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.block.GenericBlockEntityModel;
import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.block.GenericBlockEntityRenderer;
import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.entity.BaseKamenRiderEffectEntity;
import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.entity.RiderEffectModel;
import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.entity.RiderEffectRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import static com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.RiderIds.fromString;

@EventBusSubscriber(modid = RideBattleParallelWorlds.MODID, value = Dist.CLIENT)
public class ClientModEvents {
    @SubscribeEvent
    static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        // 注册SkillProjectile的渲染器，使用原版投掷物渲染器
        event.registerEntityRenderer(
                ModEntities.SKILL_PROJECTILE.get(),
                context -> new ThrownItemRenderer<>(
                        context, 1.0f, true)
        );
        event.registerEntityRenderer(
                ModEntities.DECADE_SPECIAL_EFFECT.get(),
                context -> new RiderEffectRenderer<>(
                        context, generateEffectEntity("decade", "decade_special_effect"))
        );
        event.registerEntityRenderer(
                ModEntities.AGITO_KICK_EFFECT.get(),
                context -> new RiderEffectRenderer<>(
                        context, generateEffectEntity("agito", "agito_kick_effect"))
        );
        event.registerBlockEntityRenderer(
                ModBlockEntities.KUUGA_COFFIN_BE.get(),
                context -> new GenericBlockEntityRenderer(
                        context, generateBlockEntity("kuuga", "kuuga_coffin"))
        );
    }

    // 辅助
    private static RiderEffectModel<BaseKamenRiderEffectEntity> generateEffectEntity(String riderName, String entityName) {
        return new RiderEffectModel<>(
                entityModelPath(riderName, entityName),
                entityTexturePath(riderName, entityName),
                entityAnimationPath(riderName, entityName)
        );
    }

    private static GenericBlockEntityModel generateBlockEntity(String riderName, String entityName) {
        return new GenericBlockEntityModel(
                blockModelPath(riderName, entityName),
                blockTexturePath(riderName, entityName),
                blockAnimationPath(riderName, entityName)
        );
    }

    // 辅助的辅助
    private static ResourceLocation entityModelPath(String riderName, String entityName) {
        return fromString("geo/" + riderName.toLowerCase() + "/entity/" + entityName.toLowerCase() + ".geo.json");
    }

    private static ResourceLocation entityTexturePath(String riderName, String entityName) {
        return fromString("textures/entity/" + riderName.toLowerCase() + "/" + entityName.toLowerCase() + ".png");
    }

    private static ResourceLocation entityAnimationPath(String riderName, String entityName) {
        return fromString("animations/" + riderName.toLowerCase() + "/entity/" + entityName + ".animation.json");
    }

    private static ResourceLocation blockModelPath(String riderName, String blockName) {
        return fromString("geo/" + riderName.toLowerCase() + "/block/" + blockName.toLowerCase() + ".geo.json");
    }

    private static ResourceLocation blockTexturePath(String riderName, String blockName) {
        return fromString("textures/block/" + riderName.toLowerCase() + "/" + blockName.toLowerCase() + ".png");
    }

    private static ResourceLocation blockAnimationPath(String riderName, String blockName) {
        return fromString("animations/" + riderName.toLowerCase() + "/block/" + blockName.toLowerCase() + ".animation.json");
    }
}
