package com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.decade.entity;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.rideevolutionlib.util.entity.AbstractOwnerFollowEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animation.AnimatableManager;

public class DecadeHenshinEffect extends AbstractOwnerFollowEffect {
    public DecadeHenshinEffect(EntityType<DecadeHenshinEffect> type, Level level) {
        super(type, level, RideBattleParallelWorlds.MODID, "decade", "decade_special_effect", 46);
    }

    @Override
    protected void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar) {
        addController(registrar, "decade_henshin_controller", createOnceController("decade_henshin"));
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
