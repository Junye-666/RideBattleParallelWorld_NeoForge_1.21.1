package com.jpigeon.ridebattleparallelworlds.core.server.handler.rider;

import com.jpigeon.ridebattlelib.common.api.RideBattleAPI;
import com.jpigeon.ridebattlelib.common.api.server.IRiderServerHandler;
import com.jpigeon.ridebattlelib.common.config.FormConfig;
import com.jpigeon.ridebattlelib.server.event.FormSwitchEvent;
import com.jpigeon.ridebattlelib.server.event.HenshinEvent;
import com.jpigeon.ridebattlelib.server.event.ItemInsertionEvent;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.entity.ModEntities;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.RiderIds;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.decade.DecaDriverItem;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.decade.DecadeConfig;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.decade.entity.DecadeHenshinEffect;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.sound.ModSounds;
import com.jpigeon.ridebattleparallelworlds.core.server.util.ModTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static com.jpigeon.ridebattlelib.common.api.RideBattleAPI.completeIn;
import static com.jpigeon.ridebattlelib.common.api.RideBattleAPI.scheduleTicks;
import static com.jpigeon.ridebattleparallelworlds.core.server.util.AbilitiesUtils.isValidItem;
import static com.jpigeon.ridebattleparallelworlds.core.server.util.RiderUtils.playHenshinSound;
import static com.jpigeon.ridebattleparallelworlds.core.server.util.RiderUtils.playSound;

public class DecadeServerHandler implements IRiderServerHandler {
    @Override
    public ResourceLocation riderId() {
        return RiderIds.DECADE_ID;
    }

    @Override
    public void onHenshinPre(HenshinEvent.@NotNull Pre event) {
        Player player = event.getPlayer();
        ResourceLocation formId = event.getFormId();

        henshinDecade(player, formId);
    }

    @Override
    public void onSwitchPre(FormSwitchEvent.@NotNull Pre event) {
        Player player = event.getPlayer();
        ResourceLocation formId = event.getNewFormId();

        henshinDecade(player, formId);
    }

    @Override
    public void onInsert(ItemInsertionEvent.@NotNull Post event) {
        Player player = event.getPlayer();
        ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack stack = event.getStack();
        if (legs.getItem() instanceof DecaDriverItem decaDriver) {
            playSound(player, ModSounds.DECADE_INSERT.get());
            scheduleTicks(5, decaDriver::triggerClose);
            if (isValidItem(stack, ModTags.Items.KAMEN_RIDE_CARDS)) {
                scheduleTicks(5, () -> playSound(player, ModSounds.KAMEN_RIDE.get()));
            } else if (isValidItem(stack, ModTags.Items.FORM_RIDE_CARDS)) {
                scheduleTicks(5, () -> playSound(player, ModSounds.FORM_RIDE.get()));
            }
        }
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
}
