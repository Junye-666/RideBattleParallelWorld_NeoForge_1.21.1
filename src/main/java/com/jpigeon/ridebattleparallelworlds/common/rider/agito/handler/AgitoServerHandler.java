package com.jpigeon.ridebattleparallelworlds.common.rider.agito.handler;

import com.jpigeon.ridebattlelib.common.api.RideBattleAPI;
import com.jpigeon.ridebattlelib.common.api.server.IRiderServerHandler;
import com.jpigeon.ridebattlelib.server.event.FormSwitchEvent;
import com.jpigeon.ridebattlelib.server.event.HenshinEvent;
import com.jpigeon.ridebattlelib.server.event.ItemInsertionEvent;
import com.jpigeon.ridebattlelib.server.event.UnhenshinEvent;
import com.jpigeon.ridebattleparallelworlds.common.data.component.ItemStateComponent;
import com.jpigeon.ridebattleparallelworlds.common.registry.ItemFormRegistry;
import com.jpigeon.ridebattleparallelworlds.common.registry.ModSounds;
import com.jpigeon.ridebattleparallelworlds.common.rider.RiderIds;
import com.jpigeon.ridebattleparallelworlds.common.rider.agito.AgitoConfig;
import com.jpigeon.ridebattleparallelworlds.common.rider.agito.armor.AlterRingItem;
import com.jpigeon.ridebattleparallelworlds.common.rider.agito.item.ShiningCaliburItem;
import com.jpigeon.ridebattleparallelworlds.server.util.AbilitiesUtils;
import com.jpigeon.ridebattleparallelworlds.server.util.PWSkillUtils;
import com.jpigeon.rideevolutionlib.compat.geckoLib.GeoAnimationApi;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.jetbrains.annotations.Nullable;

import static com.jpigeon.ridebattlelib.common.api.RideBattleAPI.completeIn;
import static com.jpigeon.ridebattlelib.common.api.RideBattleAPI.scheduleTicks;
import static com.jpigeon.ridebattleparallelworlds.server.util.RiderUtils.playSound;

public class AgitoServerHandler implements IRiderServerHandler {
    @Override
    public ResourceLocation riderId() {
        return RiderIds.AGITO_ID;
    }

    @Override
    public void onInsert(ItemInsertionEvent.Post event) {
        Player player = event.getPlayer();

        ResourceLocation slotId = event.getSlotId();
        ItemStack stack = event.getStack();
        ResourceLocation formId = ItemFormRegistry.getFormForItem(stack.getItem());
        if (formId == null) return;
        ResourceLocation riderId;

        if (slotId.equals(AgitoConfig.ALTER_RING_CORE)) {
            riderId = RiderIds.AGITO_ID;
            AbilitiesUtils.unlockFormIfLocked(player, riderId, formId);
        }

        prepareAgito(player);
    }

    @Override
    public void onHenshinPre(HenshinEvent.Pre event) {
        Player player = event.getPlayer();
        completeAgito(player);
    }

    @Override
    public void onHenshinPost(HenshinEvent.Post event) {
        Player player = event.getPlayer();

        applyAlterRingState(player, event.getFormId());

        PWSkillUtils.addSaturation(player, 40);
        PWSkillUtils.addRegeneration(player, 20);
    }

    @Override
    public void onSwitchPre(FormSwitchEvent.Pre event) {
        Player player = event.getPlayer();
        completeAgito(player);
    }

    @Override
    public void onSwitchPost(FormSwitchEvent.Post event) {
        applyAlterRingState(event.getPlayer(), event.getNewFormId());
    }

    @Override
    public void onUnhenshinPost(UnhenshinEvent.Post event) {
        setAlterRingState(event.getPlayer(), "shrink");
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();

        if (player.level().isClientSide()) {
            return;
        }

        if (RideBattleAPI.isTransformed(player)) {
            applyAlterRingState(player, RideBattleAPI.getCurrentFormId(player));
        } else {
            setAlterRingState(player, "shrink");
        }

        restoreShiningCaliburState(player);
    }

    @SubscribeEvent
    public static void onPlayerEquip(LivingEquipmentChangeEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (player.level().isClientSide()) {
            return;
        }

        if (event.getSlot() != EquipmentSlot.LEGS) {
            return;
        }

        if (!(event.getTo().getItem() instanceof AlterRingItem)) {
            return;
        }

        if (RideBattleAPI.isTransformed(player)) {
            applyAlterRingState(player, RideBattleAPI.getCurrentFormId(player));
        } else {
            setAlterRingState(player, "shrink");
        }
    }

    private static void restoreShiningCaliburState(Player player) {
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() instanceof ShiningCaliburItem shiningCalibur) {
                String state = ItemStateComponent.getPersistentState(stack, "closed");

                GeoAnimationApi.setState(
                        shiningCalibur,
                        stack,
                        player,
                        state
                );
            }
        }

        ItemStack offhandStack = player.getOffhandItem();

        if (offhandStack.getItem() instanceof ShiningCaliburItem shiningCalibur) {
            String state = ItemStateComponent.getPersistentState(offhandStack, "closed");

            GeoAnimationApi.setState(
                    shiningCalibur,
                    offhandStack,
                    player,
                    state
            );
        }
    }

    private static void applyAlterRingState(Player player, @Nullable ResourceLocation formId) {
        ItemStack stack = player.getItemBySlot(EquipmentSlot.LEGS);

        if (!(stack.getItem() instanceof AlterRingItem alterRing)) {
            return;
        }

        String state = alterRing.getAnimationStateByFormId(formId);

        if (state == null) {
            state = "shrink";
        }

        GeoAnimationApi.setState(
                alterRing,
                stack,
                player,
                state
        );
    }

    private static void setAlterRingState(Player player, String state) {
        ItemStack stack = player.getItemBySlot(EquipmentSlot.LEGS);

        if (!(stack.getItem() instanceof AlterRingItem alterRing)) {
            return;
        }

        GeoAnimationApi.setState(
                alterRing,
                stack,
                player,
                state
        );
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
}
