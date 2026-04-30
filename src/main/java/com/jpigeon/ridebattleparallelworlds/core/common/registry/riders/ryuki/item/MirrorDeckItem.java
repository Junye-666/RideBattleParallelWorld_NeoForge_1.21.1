package com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.ryuki.item;

import com.jpigeon.ridebattlelib.common.api.RideBattleAPI;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.item.ModItems;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.ryuki.VBuckleItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

// 镜系统的卡盒Item
public class MirrorDeckItem extends Item {
    public MirrorDeckItem(Properties properties) {
        super(properties);
        properties.stacksTo(1);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack deck = player.getItemInHand(usedHand);
        if (!usedHand.equals(InteractionHand.OFF_HAND)) return InteractionResultHolder.fail(deck);

        // 仅当玩家未变身
        if (!player.level().isClientSide() && !RideBattleAPI.isTransformed(player)) {
            ItemStack leg = player.getItemBySlot(EquipmentSlot.LEGS);
            if (leg.getItem() instanceof VBuckleItem) {
                return InteractionResultHolder.success(deck);
            } else {
                player.getCooldowns().addCooldown(deck.getItem(), 30);
                player.setItemSlot(EquipmentSlot.LEGS, new ItemStack(ModItems.V_BUCKLE.get()));
                if (!leg.isEmpty() && !leg.is(Items.AIR)) {
                    if (!player.getInventory().add(leg)) {
                        player.drop(leg, false);
                    }
                }
                return InteractionResultHolder.pass(deck);
            }
        }

        return InteractionResultHolder.pass(deck);
    }
}
