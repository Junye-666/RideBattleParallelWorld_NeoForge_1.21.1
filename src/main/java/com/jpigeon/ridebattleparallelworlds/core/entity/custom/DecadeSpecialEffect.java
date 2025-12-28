package com.jpigeon.ridebattleparallelworlds.core.entity.custom;

import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.entity.BaseKamenRiderEffectEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animation.AnimatableManager;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class DecadeSpecialEffect extends BaseKamenRiderEffectEntity {
    private static final int MAX_LIFETIME = 46;
    private int lifetime = 0;
    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(DecadeSpecialEffect.class, EntityDataSerializers.OPTIONAL_UUID);


    public DecadeSpecialEffect(EntityType<DecadeSpecialEffect> type, Level level) {
        super(type, level, "decade", "decade_special_effect");
    }

    @Override
    protected void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar) {
        addController(registrar, "decade_henshin_controller", createOnceAnimationController("decade_henshin_controller", "decade_henshin"));
    }

    @Override
    public boolean shouldApplyTransparency() {
        return true;
    }

    @Override
    public float getCurrentAlpha() {
        return 0.5f;
    }

    @Override
    public void tick() {
        super.tick();
        lifetime++;

        Player owner = getOwner();
        if (owner == null) {
            this.discard();
            return;
        }

        this.setPos(owner.getX(), owner.getY(), owner.getZ());
        this.setYRot(owner.getYRot());

        if (lifetime >= MAX_LIFETIME || owner.isRemoved()) {
            this.discard();
        }
    }

    @Nullable
    public Player getOwner() {
        Optional<UUID> uuid = this.entityData.get(OWNER_UUID);
        return uuid.map(value -> level().getPlayerByUUID(value)).orElse(null);
    }

    public void setOwner(@Nullable Player player) {
        if (player != null) {
            this.entityData.set(OWNER_UUID, Optional.of(player.getUUID()));
        } else {
            this.entityData.set(OWNER_UUID, Optional.empty());
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(OWNER_UUID, Optional.empty());
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        entityData.get(OWNER_UUID).ifPresent(uuid -> tag.putUUID("OwnerUUID", uuid));
    }

    @Override
    public boolean isCustomNameVisible() {
        return false;
    }
}
