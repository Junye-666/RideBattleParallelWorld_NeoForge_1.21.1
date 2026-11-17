package com.jpigeon.ridebattleparallelworlds.common.data.component;

import com.jpigeon.rideevolutionlib.compat.geckoLib.GeoAnimationApi;
import com.jpigeon.rideevolutionlib.compat.geckoLib.item.BaseRiderGeoItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public final class ItemStateComponent {

    private ItemStateComponent() {
    }

    public static String getPersistentState(
            ItemStack stack,
            String defaultState
    ) {
        if (stack.isEmpty()) {
            return defaultState;
        }

        String state = stack.get(ModDataComponents.ITEM_STATE.get());

        return state != null ? state : defaultState;
    }

    public static void setPersistentState(
            BaseRiderGeoItem item,
            ItemStack stack,
            Entity entity,
            String state
    ) {
        if (stack.isEmpty() || entity == null || entity.level().isClientSide()) {
            return;
        }

        stack.set(ModDataComponents.ITEM_STATE.get(), state);

        GeoAnimationApi.setState(item, stack, entity, state);
    }
}