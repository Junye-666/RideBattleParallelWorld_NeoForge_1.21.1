package com.jpigeon.ridebattleparallelworlds.core.server.handler.rider;

import com.jpigeon.ridebattlelib.common.api.server.IRiderServerHandler;
import com.jpigeon.ridebattlelib.server.event.FormSwitchEvent;
import com.jpigeon.ridebattlelib.server.event.HenshinEvent;
import com.jpigeon.ridebattlelib.server.event.UnhenshinEvent;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.entity.ModEntities;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.RiderIds;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.ryuki.MirrorConfig;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.ryuki.entity.RyukiHenshinEffect;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.sound.ModSounds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import static com.jpigeon.ridebattlelib.common.api.RideBattleAPI.completeIn;
import static com.jpigeon.ridebattlelib.common.api.RideBattleAPI.scheduleTicks;
import static com.jpigeon.ridebattleparallelworlds.core.server.util.RiderUtils.playSound;

public class MirrorServerHandler implements IRiderServerHandler {
    @Override
    public ResourceLocation riderId() {
        return RiderIds.MIRROR_SYSTEM_ID;
    }

    @Override
    public void onHenshinPre(HenshinEvent.@NotNull Pre event) {
        Player player = event.getPlayer();
        ResourceLocation formId = event.getFormId();

        // TODO : 镜系统完善
        playSound(player, ModSounds.MIRROR_HENSHIN.get());
        Level level = player.level();

        if (formId.equals(MirrorConfig.RYUKI_BASE_ID)) {
            RyukiHenshinEffect effect = new RyukiHenshinEffect(
                    ModEntities.RYUKI_HENSHIN_EFFECT.get(),
                    level
            );
            effect.setPos(player.position());
            effect.setYRot(player.getYRot());
            effect.setOwner(player);

            scheduleTicks(10, () -> level.addFreshEntity(effect));
            completeIn(22, player);
        }
    }

    @Override
    public void onSwitchPre(FormSwitchEvent.@NotNull Pre event) {
        // TODO：生存形态
    }

    @Override
    public void onUnhenshinPost(UnhenshinEvent.@NotNull Post event) {
        Player player = event.getPlayer();

        player.setItemSlot(EquipmentSlot.LEGS, Items.AIR.getDefaultInstance());
    }
}
