package com.jpigeon.ridebattleparallelworlds.core.entity;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.core.entity.custom.DecadeSpecialEffect;
import com.jpigeon.ridebattleparallelworlds.core.entity.custom.SkillProjectile;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, RideBattleParallelWorlds.MODID);

    public static final Supplier<EntityType<SkillProjectile>> SKILL_PROJECTILE =
            ENTITY_TYPES.register("skill_projectile",
                    () -> EntityType.Builder.<SkillProjectile>of(SkillProjectile::new, MobCategory.MISC)
                            .sized(0.25f, 0.25f) // 碰撞箱大小
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build("skill_projectile"));

    public static final Supplier<EntityType<DecadeSpecialEffect>> DECADE_SPECIAL_EFFECT =
            ENTITY_TYPES.register("decade_special_effect",
                    () -> EntityType.Builder.of(DecadeSpecialEffect::new, MobCategory.MISC)
                            .sized(0.8f, 1.8f)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build("decade_special_effect"));


    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }

    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(DECADE_SPECIAL_EFFECT.get(), DecadeSpecialEffect.createAttributes().build());
    }
}
