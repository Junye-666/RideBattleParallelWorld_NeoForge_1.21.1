package com.jpigeon.ridebattleparallelworlds.core.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.Objects;
import java.util.Optional;

/**
 * 轻量级 ItemStack 数据，只存储最基本的信息用于复制
 */
public record ItemData(
        ResourceLocation itemId,
        int count,
        Optional<String> customName,
        Optional<CompoundTag> enchantments
) {
    public static final ItemData EMPTY = new ItemData(
            BuiltInRegistries.ITEM.getKey(Items.AIR),
            0,
            Optional.empty(),
            Optional.empty()
    );

    public static Codec<ItemData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("item").forGetter(ItemData::itemId),
                    Codec.INT.optionalFieldOf("count", 1).forGetter(ItemData::count),
                    Codec.STRING.optionalFieldOf("name").forGetter(ItemData::customName),
                    CompoundTag.CODEC.optionalFieldOf("enchantments").forGetter(ItemData::enchantments)
            ).apply(instance, ItemData::new)
    );

    public static ItemData fromItemStack(ItemStack stack) {
        if (stack.isEmpty()) {
            return EMPTY;
        }

        ResourceLocation itemId = stack.getItemHolder().unwrapKey()
                .orElse(BuiltInRegistries.ITEM.getResourceKey(Items.AIR).get())
                .location();

        String name = stack.has(DataComponents.CUSTOM_NAME) ?
                Objects.requireNonNull(stack.get(DataComponents.CUSTOM_NAME)).getString() : null;

        ItemEnchantments enchantments = stack.get(DataComponents.ENCHANTMENTS);
        CompoundTag enchantTag = null;
        if (enchantments != null && !enchantments.isEmpty()) {
            // 将附魔编码为 NBT
            var result = ItemEnchantments.CODEC.encodeStart(
                    NbtOps.INSTANCE,
                    enchantments
            );
            if (result.result().isPresent()) {
                enchantTag = (CompoundTag) result.result().get();
            }
        }

        return new ItemData(
                itemId,
                stack.getCount(),
                Optional.ofNullable(name),
                Optional.ofNullable(enchantTag)
        );
    }

    public ItemStack toItemStack() {
        if (this.equals(EMPTY) || itemId.equals(BuiltInRegistries.ITEM.getKey(Items.AIR))) {
            return ItemStack.EMPTY;
        }

        Item item = BuiltInRegistries.ITEM.get(itemId);
        if (item == Items.AIR) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = new ItemStack(item, count);

        // 设置自定义名称
        customName.ifPresent(name ->
                stack.set(DataComponents.CUSTOM_NAME, Component.literal(name))
        );

        // 设置附魔
        enchantments.ifPresent(enchantTag -> {
            var result = ItemEnchantments.CODEC.parse(NbtOps.INSTANCE, enchantTag);
            if (result.result().isPresent()) {
                stack.set(DataComponents.ENCHANTMENTS, result.result().get());
            }
        });
        return stack;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemData(
                ResourceLocation id, int count1, Optional<String> name, Optional<CompoundTag> enchantments1
        ))) return false;

        if (count != count1) return false;
        if (!itemId.equals(id)) return false;
        if (!customName.equals(name)) return false;

        // 比较 enchantmentsTag
        if (enchantments.isPresent() && enchantments1.isPresent()) {
            return enchantments.get().equals(enchantments1.get());
        } else if (enchantments.isPresent() || enchantments1.isPresent()) {
            return false;
        }

        return false;
    }

    @Override
    public int hashCode() {
        int result = itemId.hashCode();
        result = 31 * result + count;
        result = 31 * result + customName.hashCode();
        result = 31 * result + enchantments.hashCode();

        return result;
    }
}
