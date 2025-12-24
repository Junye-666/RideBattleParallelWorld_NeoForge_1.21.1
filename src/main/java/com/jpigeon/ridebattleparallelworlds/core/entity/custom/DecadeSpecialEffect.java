package com.jpigeon.ridebattleparallelworlds.core.entity.custom;

import com.jpigeon.ridebattlelib.api.RiderManager;
import com.jpigeon.ridebattleparallelworlds.impl.geckoLib.entity.BaseKamenRiderGeoEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animation.AnimatableManager;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class DecadeSpecialEffect extends BaseKamenRiderGeoEntity {
    private static final int LIFETIME = 25;
    private int lifetime = 0;
    private final float alpha = 0.5f;
    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(DecadeSpecialEffect.class, EntityDataSerializers.OPTIONAL_UUID);


    public DecadeSpecialEffect(EntityType<DecadeSpecialEffect> type, Level level) {
        super(type, level, "decade", "decade_special_effect", false, false);
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
        return alpha;
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
        this.setPos(owner.position());
        this.setXRot(owner.getXRot());
        this.setYRot(owner.getYHeadRot());
        if (lifetime % 5 != 0) return;

        // 超时消失改为触发消失动画
        if (lifetime >= LIFETIME) {
            this.discard();
            return;
        }

        if (owner.isRemoved()) {
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

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 1.0)
                .add(Attributes.MOVEMENT_SPEED, 0.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 100.0)
                .add(Attributes.FOLLOW_RANGE, 0.0);
    }
}
