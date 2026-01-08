package com.jpigeon.ridebattleparallelworlds.core.entity;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.core.entity.custom.AgitoKickEffect;
import com.jpigeon.ridebattleparallelworlds.core.entity.custom.DecadeHenshinEffect;
import com.jpigeon.ridebattleparallelworlds.core.entity.custom.SkillProjectile;
import net.minecraft.core.registries.BuiltInRegistries;
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
            ENTITY_TYPES.register("decade_special_effect",
                    () -> EntityType.Builder.of(DecadeHenshinEffect::new, MobCategory.MISC)
                            .sized(0.2f, 1.8f)
                            .clientTrackingRange(32)
                            .updateInterval(10)
                            .build("decade_special_effect"));

    public static final Supplier<EntityType<AgitoKickEffect>> AGITO_KICK_EFFECT =
            ENTITY_TYPES.register("agito_kick_effect",
                    () -> EntityType.Builder.of(AgitoKickEffect::new, MobCategory.MISC)
                            .sized(4f, 0.1f)
                            .clientTrackingRange(32)
                            .updateInterval(10)
                            .build("agito_kick_effect"));

    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
}
