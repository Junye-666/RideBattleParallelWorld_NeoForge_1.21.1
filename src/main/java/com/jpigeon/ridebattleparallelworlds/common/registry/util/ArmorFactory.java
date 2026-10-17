package com.jpigeon.ridebattleparallelworlds.common.registry.util;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.common.registry.ParallelRiderArmor;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ArmorFactory {
    private ArmorFactory() {
    }

    /**
     * 命名规则：{@code <rider>_<form>_<type>}。
     * <p>
     * material 必须用 {@link Supplier} 延迟读取：
     */
    public static DeferredItem<ParallelRiderArmor> create(
            DeferredRegister.Items items,
            String riderName,
            String formName,
            Supplier<Holder<ArmorMaterial>> materialSupplier,
            ArmorItem.Type type,
            boolean animated) {

        String name = riderName + "_" + formName + "_" + type.getName();
        return items.register(name, () ->
                new ParallelRiderArmor(
                        RideBattleParallelWorlds.MODID,
                        riderName, formName,
                        materialSupplier.get(), type,
                        new Item.Properties(), animated));
    }

    public static DeferredItem<ParallelRiderArmor> create(
            DeferredRegister.Items items,
            String riderName,
            String formName,
            Supplier<Holder<ArmorMaterial>> materialSupplier,
            ArmorItem.Type type) {

        return create(items, riderName, formName, materialSupplier, type, false);
    }
}