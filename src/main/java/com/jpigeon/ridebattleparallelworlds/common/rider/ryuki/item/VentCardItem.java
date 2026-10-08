package com.jpigeon.ridebattleparallelworlds.common.rider.ryuki.item;

import com.jpigeon.ridebattlelib.common.api.RideBattleAPI;
import com.jpigeon.ridebattlelib.server.event.SkillEvent;
import com.jpigeon.ridebattleparallelworlds.RideBattleParallelWorlds;
import com.jpigeon.ridebattleparallelworlds.common.registry.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * 降临卡基类。
 * <p>
 * 卡牌物品 id 形如 {@code ryuki_sword_vent_card}，
 * 对应 skillId 就是去掉 {@code _card} 后缀：
 * {@code ridebattleparallelworlds:ryuki_sword_vent}。
 * <p>
 * 玩家右键使用后自动分发 skill；skill 成功触发则消耗该卡。
 * 卡本身没有耐久 / 无堆叠上限 = 1。
 */
public class VentCardItem extends Item {
    private static final String CARD_SUFFIX = "_card";
    private final String ventPath;
    private UUID ownerId;

    /**
     * @param ventPath 共享翻译键的短名，例如 "final_vent"。
     *                 完整 key = {@code card.ridebattleparallelworlds.<ventPath>}
     */
    public VentCardItem(String ventPath, Properties properties) {
        super(properties.stacksTo(1));
        this.ventPath = ventPath;
    }

    @Override
    public @NotNull String getDescriptionId() {
        return "card." + RideBattleParallelWorlds.MODID + "." + ventPath;
    }

    /**
     * 由自身 item id 推导 skillId。
     */
    protected ResourceLocation resolveSkillId() {
        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(this);
        String path = itemId.getPath();
        if (path.endsWith(CARD_SUFFIX)) {
            path = path.substring(0, path.length() - CARD_SUFFIX.length());
        }
        return ResourceLocation.fromNamespaceAndPath(itemId.getNamespace(), path);
    }

    protected SoundEvent resolveVentSound() {
        ResourceLocation soundKey = ResourceLocation.fromNamespaceAndPath(RideBattleParallelWorlds.MODID, ventPath);
        return BuiltInRegistries.SOUND_EVENT.get(soundKey);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (level.isClientSide()) return InteractionResultHolder.success(stack);
        if (!RideBattleAPI.isTransformed(player)) return InteractionResultHolder.pass(stack);

        // TODO: 召唤器检查
        ResourceLocation skillId = resolveSkillId();
        Player owner = level.getPlayerByUUID(ownerId);
        if (owner == null) owner = player;
        boolean triggered = RideBattleAPI.triggerSkill(owner, skillId, SkillEvent.SkillTriggerType.ITEM);

        if (triggered) {
            SoundEvent sound = resolveVentSound();
            if (sound != null) {
                RideBattleAPI.playPublicSound(player, ModSounds.MIRROR_READ.get());
                RideBattleAPI.scheduleTicks(10, () -> RideBattleAPI.playPublicSound(player, sound));
            } else {
                RideBattleParallelWorlds.LOGGER.warn("降临 音效未注册: {}", ventPath);
            }
            stack.shrink(1);
        }
        return InteractionResultHolder.success(stack);
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        if (Screen.hasShiftDown()) {
            tooltipComponents.add(Component.translatable("tooltip.ventCard.owner"));
            Level level = Minecraft.getInstance().level;
            if (level != null) {
                tooltipComponents.add(Objects.requireNonNull(level.getPlayerByUUID(ownerId)).getName());
            } else {
                tooltipComponents.add(Component.literal("None"));
            }
        }
    }
}
