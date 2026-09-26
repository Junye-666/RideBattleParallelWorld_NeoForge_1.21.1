package com.jpigeon.ridebattleparallelworlds.common.rider.ryuki.entity;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.rideevolutionlib.util.entity.AbstractOwnerFollowEffect;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animation.AnimatableManager;

import java.util.Optional;
import java.util.UUID;

public class RyukiHenshinEffect extends AbstractOwnerFollowEffect {
    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = createOwnerAccessor(RyukiHenshinEffect.class);

    public RyukiHenshinEffect(EntityType<RyukiHenshinEffect> type, Level level) {
        super(type, level, RideBattleParallelWorlds.MODID, "mirror", "ryuki_henshin_effect", 15);
    }

    @Override
    protected EntityDataAccessor<Optional<UUID>> ownerAccessor() {
        return OWNER_UUID;
    }

    @Override
    protected void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar) {
        addController(registrar, "ryuki_henshin_controller", createHoldController("ryuki_henshin_effect"));
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
