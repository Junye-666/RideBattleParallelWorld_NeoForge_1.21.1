package com.jpigeon.ridebattleparallelworlds.common.data.component;

import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.mojang.serialization.Codec;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.item.ItemStack;

/**
 * 转换武器的"原貌存档"：整份物品 NBT，剥离 ORIGIN_ITEM_DATA 防止递归。
 */
public record ItemData(CompoundTag tag) {

    public static final ItemData EMPTY = new ItemData(new CompoundTag());

    public static final Codec<ItemData> CODEC =
            CompoundTag.CODEC.xmap(ItemData::new, ItemData::tag);

    /**
     * 从物品生成存档。
     *
     * @param registryAccess 调用方需提供，通常是 {@code player.registryAccess()}
     */
    public static ItemData fromItemStack(ItemStack stack, RegistryAccess registryAccess) {
        if (stack.isEmpty()) return EMPTY;

        ItemStack copy = stack.copyWithCount(1);
        // 关键：剥掉我们自己，避免嵌套自引用
        copy.remove(ModDataComponents.ORIGIN_ITEM_DATA.get());

        RegistryOps<Tag> ops = RegistryOps.create(NbtOps.INSTANCE, registryAccess);
        return ItemStack.CODEC.encodeStart(ops, copy)
                .result()
                .map(t -> new ItemData((CompoundTag) t))
                .orElseGet(() -> {
                    RideBattleParallelWorlds.LOGGER.warn(
                            "ItemData: 无法编码 {}，将保存为空档",
                            BuiltInRegistries.ITEM.getKey(copy.getItem()));
                    return EMPTY;
                });
    }

    /**
     * 还原物品。
     */
    public ItemStack toItemStack(RegistryAccess registryAccess) {
        if (tag.isEmpty()) return ItemStack.EMPTY;

        RegistryOps<Tag> ops = RegistryOps.create(NbtOps.INSTANCE, registryAccess);
        return ItemStack.CODEC.parse(ops, tag)
                .result()
                .orElse(ItemStack.EMPTY);
    }
}