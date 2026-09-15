package com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.ryuki.item;

import com.jpigeon.ridebattlelib.common.api.RideBattleAPI;
import com.jpigeon.ridebattleparallelworlds.core.client.anim.rider.MirrorAnimations;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.item.ModItems;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.ryuki.VBuckleItem;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

// 镜系统的卡盒Item
public class MirrorDeckItem extends Item {
    public MirrorDeckItem(Properties properties) {
        super(properties);
        properties.stacksTo(1);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand usedHand) {
        ItemStack deck = player.getItemInHand(usedHand);
        if (!usedHand.equals(InteractionHand.OFF_HAND)) return InteractionResultHolder.fail(deck);

        // 仅当玩家未变身
        if (!RideBattleAPI.isTransformed(player)) {
            ItemStack leg = player.getItemBySlot(EquipmentSlot.LEGS);
            if (leg.getItem() instanceof VBuckleItem) {
                return InteractionResultHolder.success(deck);
            } else {
                player.getCooldowns().addCooldown(deck.getItem(), 30);
                player.setItemSlot(EquipmentSlot.LEGS, new ItemStack(ModItems.V_BUCKLE.get()));
                if (player instanceof LocalPlayer p) {
                    MirrorAnimations.V_BUCKLE.play(p);
                }
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
