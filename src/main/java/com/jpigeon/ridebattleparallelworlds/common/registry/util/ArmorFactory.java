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
    private ArmorFactory() {}

    /**
     * 命名规则：{@code <rider>_<form>_<type>}。
     * <p>
     * material 必须用 {@link Supplier} 延迟读取：
     * {@code ModItems.<clinit>} 与 {@code PWArmorMaterial.<clinit>} 会互相引用，
     * 若在 {@code ModItems.<clinit>} 里直接读 {@code PWArmorMaterial.KUUGA_MATERIAL}，
     * 会在对方尚未初始化完成时拿到 null → {@code ArmorItem.material = null} → 运行时 NPE。
     * 延迟到 {@code items.register(...)} 的 supplier 内（RegisterEvent 阶段）才读，
     * 那时 PWArmorMaterial 已经完整初始化。
     */
    public static DeferredItem<ParallelRiderArmor> create(
            DeferredRegister.Items items,
            String riderName,
            String formName,
            Supplier<Holder<ArmorMaterial>> materialSupplier,
            ArmorItem.Type type) {

        String name = riderName + "_" + formName + "_" + type.getName();
        return items.register(name, () ->
                new ParallelRiderArmor(
                        RideBattleParallelWorlds.MODID,
                        riderName, formName,
                        materialSupplier.get(), type,
                        new Item.Properties(), false));
    }
}