package com.jpigeon.ridebattleparallelworlds.core.riders;

import com.jpigeon.ridebattlelib.api.RiderManager;
import com.jpigeon.ridebattlelib.core.system.event.FormSwitchEvent;
import com.jpigeon.ridebattlelib.core.system.event.HenshinEvent;
import com.jpigeon.ridebattlelib.core.system.event.UnhenshinEvent;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.ArcleItem;
import com.jpigeon.ridebattleparallelworlds.core.riders.kuuga.KuugaConfig;
import com.jpigeon.ridebattleparallelworlds.core.sound.ModSounds;
import com.jpigeon.ridebattleparallelworlds.impl.playerAnimator.PlayerAnimationTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = RideBattleParallelWorlds.MODID)
public class RiderHandler {
    @SubscribeEvent
    public static void onHenshin(HenshinEvent.Pre event) {
        Player player = event.getPlayer();
        ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
        AbstractClientPlayer abstractClientPlayer = getAbstractPlayer(player);
        // 处理空我
        if (event.getRiderId().equals(RiderIds.KUUGA_ID)) {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 55, 4));
            PlayerAnimationTrigger.playAnimation(abstractClientPlayer, "kuuga_henshin", 0);
            if (legs.getItem() instanceof ArcleItem arcleItem) {
                RiderManager.playPublicSound(player, ModSounds.ARCLE_APPEAR.get());
                RiderManager.scheduleTicks(5, arcleItem::triggerAppear);
                ResourceLocation formId = event.getFormId();
                RiderManager.scheduleTicks(36, () -> playHenshinSound(player, formId));
                RiderManager.completeIn(100, player);
            }
        }
    }

    @SubscribeEvent
    public static void onUnhenshin(UnhenshinEvent.Post event) {
        ItemStack legs = event.getPlayer().getItemBySlot(EquipmentSlot.LEGS);

        if (legs.getItem() instanceof ArcleItem arcle) {
            arcle.shrinkInBody();
        }
    }

    @SubscribeEvent
    public static void onSwitch(FormSwitchEvent.Pre event) {
        Player player = event.getPlayer();
        ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
        AbstractClientPlayer abstractClientPlayer = getAbstractPlayer(player);
        ResourceLocation newFormId = event.getNewFormId();
        if (RiderManager.getActiveRiderConfig(player) == KuugaConfig.KUUGA) {
            PlayerAnimationTrigger.playAnimation(abstractClientPlayer, "kuuga_henshin", 0);
            RiderManager.scheduleTicks(10, () -> setDriverAnim(legs, newFormId));
            RiderManager.scheduleTicks(36, () -> playHenshinSound(player, newFormId));
            RiderManager.completeIn(100, player);
        }

    }

    @SubscribeEvent
    public static void postHenshin(HenshinEvent.Post event) {
        ItemStack legs = event.getPlayer().getItemBySlot(EquipmentSlot.LEGS);
        ResourceLocation formId = event.getFormId();
        setDriverAnim(legs, formId);
    }

    private static AbstractClientPlayer getAbstractPlayer(Player player) {
        Minecraft mc = Minecraft.getInstance();
        return mc.player != null && mc.player.getUUID().equals(player.getUUID()) ? mc.player : null;
    }

    private static void setDriverAnim(ItemStack legs, ResourceLocation formId) {
        if (legs.getItem() instanceof ArcleItem arcle) {
            if (formId.equals(KuugaConfig.KUUGA_MIGHTY_FORM.getFormId())) {
                arcle.setCurrentState(ArcleItem.AnimState.MIGHTY);
            }
            if (formId.equals(KuugaConfig.KUUGA_DRAGON_FORM.getFormId())) {
                arcle.setCurrentState(ArcleItem.AnimState.DRAGON);
            }
            if (formId.equals(KuugaConfig.KUUGA_PEGASUS_FORM.getFormId())) {
                arcle.setCurrentState(ArcleItem.AnimState.PEGASUS);
            }
            if (formId.equals(KuugaConfig.KUUGA_TITAN_FORM.getFormId())) {
                arcle.setCurrentState(ArcleItem.AnimState.TITAN);
            }
        }
    }

    private static void playHenshinSound(Player player, ResourceLocation formId) {
        Level level = player.level();
        if (formId.equals(KuugaConfig.KUUGA_MIGHTY_FORM.getFormId())) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.KUUGA_MIGHTY.get(), SoundSource.PLAYERS);
        }
        if (formId.equals(KuugaConfig.KUUGA_DRAGON_FORM.getFormId())) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.KUUGA_DRAGON.get(), SoundSource.PLAYERS);
        }
        if (formId.equals(KuugaConfig.KUUGA_PEGASUS_FORM.getFormId())) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.KUUGA_PEGASUS.get(), SoundSource.PLAYERS);
        }
        if (formId.equals(KuugaConfig.KUUGA_TITAN_FORM.getFormId())) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.KUUGA_TITAN.get(), SoundSource.PLAYERS);
        }
    }
}
