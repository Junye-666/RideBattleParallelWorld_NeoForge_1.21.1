package com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.decade.entity;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.rideevolutionlib.util.entity.AbstractOwnerFollowEffect;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animation.AnimatableManager;

import java.util.Optional;
import java.util.UUID;

public class DecadeHenshinEffect extends AbstractOwnerFollowEffect {
    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = createOwnerAccessor(DecadeHenshinEffect.class);

    public DecadeHenshinEffect(EntityType<DecadeHenshinEffect> type, Level level) {
        super(type, level, RideBattleParallelWorlds.MODID, "decade", "decade_special_effect", 46);
    }
    static {
        System.out.println("OWNER_UUID id = " + OWNER_UUID);
    }
    @Override
    protected EntityDataAccessor<Optional<UUID>> ownerAccessor() {
        return OWNER_UUID;
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
