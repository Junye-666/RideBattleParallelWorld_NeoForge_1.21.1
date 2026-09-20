package com.jpigeon.ridebattleparallelworlds.core.server.handler.rider;

import com.jpigeon.ridebattlelib.common.api.RideBattleAPI;
import com.jpigeon.ridebattlelib.common.api.server.IRiderServerHandler;
import com.jpigeon.ridebattlelib.server.event.FormSwitchEvent;
import com.jpigeon.ridebattlelib.server.event.HenshinEvent;
import com.jpigeon.ridebattlelib.server.event.ItemInsertionEvent;
import com.jpigeon.ridebattlelib.server.event.UnhenshinEvent;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.RiderIds;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.sound.ModSounds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.jpigeon.ridebattlelib.common.api.RideBattleAPI.completeIn;
import static com.jpigeon.ridebattleparallelworlds.core.common.registry.item.ModItems.Agito.*;
import static com.jpigeon.ridebattleparallelworlds.core.server.util.RiderUtils.playSound;

public class AgitoServerHandler implements IRiderServerHandler {
    @Override
    public ResourceLocation riderId() {
        return RiderIds.AGITO_ID;
    }

    @Override
    public void onInsert(ItemInsertionEvent.Post event) {
        Player player = event.getPlayer();

        prepareAgito(player);
    }

    @Override
    public void onHenshinPre(HenshinEvent.@NotNull Pre event) {
        Player player = event.getPlayer();
        completeAgito(player);
    }

    @Override
    public void onSwitchPre(FormSwitchEvent.@NotNull Pre event) {
        Player player = event.getPlayer();
        removeAgitoWeapon(player);
        completeAgito(player);
    }

    @Override
    public void onUnhenshinPost(UnhenshinEvent.@NotNull Post event) {
        removeAgitoWeapon(event.getPlayer());
    }

    private static void prepareAgito(Player player) {
        playSound(player, ModSounds.AGITO_PREPARE.get());
        if (!RideBattleAPI.isTransformed(player)) playSound(player, ModSounds.AGITO_STEADY.get());
    }

    private static void completeAgito(Player player) {
        // TODO : 燃烧/闪耀相关音效
        scheduleTicks(1, () -> playSound(player, ModSounds.AGITO_FINISH.get()));
        completeIn(10, player);
    }

    private static void removeAgitoWeapon(Player player) {
        List<Item> toRemove = List.of(FLAME_SABER.get(), STORM_HALBERD.get(), SHINING_CALIBUR.get());
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (toRemove.contains(stack.getItem())) {
                int removeAmount = stack.getCount();
                stack.shrink(removeAmount);
            }
        }
    }

    private static void scheduleTicks(int ticks, Runnable callback) {
        RideBattleAPI.scheduleTicks(ticks, callback);
    }
}
