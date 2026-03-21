package com.jpigeon.ridebattleparallelworlds;

import com.jpigeon.ridebattleparallelworlds.core.block.ModBlockEntities;
import com.jpigeon.ridebattleparallelworlds.core.entity.ModEntities;
import com.jpigeon.ridebattleparallelworlds.core.extra.shocker.ShockerConfig;
import com.jpigeon.ridebattleparallelworlds.core.riders.RiderSkills;
import com.jpigeon.ridebattleparallelworlds.core.riders.agito.AgitoConfig;
import com.jpigeon.ridebattleparallelworlds.core.riders.decade.DecadeConfig;
import com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.KuugaConfig;
import com.jpigeon.ridebattleparallelworlds.core.sound.ModSounds;
import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.block.GenericBlockEntityModel;
import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.block.GenericBlockEntityRenderer;
import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.entity.RiderEffectModel;
import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.entity.RiderEffectRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

import static com.jpigeon.ridebattleparallelworlds.core.riders.RiderIds.fromString;

@Mod(value = RideBattleParallelWorlds.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = RideBattleParallelWorlds.MODID, value = Dist.CLIENT)
public class RideBattleParallelWorldsClient {
    public RideBattleParallelWorldsClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        RiderSkills.init();
        KuugaConfig.init();
        AgitoConfig.init();
        DecadeConfig.init();
        ShockerConfig.init();

        ModSounds.registerFormSoundMap();
    }

    @SubscribeEvent
    static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        // 注册SkillProjectile的渲染器，使用原版投掷物渲染器
        event.registerEntityRenderer(ModEntities.SKILL_PROJECTILE.get(),
                context -> new ThrownItemRenderer<>(context, 1.0f, true));
        event.registerEntityRenderer(
                ModEntities.DECADE_SPECIAL_EFFECT.get(),
                context -> new RiderEffectRenderer<>(
                        context,
                        new RiderEffectModel<>(
                                generateEntityModelPath("decade", "decade_special_effect"),
                                generateEntityTexturePath("decade", "decade_special_effect"),
                                generateEntityAnimationPath("decade", "decade_special_effect")
                        )
                )
        );
        event.registerEntityRenderer(
                ModEntities.AGITO_KICK_EFFECT.get(),
                context -> new RiderEffectRenderer<>(
                        context,
                        new RiderEffectModel<>(
                                generateEntityModelPath("agito", "agito_kick_effect"),
                                generateEntityTexturePath("agito", "agito_kick_effect"),
                                generateEntityAnimationPath("agito", "agito_kick_effect")
                        )
                )
        );
        event.registerBlockEntityRenderer(
                ModBlockEntities.KUUGA_COFFIN_BE.get(),
                context -> new GenericBlockEntityRenderer(
                        context,
                        new GenericBlockEntityModel(
                                generateBlockModelPath("kuuga", "kuuga_coffin"),
                                generateBlockTexturePath("kuuga", "kuuga_coffin"),
                                generateBlockAnimationPath("kuuga", "kuuga_coffin")
                        )

                )
        );
    }

    private static ResourceLocation generateEntityModelPath(String riderName, String entityName) {
        return fromString("geo/" + riderName.toLowerCase() + "/entity/" + entityName.toLowerCase() + ".geo.json");
    }

    private static ResourceLocation generateEntityTexturePath(String riderName, String entityName) {
        return fromString("textures/entity/" + riderName.toLowerCase() + "/" + entityName.toLowerCase() + ".png");
    }

    private static ResourceLocation generateEntityAnimationPath(String riderName, String entityName) {
        return fromString("animations/" + riderName.toLowerCase() + "/entity/" + entityName + ".animation.json");
    }

    private static ResourceLocation generateBlockModelPath(String riderName, String blockName) {
        return fromString("geo/" + riderName.toLowerCase() + "/block/" + blockName.toLowerCase() + ".geo.json");
    }

    private static ResourceLocation generateBlockTexturePath(String riderName, String blockName) {
        return fromString("textures/block/" + riderName.toLowerCase() + "/" + blockName.toLowerCase() + ".png");
    }

    private static ResourceLocation generateBlockAnimationPath(String riderName, String blockName) {
        return fromString("animations/" + riderName.toLowerCase() + "/block/" + blockName.toLowerCase() + ".animation.json");
    }
}
