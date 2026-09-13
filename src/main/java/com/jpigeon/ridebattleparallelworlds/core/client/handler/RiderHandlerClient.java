package com.jpigeon.ridebattleparallelworlds.core.client.handler;

import com.jpigeon.ridebattlelib.client.event.ClientRiderEvents;
import com.jpigeon.ridebattlelib.common.api.RideBattleAPI;
import com.jpigeon.ridebattlelib.common.config.RiderConfig;
import com.jpigeon.ridebattlelib.server.event.ItemGrantEvent;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.extra.shocker.ShockerCombatManItem;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.extra.shocker.ShockerConfig;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.item.ModItems;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.RiderIds;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.agito.AlterRingItem;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.agito.armor.AgitoGroundItem;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.agito.item.FlameSaberItem;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.agito.item.ShiningCaliburItem;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.decade.DecaDriverItem;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.kuuga.ArcleItem;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.ryuki.MirrorConfig;
import com.jpigeon.ridebattleparallelworlds.core.common.registry.riders.ryuki.VBuckleItem;
import com.jpigeon.ridebattleparallelworlds.impl.playerAnimator.PlayerAnimationHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.Map;

@EventBusSubscriber(modid = RideBattleParallelWorlds.MODID, value = Dist.CLIENT)
public class RiderHandlerClient {
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();

        player.displayClientMessage(Component.translatable("message.riderGreet.login"), false);
        player.displayClientMessage(Component.translatable("message.riderHint.login").withStyle(ChatFormatting.GREEN), false);
        player.displayClientMessage(Component.translatable("message.fromHint.login").withStyle(ChatFormatting.RED), false);
        ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
        switch (legs.getItem()) {
            case ArcleItem arcle -> {
                if (!isTransformed(player)) {
                    arcle.shrinkInBody();
                    return;
                }
                ResourceLocation formId = RideBattleAPI.getCurrentFormId(player);
                if (formId == null) return;
                setDriverAnim(legs, formId);
            }
            case AlterRingItem alterRingItem -> {
                if (!isTransformed(player)) {
                    alterRingItem.shrinkInBody();
                    return;
                }
                ResourceLocation formId = RideBattleAPI.getCurrentFormId(player);
                if (formId == null) return;
                setDriverAnim(legs, formId);
            }
            case DecaDriverItem decaDriver -> {
                if (!RideBattleAPI.isDriverEmpty(player)) decaDriver.triggerOpen();
            }
            case VBuckleItem vBuckle -> vBuckle.setIdle();
            default -> {
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerEquip(LivingEquipmentChangeEvent event) {
        EquipmentSlot slot = event.getSlot();
        ItemStack stack = event.getTo();
        if (!(event.getEntity() instanceof Player player)) return;

        if (slot.isArmor()) {
            if (!isTransformed(player))
                switch (stack.getItem()) {
                    case DecaDriverItem decaDriver -> decaDriver.triggerOpen();
                    case ArcleItem arcle -> arcle.shrinkInBody();
                    case AlterRingItem alterRing -> alterRing.shrinkInBody();
                    case VBuckleItem vBuckle -> vBuckle.triggerAppear();
                    case ShockerCombatManItem ignored -> RideBattleAPI.transform(player);
                    default -> {
                    }
                }
        }
    }

    @SubscribeEvent
    public static void onGrantItem(ItemGrantEvent.Post event) {
        ItemStack stack = event.getStack();
        if (stack.getItem() instanceof FlameSaberItem flameSaber) {
            flameSaber.setClose();
        }
        if (stack.getItem() instanceof ShiningCaliburItem shiningCalibur) {
            shiningCalibur.setClose();
        }
    }

    // 接收网络包，根据具体情况分发

    @SubscribeEvent
    public static void handleClientStateEvent(ClientRiderEvents.HenshinStateChanged event) {
        Player player = event.getPlayer();
        RiderConfig config = RiderConfig.findActiveDriverConfig(player);
        if (config == null) return;
        ItemStack driver = player.getItemBySlot(config.getDriverSlot());
        ResourceLocation riderId = event.getRiderId();
        ResourceLocation formId = event.getPendingFormId();
        ClientRiderEvents.HenshinStateChanged.ChangeType changeType = event.getChangeType();

        switch (changeType) {
            case PENDING -> handleHenshinClient(player, driver, riderId, formId, isTransformed(player));
            case UNHENSHIN -> handleUnHenshinClient(player, driver, riderId, formId);
        }
    }

    @SubscribeEvent
    public static void handleClientDriverEvent(ClientRiderEvents.DriverDataChanged event) {
        Player player = event.getPlayer();
        RiderConfig config = RiderConfig.findActiveDriverConfig(player);
        if (config == null) return;
        ItemStack driver = player.getItemBySlot(config.getDriverSlot());
        Map<ResourceLocation, ItemStack> CHANGES = event.getChanges();
        CHANGES.forEach(
                (slotId, stack) -> handleDriverChangeClient(player, driver, stack)
        );
    }

    public static void handleClientSkillEvent(Player player, ResourceLocation skillId) {
        RiderConfig config = RiderConfig.findActiveDriverConfig(player);
        if (config == null) return;
        ItemStack driver = player.getItemBySlot(config.getDriverSlot());
        SkillHandlerClient.handleSkillClient(player, driver, skillId);
    }

    // 处理变身
    private static void handleHenshinClient(Player player, ItemStack driver, ResourceLocation riderId, ResourceLocation formId, boolean isTransformed) {
        // TODO: RiderHandler中所有动画逻辑搬到这里
        if (riderId.equals(RiderIds.KUUGA_ID)) {
            handleKuugaClient(player, driver, formId, isTransformed);
        } else if (riderId.equals(RiderIds.AGITO_ID)) {
            completeAgitoClient(player, driver, formId);
        } else if (riderId.equals(RiderIds.MIRROR_SYSTEM_ID)) {
            handleMirrorClient(player, driver, formId);
        } else if (riderId.equals(RiderIds.DECADE_ID)) {
            handleDecadeClient(player, driver, formId, isTransformed);
        } else {
            handleMisc(player, driver, formId);
        }
    }

    // 处理解除
    private static void handleUnHenshinClient(Player player, ItemStack driver, ResourceLocation riderId, ResourceLocation formId) {
        switch (driver.getItem()) {
            case ArcleItem arcle -> arcle.shrinkInBody();
            case AlterRingItem alterRing -> alterRing.shrinkInBody();
            case DecaDriverItem decaDriver -> decaDriver.triggerOpen();
            default -> {
            }
        }
    }

    //处理物品
    private static void handleDriverChangeClient(Player player, ItemStack driver, ItemStack stack) {
        // 处理Decade
        switch (driver.getItem()) {
            case DecaDriverItem decaDriver -> {
                if (stack != ItemStack.EMPTY) {
                    scheduleTicks(5, decaDriver::triggerClose);
                } else {
                    decaDriver.triggerOpen();
                }
            }

            // 处理Agito
            case AlterRingItem alterRing -> {
                if (stack == ItemStack.EMPTY) return;
                if (stack.is(ModItems.BURNING_ELEMENT.get()) || stack.is(ModItems.SHINING_ELEMENT.get()))
                    playAnimation(player, "agito_prepare_b");
                else playAnimation(player, "agito_prepare");
                if (alterRing.getCurrentAnimState().equals("inBody") || alterRing.getCurrentAnimState().equals("shrink")) {
                    alterRing.triggerAppear();
                    setDriverAnim(driver, RideBattleAPI.getPendingForm(player));
                }
            }
            default -> {
            }
        }
    }

    private static void handleKuugaClient(Player player, ItemStack driver, ResourceLocation formId, boolean isTransformed) {
        if (!(driver.getItem() instanceof ArcleItem arcleItem)) return;
        if (!isTransformed) {
            if (player.isCrouching()) {
                setDriverAnim(driver, formId);
                return;
            }
            playAnimation(player, "kuuga_henshin");
            if (arcleItem.getCurrentAnimState().equals("inBody") || arcleItem.getCurrentAnimState().equals("shrink")) {
                scheduleTicks(5, arcleItem::triggerAppear);
            }
        } else {
            if (player.isCrouching()) {
                setDriverAnim(driver, formId);
                return;
            }
            playAnimation(player, "kuuga_switch");
        }
        setDriverAnim(driver, formId);
    }

    private static void completeAgitoClient(Player player, ItemStack driver, ResourceLocation formId) {
        if (formId != null) {playAnimation(player, "agito_henshin");}
        scheduleTicks(10, () -> setDriverAnim(driver, formId));
        Minecraft.getInstance().getSoundManager().stop();
    }

    private static void handleMirrorClient(Player player, ItemStack driver, ResourceLocation formId) {
        if (formId.equals(MirrorConfig.RYUKI_BASE_ID)) {
            playAnimation(player, "ryuki_henshin");
        }
    }

    private static void handleDecadeClient(Player player, ItemStack driver, ResourceLocation formId, boolean isTransformed) {
        playAnimation(player, "decade_insert");
    }

    private static void handleMisc(Player player, ItemStack driver, ResourceLocation formId) {
        ItemStack head = player.getItemBySlot(EquipmentSlot.HEAD);
        if (head.getItem() instanceof AgitoGroundItem agitoGround) {
            agitoGround.setCurrentState(AgitoGroundItem.AnimState.IDLE);
        }
        if (formId.equals(ShockerConfig.COMBATMAN_ID)) {
            playAnimation(player, "shocker_greeting");
        }

    }

    public static void setDriverAnim(ItemStack driver, ResourceLocation formId) {
        if (driver.getItem() instanceof ArcleItem arcle) {
            arcle.setStateByFormId(formId);
        } else if (driver.getItem() instanceof AlterRingItem alterRing) {
            alterRing.setStateByFormId(formId);
        }
    }

    private static void playAnimation(Player player, String animationId, int fadeDuration) {
        PlayerAnimationHandler.handleAnimation(player, animationId, fadeDuration);
    }

    private static void playAnimation(Player player, String animationId) {
        PlayerAnimationHandler.handleAnimation(player, animationId, 0);
    }

    private static void scheduleTicks(int ticks, Runnable runnable) {
        RideBattleAPI.scheduleTicks(ticks, runnable);
    }

    private static boolean isTransformed(Player player) {
        return RideBattleAPI.isTransformed(player);
    }
}
