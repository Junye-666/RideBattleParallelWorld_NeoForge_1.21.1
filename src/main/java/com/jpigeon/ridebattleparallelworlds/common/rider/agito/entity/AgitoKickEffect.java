package com.jpigeon.ridebattleparallelworlds.common.rider.agito.entity;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.rideevolutionlib.util.entity.AbstractOwnerFollowEffect;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animation.AnimatableManager;

import java.util.Optional;
import java.util.UUID;

public class AgitoKickEffect extends AbstractOwnerFollowEffect {
    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = createOwnerAccessor(AgitoKickEffect.class);

    public AgitoKickEffect(EntityType<AgitoKickEffect> type, Level level) {
        super(type, level, RideBattleParallelWorlds.MODID, "agito", "agito_kick_effect", 150);
    }

    @Override
    protected EntityDataAccessor<Optional<UUID>> ownerAccessor() {
        return OWNER_UUID;
    }

    @Override
    protected void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar) {
        addController(registrar, "agito_kick_effect_controller", createHoldController("turn"));
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
