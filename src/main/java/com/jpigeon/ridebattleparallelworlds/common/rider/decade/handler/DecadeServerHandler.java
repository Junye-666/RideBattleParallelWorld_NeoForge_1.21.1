package com.jpigeon.ridebattleparallelworlds.common.rider.decade.handler;

import com.jpigeon.ridebattlelib.common.api.RideBattleAPI;
import com.jpigeon.ridebattlelib.common.api.server.IRiderServerHandler;
import com.jpigeon.ridebattlelib.common.config.FormConfig;
import com.jpigeon.ridebattlelib.server.event.FormSwitchEvent;
import com.jpigeon.ridebattlelib.server.event.HenshinEvent;
import com.jpigeon.ridebattlelib.server.event.ItemInsertionEvent;
import com.jpigeon.ridebattlelib.server.event.SlotExtractionEvent;
import com.jpigeon.ridebattleparallelworlds.common.registry.ModEntities;
import com.jpigeon.ridebattleparallelworlds.common.registry.ModSounds;
import com.jpigeon.ridebattleparallelworlds.common.rider.RiderIds;
import com.jpigeon.ridebattleparallelworlds.common.rider.decade.armor.DecaDriverItem;
import com.jpigeon.ridebattleparallelworlds.common.rider.decade.DecadeConfig;
import com.jpigeon.ridebattleparallelworlds.common.rider.decade.entity.DecadeHenshinEffect;
import com.jpigeon.ridebattleparallelworlds.server.util.ModTags;
import com.jpigeon.ridebattleparallelworlds.server.util.PWSkillUtils;
import com.jpigeon.rideevolutionlib.compat.geckoLib.GeoAnimationApi;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.Optional;

import static com.jpigeon.ridebattlelib.common.api.RideBattleAPI.completeIn;
import static com.jpigeon.ridebattlelib.common.api.RideBattleAPI.scheduleTicks;
import static com.jpigeon.ridebattleparallelworlds.server.util.AbilitiesUtils.isValidItem;
import static com.jpigeon.ridebattleparallelworlds.server.util.RiderUtils.playHenshinSound;
import static com.jpigeon.ridebattleparallelworlds.server.util.RiderUtils.playSound;

public class DecadeServerHandler implements IRiderServerHandler {
    @Override
    public ResourceLocation riderId() {
        return RiderIds.DECADE_ID;
    }

    @Override
    public void onHenshinPre(HenshinEvent.Pre event) {
        Player player = event.getPlayer();
        ResourceLocation formId = event.getFormId();

        henshinDecade(player, formId);
    }

    @Override
    public void onHenshinPost(HenshinEvent.Post event) {
        Player player = event.getPlayer();

        PWSkillUtils.addSaturation(player, 40);
        PWSkillUtils.addRegeneration(player, 20);
    }

    @Override
    public void onSwitchPre(FormSwitchEvent.Pre event) {
        Player player = event.getPlayer();
        ResourceLocation formId = event.getNewFormId();

        henshinDecade(player, formId);
    }

    @Override
    public void onInsert(ItemInsertionEvent.Post event) {
        Player player = event.getPlayer();
        ItemStack stack = event.getStack();

        if (event.getSlotId().equals(DecadeConfig.DECA_CARD)) {
            playDecaDriverTransition(player, "closed", "close");
        }

        // 以下保留原本的音效逻辑
        playSound(player, ModSounds.DECADE_INSERT.get());

        if (isValidItem(stack, ModTags.Items.KAMEN_RIDE_CARDS)) {
            scheduleTicks(5, () ->
                    playSound(player, ModSounds.KAMEN_RIDE.get())
            );
        } else if (isValidItem(stack, ModTags.Items.FORM_RIDE_CARDS)) {
            scheduleTicks(5, () ->
                    playSound(player, ModSounds.FORM_RIDE.get())
            );
        }
    }

    @Override
    public void onExtract(SlotExtractionEvent.Post event) {
        Player player = event.getPlayer();

        if (!event.getSlotId().equals(DecadeConfig.DECA_CARD)) {
            return;
        }

        playDecaDriverTransition(player, "opened", "open");
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();

        if (player.level().isClientSide()) {
            return;
        }

        restoreDecaDriverState(player);
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

        if (!(event.getTo().getItem() instanceof DecaDriverItem)) {
            return;
        }

        restoreDecaDriverState(player);
    }

    private static void henshinDecade(Player player, ResourceLocation formId) {
        FormConfig form = RideBattleAPI.getFormConfig(player, formId);
        scheduleTicks(10, () -> playHenshinSound(player, form));
        if (formId.equals(DecadeConfig.DECADE_BASE_ID)) {
            Level level = player.level();

            DecadeHenshinEffect effect = new DecadeHenshinEffect(
                    ModEntities.DECADE_SPECIAL_EFFECT.get(),
                    level
            );
            effect.setPos(player.position());
            effect.setYRot(player.getYRot());

            effect.setOwner(player);

            scheduleTicks(20, () -> level.addFreshEntity(effect));
        }
        Optional<Integer> length = ModSounds.getSoundLength(form);
        length.ifPresent(integer -> completeIn(integer, player));
    }

    /**
     * 设置 Decade 驱动器的持续动画状态。
     */
    private static void setDecaDriverState(Player player, String state) {
        ItemStack driverStack = player.getItemBySlot(EquipmentSlot.LEGS);

        if (!(driverStack.getItem() instanceof DecaDriverItem decaDriver)) {
            return;
        }

        GeoAnimationApi.setState(
                decaDriver,
                driverStack,
                player,
                state
        );
    }

    /**
     * 播放驱动器开合动画，并同步切换到目标持续状态。
     *
     * @param state   动画结束后保持的状态：opened / closed
     * @param trigger 一次性动画：open / close
     */
    private static void playDecaDriverTransition(
            Player player,
            String state,
            String trigger
    ) {
        ItemStack driverStack = player.getItemBySlot(EquipmentSlot.LEGS);

        if (!(driverStack.getItem() instanceof DecaDriverItem decaDriver)) {
            return;
        }

        // 先更新持续状态
        GeoAnimationApi.setState(
                decaDriver,
                driverStack,
                player,
                state
        );

        // 再触发一次性开合动画
        GeoAnimationApi.trigger(
                decaDriver,
                driverStack,
                player,
                "actions",
                trigger
        );
    }

    /**
     * 玩家登录或重新装备驱动器时恢复正确的持续状态。
     * 恢复状态时不播放开合动画。
     */
    private static void restoreDecaDriverState(Player player) {
        ItemStack driverStack = player.getItemBySlot(EquipmentSlot.LEGS);

        if (!(driverStack.getItem() instanceof DecaDriverItem)) {
            return;
        }

        ItemStack cardStack =
                RideBattleAPI.getItemForSlot(player, DecadeConfig.DECA_CARD);

        setDecaDriverState(
                player,
                cardStack.isEmpty() ? "opened" : "closed"
        );
    }
}
