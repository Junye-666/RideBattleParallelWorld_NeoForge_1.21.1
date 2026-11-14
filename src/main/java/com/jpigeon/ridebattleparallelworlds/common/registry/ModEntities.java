package com.jpigeon.ridebattleparallelworlds.common.registry;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.common.entity.SkillProjectile;
import com.jpigeon.ridebattleparallelworlds.common.rider.agito.entity.AgitoKickEffect;
import com.jpigeon.ridebattleparallelworlds.common.rider.decade.entity.DecadeHenshinEffect;
import com.jpigeon.ridebattleparallelworlds.common.rider.ryuki.entity.RyukiHenshinEffect;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, RideBattleParallelWorlds.MODID);

    public static final Supplier<EntityType<SkillProjectile>> SKILL_PROJECTILE =
            ENTITY_TYPES.register("skill_projectile",
                    () -> EntityType.Builder.<SkillProjectile>of(SkillProjectile::new, MobCategory.MISC)
                            .sized(0.25f, 0.25f)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build("skill_projectile"));

    public static final Supplier<EntityType<DecadeHenshinEffect>> DECADE_SPECIAL_EFFECT =
            effect("decade_special_effect", DecadeHenshinEffect::new, 0.2f, 1.8f);

    public static final Supplier<EntityType<AgitoKickEffect>> AGITO_KICK_EFFECT =
            effect("agito_kick_effect", AgitoKickEffect::new, 4f, 0.1f);

    public static final Supplier<EntityType<RyukiHenshinEffect>> RYUKI_HENSHIN_EFFECT =
            effect("ryuki_henshin_effect", RyukiHenshinEffect::new, 0.2f, 1.8f);

    private static <T extends Entity> Supplier<EntityType<T>> effect(
            String name, EntityType.EntityFactory<T> factory, float width, float height
    ) {
        return ENTITY_TYPES.register(name, () ->
                EntityType.Builder.of(factory, MobCategory.MISC)
                        .sized(width, height)
                        .clientTrackingRange(32)
                        .updateInterval(10)
                        .build(name));

    }

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
