package com.jpigeon.ridebattleparallelworlds;

import com.jpigeon.ridebattleparallelworlds.core.entity.ModEntities;
import com.jpigeon.ridebattleparallelworlds.core.extra.shocker.ShockerConfig;
import com.jpigeon.ridebattleparallelworlds.core.riders.RiderSkills;
import com.jpigeon.ridebattleparallelworlds.core.riders.agito.AgitoConfig;
import com.jpigeon.ridebattleparallelworlds.core.riders.decade.DecadeConfig;
import com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.KuugaConfig;
import com.jpigeon.ridebattleparallelworlds.core.sound.ModSounds;
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
                                generateModelPath("decade", "decade_special_effect"),
                                generateTexturePath("decade", "decade_special_effect"),
                                generateAnimationPath("decade", "decade_special_effect")
                        )
                )
        );
        event.registerEntityRenderer(
                ModEntities.AGITO_KICK_EFFECT.get(),
                context -> new RiderEffectRenderer<>(
                        context,
                        new RiderEffectModel<>(
                                generateModelPath("agito", "agito_kick_effect"),
                                generateTexturePath("agito", "agito_kick_effect"),
                                generateAnimationPath("agito", "agito_kick_effect")
                        )
                )

        );
    }

    private static ResourceLocation generateModelPath(String riderName, String entityName) {
        return fromString("geo/" + riderName.toLowerCase() + "/entity/" + entityName.toLowerCase() + ".geo.json");
    }
    private static ResourceLocation generateTexturePath(String riderName, String entityName) {
        return fromString("textures/entity/" + riderName.toLowerCase() + "/" + entityName.toLowerCase() +".png");
    }
    private static ResourceLocation generateAnimationPath(String riderName, String entityName) {
        return fromString("animations/" + riderName.toLowerCase() + "/entity/" + entityName + ".animation.json");
    }
}
