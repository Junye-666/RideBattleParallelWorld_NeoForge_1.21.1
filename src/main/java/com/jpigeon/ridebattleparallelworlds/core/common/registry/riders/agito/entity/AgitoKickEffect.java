package com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.agito.entity;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.rideevolutionlib.util.entity.AbstractOwnerFollowEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animation.AnimatableManager;

public class AgitoKickEffect extends AbstractOwnerFollowEffect {
    public AgitoKickEffect(EntityType<AgitoKickEffect> type, Level level) {
        super(type, level, RideBattleParallelWorlds.MODID, "agito", "agito_kick_effect", 150);
    }

    @Override
    protected void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar) {
        addController(registrar, "agito_kick_effect_controller", createOnceController("turn"));
    }

    @Override
    public boolean shouldApplyTransparency() {
        return true;
    }

    @Override
    public float getCurrentAlpha() {
        return 0.5f;
    }
}
