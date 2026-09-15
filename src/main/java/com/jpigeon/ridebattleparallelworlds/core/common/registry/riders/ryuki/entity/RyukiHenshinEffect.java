package com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.ryuki.entity;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.rideevolutionlib.util.entity.AbstractOwnerFollowEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animation.AnimatableManager;

public class RyukiHenshinEffect extends AbstractOwnerFollowEffect {
    public RyukiHenshinEffect(EntityType<RyukiHenshinEffect> type, Level level) {
        super(type, level, RideBattleParallelWorlds.MODID, "ryuki", "ryuki_henshin_effect", 15);
    }

    @Override
    protected void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar) {
        addController(registrar, "ryuki_henshin_controller", createOnceController("ryuki_henshin_effect"));
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
