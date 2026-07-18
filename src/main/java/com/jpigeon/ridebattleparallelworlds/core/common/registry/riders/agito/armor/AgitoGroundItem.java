package com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.agito.armor;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.rideevolutionlib.compat.geckoLib.armor.BaseRiderArmorItem;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorMaterial;
import software.bernie.geckolib.animation.AnimatableManager;

public class AgitoGroundItem extends BaseRiderArmorItem {
    public enum AnimState {IDLE, OPEN, POWERED}

    public AgitoGroundItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(RideBattleParallelWorlds.MODID, "agito", "ground", material, type, properties, true);
    }

    @Override
    protected void registerAnimationControllers(AnimatableManager.ControllerRegistrar registrar) {
        addController(registrar, "idle", createLoopController("idle"));
        addController(registrar, "open", createHoldController("open"));
    }

    public void triggerOpen() {
        setAnimState("open");
    }

    public void setClosed() {
        setAnimState("idle");
    }

    public void setCurrentState(AnimState state){
        setAnimState(state.name().toLowerCase());
    }
}
