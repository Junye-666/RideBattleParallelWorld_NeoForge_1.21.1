package com.jpigeon.ridebattleparallelworlds.server.util;

import com.jpigeon.ridebattlelib.common.api.RideBattleAPI;
import com.jpigeon.ridebattlelib.common.config.FormConfig;
import com.jpigeon.ridebattlelib.server.event.UnhenshinEvent;
import com.jpigeon.ridebattleparallelworlds.Config;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.common.registry.ModSounds;
import com.jpigeon.ridebattleparallelworlds.common.rider.RiderSkillFlags;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import java.util.Optional;

/**
 * 管业务逻辑
 */
@EventBusSubscriber(modid = RideBattleParallelWorlds.MODID)
public class RiderUtils {
    @SubscribeEvent
    public static void postUnhenshin(UnhenshinEvent.Post event) {
        Player player = event.getPlayer();
        // 清除Flag缓存
        RiderSkillFlags.clearAll(player);
    }

    public static void playHenshinSound(Player player, FormConfig form) {
        Optional<SoundEvent> sound = ModSounds.getHenshinSound(form);
        if (sound.isEmpty()) return;
        playSound(player, sound.get());
    }

    public static void playSound(Player player, SoundEvent soundEvent) {
        RideBattleAPI.playPublicSound(player, soundEvent, ((float) Config.RIDER_SOUNDS_VOLUME.get() / 100));
    }
}
